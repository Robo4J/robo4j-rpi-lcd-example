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
package com.robo4j.rpi.lcd;

import java.util.HashMap;
import java.util.Map;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;
import com.robo4j.rpi.lcd.controllers.LcdExampleController;

/**
 * Demo using Robo4J to send messages from the buttons on an Adafruit 2x16 LCD
 * Unit to the LCD via a controller.
 * 
 * @see AdafruitLcdUnit
 * @see ButtonUnit
 * @see AdafruitLcd
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class LcdExampleMain {
	public static void main(String[] args) throws Exception {
		RoboSystem system = new RoboSystem();

		ButtonUnit buttons = new ButtonUnit(system, "buttons");
		Map<String, String> properties = createSingleValueProps("target", "controller");
		properties.put(I2CRoboUnit.PROPERTY_KEY_ADDRESS, String.valueOf(AdafruitLcd.DEFAULT_ADDRESS));
		properties.put(I2CRoboUnit.PROPERTY_KEY_BUS, String.valueOf(AdafruitLcd.DEFAULT_BUS));
		buttons.initialize(properties);

		LcdExampleController ctrl = new LcdExampleController(system, "controller");
		ctrl.initialize(createSingleValueProps("target", "lcd"));

		AdafruitLcdUnit lcd = new AdafruitLcdUnit(system, "lcd");
		properties = createSingleValueProps(I2CRoboUnit.PROPERTY_KEY_ADDRESS, String.valueOf(AdafruitLcd.DEFAULT_ADDRESS));
		properties.put(I2CRoboUnit.PROPERTY_KEY_ADDRESS, String.valueOf(AdafruitLcd.DEFAULT_ADDRESS));
		properties.put(I2CRoboUnit.PROPERTY_KEY_BUS, String.valueOf(AdafruitLcd.DEFAULT_BUS));
		lcd.initialize(properties);
		system.addUnits(buttons, ctrl, lcd);

		System.out.println(SystemUtil.generateStateReport(system));
		system.start();
		system.sendMessage("lcd", new LcdMessage("Robo4J: Welcome!\nPress Up/Down!"));
		System.out.println(SystemUtil.generateStateReport(system));
		System.out.println("Press enter to quit!");
		System.in.read();
		system.shutdown();
	}

	public static Map<String, String> createSingleValueProps(String key, String value) {
		Map<String, String> properties = new HashMap<>();
		properties.put(key, value);
		return properties;
	}
}
