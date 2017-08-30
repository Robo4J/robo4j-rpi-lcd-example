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

import java.util.Collections;
import java.util.Map;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;
import com.robo4j.rpi.lcd.example.controller.LcdExampleController;
import com.robo4j.socket.http.units.HttpServerUnit;
import com.robo4j.socket.http.util.JsonUtil;
import com.robo4j.units.rpi.I2CRoboUnit;
import com.robo4j.units.rpi.lcd.AdafruitButtonUnit;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

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
	private static int PORT = 8025;
	private static String PATH = "lcd";

	public static void main(String[] args) throws Exception {
		RoboSystem system = new RoboSystem();

		AdafruitButtonUnit buttons = new AdafruitButtonUnit(system, "buttons");
		Configuration config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS);
		config.setInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS);
		buttons.initialize(config);

		HttpServerUnit http = new HttpServerUnit(system, "http");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		config.setInteger("port", PORT);
		config.setString("packages", "com.robo4j.rpi.lcd.example.codec");
		/* put target units and access method */
		Map<String, Object> httpServerConfig = Collections.singletonMap("controller", "GET");
		config.setString("targetUnits", JsonUtil.getJsonByMap(httpServerConfig));
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
		System.out.println(SystemUtil.printStateReport(system));
		system.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.printStateReport(system));

		system.getReference("lcd").sendMessage(new LcdMessage(INIT_MESSAGE));
		System.out.println(SystemUtil.printSocketEndPoint(http, ctrl));
		System.out.println("Press enter to quit!");
		System.in.read();
		system.shutdown();

	}

}
