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

import static com.robo4j.rpi.lcd.example.LcdExampleDeclarativeMain.INIT_MESSAGE;

import com.robo4j.RoboBuilder;
import com.robo4j.RoboContext;
import com.robo4j.configuration.Configuration;
import com.robo4j.configuration.ConfigurationBuilder;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;
import com.robo4j.rpi.lcd.example.controller.LcdExampleController;
import com.robo4j.socket.http.units.HttpServerUnit;
import com.robo4j.units.rpi.I2CRoboUnit;
import com.robo4j.units.rpi.lcd.AdafruitButtonUnit;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;
import com.robo4j.util.SystemUtil;

/**
 * Demo using Robo4J to send messages from the buttons on an Adafruit 2x16 LCD
 * Unit to the LCD via a controller.
 * 
 * @see AdafruitLcdUnit
 * @see AdafruitButtonUnit
 * @see AdafruitLcd
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class LcdExampleMain {

	public static void main(String[] args) throws Exception {
		// basic configuration
		final int port = 8025;
		final String lcdUnitName = "lcd";

		final RoboBuilder roboBuilder = new RoboBuilder();

		// Adafruit: button plate Controller
		Configuration config = new ConfigurationBuilder().addString("target", "controller")
				.addInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS)
				.addInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS).build();
		roboBuilder.add(AdafruitButtonUnit.class, config, "buttons");

		// Adafruit: lcd controller
		config = new ConfigurationBuilder().addInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS)
				.addInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS).build();
		roboBuilder.add(AdafruitLcdUnit.class, config, lcdUnitName);

		// Robo4J: lcd example controller
		config = new ConfigurationBuilder().addString("target", lcdUnitName).build();
		roboBuilder.add(LcdExampleController.class, config, "controller");

		// Robo4J: Http end-point
		config = new ConfigurationBuilder().addString("target", "controller").addInteger("port", port)
				.addString("packages", "com.robo4j.rpi.lcd.example.codec")
				.addString("unitPathsConfig", "[{\"roboUnit\":\"controller\",\"method\":\"POST\"}]").build();
		roboBuilder.add(HttpServerUnit.class, config, "http");

		// Robo4J: build system
		final RoboContext roboSystem = roboBuilder.build();

		// Robo4J: Start demo
		System.out.println("State before start:");
		System.out.println(SystemUtil.printStateReport(roboSystem));
		roboSystem.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.printStateReport(roboSystem));

		roboSystem.getReference("lcd").sendMessage(new LcdMessage(INIT_MESSAGE));
		System.out.println("Press enter to quit!");
		System.in.read();
		roboSystem.shutdown();
	}
}
