/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This LcdExampleController.java  is part of robo4j.
 * module: robo4j-rpi-lcd-example
 *
 * robo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * robo4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */
package com.robo4j.rpi.lcd.example.controller;

import java.io.IOException;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboResult;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.logging.SimpleLoggingUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Demo;
import com.robo4j.rpi.lcd.example.demos.AbstractDemo;
import com.robo4j.rpi.lcd.example.demos.ColorDemo;
import com.robo4j.rpi.lcd.example.demos.DisplayDemo;
import com.robo4j.rpi.lcd.example.demos.ExitDemo;
import com.robo4j.rpi.lcd.example.demos.LcdDemo;
import com.robo4j.rpi.lcd.example.demos.ScrollDemo;
import com.robo4j.units.rpi.lcd.AdafruitButtonPlateEnum;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.ButtonUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * This controller binds together the standard {@link AdafruitLcdUnit}, {@link HttpUnit} and the
 * {@link ButtonUnit} to provide a demo similar to the one in {@link Demo}.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 * @since 22.09.2016
 */
public class LcdExampleController extends RoboUnit<String> {
	private static int currentTest = -1;
	private final static LcdDemo[] TESTS = new LcdDemo[] { new ScrollDemo(), new ColorDemo(), new DisplayDemo(),
			new ExitDemo() };
	private String target;

	public LcdExampleController(RoboContext context, String id) {
		super(context, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RoboResult<String, ?> onMessage(Object message) {

		if (message instanceof AdafruitButtonPlateEnum) {
			AdafruitButtonPlateEnum myMessage = (AdafruitButtonPlateEnum) message;
			processAdaruitMessage(myMessage);
		}
		if (message instanceof String) {
			AdafruitButtonPlateEnum myMessage = AdafruitButtonPlateEnum.getByText(message.toString());
			SimpleLoggingUtil.debug(getClass(), "message text: " + message + " myMessage: " + myMessage);
			processAdaruitMessage(myMessage);
		}

		return null;
	}

	@Override
	public void onInitialization(Configuration configuration) throws ConfigurationException {
		target = configuration.getString("target", null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException("target");
		}
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

	// Private Methods
	private void sendLcdMessage(RoboContext ctx, LcdMessage message) {
		ctx.getReference(target).sendMessage(message);
	}

	private void sendLcdMessage(RoboContext ctx, String message) {
		ctx.getReference(target).sendMessage(new LcdMessage(message));
	}

	private void processAdaruitMessage(AdafruitButtonPlateEnum myMessage) {
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
	 * @param e - IOException
	 */
	private void handleException(IOException e) {
		SimpleLoggingUtil.error(getClass(), "Failed to run demo", e);
	}


}
