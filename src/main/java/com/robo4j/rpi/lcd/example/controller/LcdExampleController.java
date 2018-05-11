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

import com.robo4j.ConfigurationException;
import com.robo4j.CriticalSectionTrait;
import com.robo4j.LifecycleState;
import com.robo4j.RoboContext;
import com.robo4j.RoboReference;
import com.robo4j.RoboUnit;
import com.robo4j.configuration.Configuration;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Demo;
import com.robo4j.logging.SimpleLoggingUtil;
import com.robo4j.rpi.lcd.example.demos.ColorDemo;
import com.robo4j.rpi.lcd.example.demos.DisplayDemo;
import com.robo4j.rpi.lcd.example.demos.ExitDemo;
import com.robo4j.rpi.lcd.example.demos.LcdDemo;
import com.robo4j.rpi.lcd.example.demos.ScrollDemo;
import com.robo4j.units.rpi.lcd.AdafruitButtonEnum;
import com.robo4j.units.rpi.lcd.AdafruitButtonUnit;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * This controller binds together the standard {@link AdafruitLcdUnit},
 * {@link com.robo4j.socket.http.units.HttpServerUnit} and the {@link AdafruitButtonUnit} to provide a demo similar
 * to the one in {@link Demo}.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
@CriticalSectionTrait
public class LcdExampleController extends RoboUnit<AdafruitButtonEnum> {
	public static final String PROPERTY_TARGET = "target";
	private static int currentDemo = -1;
	private RoboReference<LcdMessage> targetLcd;
	private String target;
	private LcdDemo[] demos;

	public LcdExampleController(RoboContext context, String id) {
		super(AdafruitButtonEnum.class, context, id);
	}

	@Override
	public void onMessage(AdafruitButtonEnum message) {
		if (!isDemoRunning()) {
			processAdafruitMessage(message);
		} else {
			SimpleLoggingUtil.print(getClass(), "Skipping " + message + " due to test already running!");
		}
	}

	@Override
	public void onInitialization(Configuration configuration) throws ConfigurationException {
		target = configuration.getString(PROPERTY_TARGET, null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException(PROPERTY_TARGET);
		}
	}

	@Override
	public void start() {
		RoboContext ctx = getContext();
		targetLcd = ctx.getReference(target);
		demos = new LcdDemo[] { new ScrollDemo(ctx, targetLcd), new ColorDemo(ctx, targetLcd),
				new DisplayDemo(ctx, targetLcd), new ExitDemo(ctx, targetLcd) };
	}

	@Override
	public void stop() {
		setState(LifecycleState.STOPPING);
		SimpleLoggingUtil.print(getClass(), "Clearing and shutting off display...");
		sendLcdMessageString(LcdMessage.MESSAGE_CLEAR);
		sendLcdMessageString(LcdMessage.MESSAGE_TURN_OFF);
		sendLcdMessageString(LcdMessage.MESSAGE_STOP);
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
			moveToNextDemo();
			break;
		case UP:
			moveToPreviousDemo();
			break;
		case SELECT:
			runDemo();
			break;
		default:
			sendLcdMessageString(String.format("Button %s\nis not in use...", myMessage));
		}
	}

	private void moveToPreviousDemo() {
		currentDemo = --currentDemo < 0 ? 0 : currentDemo;
		sendLcdMessageString(String.format("#%d:%s\nPress Sel to run!", currentDemo, demos[currentDemo].getName()));
	}

	private void moveToNextDemo() {
		currentDemo = ++currentDemo > (demos.length - 1) ? demos.length - 1 : currentDemo;
		sendLcdMessageString(String.format("#%d:%s\nPress Sel to run!", currentDemo, demos[currentDemo].getName()));
	}

	private void runDemo() {
		LcdDemo test = demos[currentDemo];
		SimpleLoggingUtil.print(getClass(), "Running test " + test.getName());
		try {
			test.run();
		} catch (IOException e) {
			SimpleLoggingUtil.error(getClass(), "Failed to run demo", e);
		}
	}

	private boolean isDemoRunning() {
		return currentDemo != -1 && demos[currentDemo].isRunning();
	}

	private void sendLcdMessageString(LcdMessage message) {
		targetLcd.sendMessage(message);
	}

	private void sendLcdMessageString(String message) {
		targetLcd.sendMessage(new LcdMessage(message));
	}
}
