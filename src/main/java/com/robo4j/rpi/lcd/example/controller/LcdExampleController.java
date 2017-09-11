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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */
package com.robo4j.rpi.lcd.example.controller;

import java.io.IOException;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.LifecycleState;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.logging.SimpleLoggingUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Demo;
import com.robo4j.rpi.lcd.example.demos.swing.ColorDemo;
import com.robo4j.rpi.lcd.example.demos.swing.DisplayDemo;
import com.robo4j.rpi.lcd.example.demos.swing.ExitDemo;
import com.robo4j.rpi.lcd.example.demos.swing.LcdDemo;
import com.robo4j.rpi.lcd.example.demos.swing.ScrollDemo;
import com.robo4j.units.rpi.lcd.AdafruitButtonEnum;
import com.robo4j.units.rpi.lcd.AdafruitButtonUnit;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * This controller binds together the standard {@link AdafruitLcdUnit},
 * {@link HttpUnit} and the {@link AdafruitButtonUnit} to provide a demo similar
 * to the one in {@link Demo}.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class LcdExampleController extends RoboUnit<AdafruitButtonEnum> {
	private static int currentTest = -1;
	private static final LcdDemo[] TESTS = new LcdDemo[] { new ScrollDemo(), new ColorDemo(), new DisplayDemo(), new ExitDemo() };
	private String target;

	public LcdExampleController(RoboContext context, String id) {
		super(AdafruitButtonEnum.class, context, id);
	}

	@Override
	public synchronized void onMessage(AdafruitButtonEnum message) {
		if (isRunning()) {
			SimpleLoggingUtil.print(getClass(), "Skipping " + message + " due to test already running!");
			return;
		}
		processAdafruitMessage(message);
	}

	@Override
	public void onInitialization(Configuration configuration) throws ConfigurationException {
		target = configuration.getString("target", null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException("target");
		}
	}

	@Override
	public void stop() {
		setState(LifecycleState.STOPPING);
		SimpleLoggingUtil.print(getClass(), "Clearing and shutting off display...");
		sendLcdMessage(getContext(), LcdMessage.MESSAGE_CLEAR);
		sendLcdMessage(getContext(), LcdMessage.MESSAGE_TURN_OFF);
		sendLcdMessage(getContext(), LcdMessage.MESSAGE_STOP);
		setState(LifecycleState.STOPPED);
	}

	@Override
	public void shutdown() {
		setState(LifecycleState.SHUTTING_DOWN);
		SimpleLoggingUtil.print(getClass(), "shutting off LcdExample...");
		setState(LifecycleState.SHUTDOWN);
		System.exit(0);
	}

	// Private Methods
	private void processAdafruitMessage(AdafruitButtonEnum myMessage) {
		switch (myMessage) {
		case DOWN:
			currentTest = ++currentTest > (TESTS.length - 1) ? TESTS.length - 1 : currentTest;
			sendLcdMessage(getContext(), String.format("#%d:%s     \nPress Sel to run!", currentTest, TESTS[currentTest].getName()));
			break;
		case SELECT:
			runTest(currentTest);
			break;
		case UP:
			currentTest = --currentTest < 0 ? 0 : currentTest;
			sendLcdMessage(getContext(), LcdMessage.MESSAGE_CLEAR);
			sendLcdMessage(getContext(), String.format("#%d:%s     \nPress Sel to run!", currentTest, TESTS[currentTest].getName()));
			break;
		default:
			sendLcdMessage(getContext(), LcdMessage.MESSAGE_CLEAR);
			sendLcdMessage(getContext(), String.format("Button %s\nis not in use...", myMessage));
			break;
		}
	}

	private void runTest(int currentTest) {
		LcdDemo test = TESTS[currentTest];
		SimpleLoggingUtil.print(getClass(), "Running test " + test.getName());
		try {
			test.run(getContext());
		} catch (IOException e) {
			SimpleLoggingUtil.error(getClass(), "Failed to run demo", e);
		}
	}

	private boolean isRunning() {
		return currentTest != -1 && TESTS[currentTest].isRunning();
	}

	private void sendLcdMessage(RoboContext ctx, LcdMessage message) {
		ctx.getReference(target).sendMessage(message);
	}

	private void sendLcdMessage(RoboContext ctx, String message) {
		ctx.getReference(target).sendMessage(new LcdMessage(message));
	}
}
