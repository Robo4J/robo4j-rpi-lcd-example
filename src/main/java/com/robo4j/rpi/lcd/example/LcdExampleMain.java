/*
 * Copyright (c) 2014, 2017, Marcus Hirt, Miroslav Wengner
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
package com.robo4j.rpi.lcd.example;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.client.util.RoboHttpUtils;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.unit.HttpServerUnit;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;
import com.robo4j.rpi.lcd.example.controller.LcdExampleController;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.ButtonUnit;
import com.robo4j.units.rpi.lcd.I2CRoboUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

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
 * @since 22.09.2016
 */
public class LcdExampleMain {
	private static int PORT = 8025;
	private static String PATH = "lcd";

	public static void main(String[] args) throws Exception {
		RoboSystem system = new RoboSystem();

		ButtonUnit buttons = new ButtonUnit(system, "buttons");
		Configuration config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS);
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS);
		buttons.initialize(config);

		HttpServerUnit http = new HttpServerUnit(system, "http");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		config.setInteger("port", PORT);
		/* put target units and access method*/
		Configuration targetUnits = config.createChildConfiguration(RoboHttpUtils.HTTP_TARGET_UNITS);
		targetUnits.setString("controller", "GET");
		http.initialize(config);

		LcdExampleController ctrl = new LcdExampleController(system, "controller");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", PATH);
		ctrl.initialize(config);

		AdafruitLcdUnit lcd = new AdafruitLcdUnit(system, "lcd");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS);
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS);
		lcd.initialize(config);

		system.addUnits(buttons, ctrl, http, lcd);

		System.out.println("State before start:");
		System.out.println(SystemUtil.generateStateReport(system));
		system.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.generateStateReport(system));

		system.getReference("lcd").sendMessage(new LcdMessage("Robo4J: Welcome!\nPress Up/Down!"));
		System.out.println("RoboSystem http server\n\tPort:" + PORT + "\n");
		System.out.println("Usage:\n\tRequest GET: http://<IP_ADDRESS>:" + PORT + "/controller?button=down");
		System.out.println("\tRequest command types: up, down, select, left, right\n");

		System.out.println("Press enter to quit!");
		System.in.read();
		system.shutdown();

	}

}
