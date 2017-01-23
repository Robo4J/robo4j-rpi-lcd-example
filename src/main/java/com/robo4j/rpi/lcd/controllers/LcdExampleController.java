/*
 * Copyright (c) 2014, 2017, Miroslav Wengner, Marcus Hirt
 * 
 * Robo4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Robo4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Robo4J. If not, see <http://www.gnu.org/licenses/>.
 */
package com.robo4j.rpi.lcd.controllers;

import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboResult;
import com.robo4j.core.RoboUnit;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Demo;
import com.robo4j.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.rpi.lcd.ButtonUnit;
import com.robo4j.rpi.lcd.ButtonUnit.Messages;
import com.robo4j.rpi.lcd.LcdMessage;
import com.robo4j.rpi.lcd.examples.AbstractDemo;
import com.robo4j.rpi.lcd.examples.ColorDemo;
import com.robo4j.rpi.lcd.examples.DisplayDemo;
import com.robo4j.rpi.lcd.examples.ExitDemo;
import com.robo4j.rpi.lcd.examples.LcdDemo;
import com.robo4j.rpi.lcd.examples.ScrollDemo;

import java.io.IOException;
import java.util.Map;

/**
 * This controller binds together the standard {@link AdafruitLcdUnit} and the {@link ButtonUnit} to provide a demo similar to the one in {@link Demo}.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class LcdExampleController extends RoboUnit<String> {
	private static int currentTest = -1;
	private final static LcdDemo[] TESTS = new LcdDemo[] { new ScrollDemo(), new ColorDemo(), new DisplayDemo(), new ExitDemo() };
	private String target;

	public LcdExampleController(RoboContext context, String id) {
		super(context, id);
	}

	@Override
	public RoboResult<?> onMessage(Object message) {
		ButtonUnit.Messages myMessage = (Messages) message;
		switch (myMessage) {
		case DOWN:
			currentTest = ++currentTest > (TESTS.length - 1) ? TESTS.length - 1 : currentTest;
			sendLcdMessage(getContext(),
					String.format("#%d:%s     \nPress Sel to run!", currentTest, TESTS[currentTest].getName()));
			break;
		case SELECT:
			runTest(currentTest);
			break;
		case UP:
			currentTest = --currentTest < 0 ? 0 : currentTest;
			sendLcdMessage(getContext(), AbstractDemo.CLEAR);
			sendLcdMessage(getContext(),
					String.format("#%d:%s     \nPress Sel to run!", currentTest, TESTS[currentTest].getName()));
			break;
		default:
			sendLcdMessage(getContext(), AbstractDemo.CLEAR);
			sendLcdMessage(getContext(), String.format("Button %s\nis not in use...", myMessage));
			break;

		}
		return null;
	}
	
	private void runTest(int currentTest) {
		LcdDemo test = TESTS[currentTest];
		System.out.println("Running test " + test.getName());
		try {
			test.run(getContext());
		} catch (IOException e) {
			handleException(e);
		}
	}

	/**
	 * @param e
	 */
	private void handleException(IOException e) {
		SimpleLoggingUtil.error(getClass(), "Failed to run demo", e);
	}

	@Override
	public void initialize(Map<String, String> properties) throws Exception {
		super.initialize(properties);
		target = properties.get("target");
	}

	protected void sendLcdMessage(RoboContext ctx, LcdMessage message) {
		ctx.getRoboUnit("controller").sendMessage(target, message);
	}

	protected void sendLcdMessage(RoboContext ctx, String message) {
		ctx.getRoboUnit("controller").sendMessage(target, new LcdMessage(message));
	}

	@Override
	public void shutdown() {
		System.out.println("Clearing and shutting off display...");
		sendLcdMessage(getContext(), AbstractDemo.CLEAR);
		sendLcdMessage(getContext(), AbstractDemo.TURN_OFF);
		sendLcdMessage(getContext(), AbstractDemo.STOP);
		super.shutdown();
		System.exit(0);
	}

}
