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
import com.robo4j.core.unit.HttpClientUnit;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;
import com.robo4j.rpi.lcd.example.controller.LcdCommandExampleController;
import com.robo4j.units.rpi.lcd.AdafruitLcdUnit;
import com.robo4j.units.rpi.lcd.ButtonUnit;
import com.robo4j.units.rpi.lcd.I2CRoboUnit;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 *
 * This is example to control remotely lego robot
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class LcdCommandExampleMain {

    private static final String PATH = "lcd";
    private static final String CLIENT_NAME = "http_client";
    private static final String CLIENT_IP = "192.168.178.26";
    private static final Integer CLIENT_PORT = 8025;

    public static void main(String[] args) throws Exception {
        RoboSystem system = new RoboSystem();

        ButtonUnit buttons = new ButtonUnit(system, "buttons");
        Configuration config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("target", "controller");
        config.setString("type", "controll");
        config.setInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS);
        config.setInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS);
        buttons.initialize(config);

        LcdCommandExampleController ctrl = new LcdCommandExampleController(system, "controller");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("target", PATH);
        config.setString("target_out", CLIENT_NAME);
        config.setString("client", CLIENT_IP);
        config.setString("client_port", CLIENT_PORT.toString());
        config.setString("client_path", "/tank?");
        ctrl.initialize(config);

        AdafruitLcdUnit lcd = new AdafruitLcdUnit(system, PATH);
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setInteger(I2CRoboUnit.PROPERTY_KEY_ADDRESS, AdafruitLcd.DEFAULT_ADDRESS);
        config.setInteger(I2CRoboUnit.PROPERTY_KEY_BUS, AdafruitLcd.DEFAULT_BUS);
        lcd.initialize(config);

        HttpClientUnit httpClient = new HttpClientUnit(system, CLIENT_NAME);
        config.setString("address", CLIENT_IP);
        config.setInteger("port", CLIENT_PORT);
		/* specific configuration */
        Configuration commands = config.createChildConfiguration(RoboHttpUtils.HTTP_COMMANDS);
        commands.setString("path", "tank");
        commands.setString("method", "GET");
        commands.setString("up", "move");
        commands.setString("down", "back");
        commands.setString("left", "right");
        commands.setString("right", "left");
        httpClient.initialize(config);

        system.addUnits(buttons, ctrl, lcd, httpClient);

        System.out.println("State before start:");
        System.out.println(SystemUtil.generateStateReport(system));
        system.start();

        System.out.println("State after start:");
        System.out.println(SystemUtil.generateStateReport(system));
        lcd.sendMessage(new LcdMessage("Robo4J: Welcome!\nPress Up/Down!"));

        System.out.println("Press enter to quit!");
        System.in.read();
        lcd.stop();
        system.shutdown();

    }

}
