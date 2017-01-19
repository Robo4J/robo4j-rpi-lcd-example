/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This ClientLcdConsumer.java  is part of robo4j.
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

package com.robo4j.rasp.lcd.platform;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;

import com.robo4j.commons.agent.AgentConsumer;
import com.robo4j.commons.command.AdafruitLcdCommandEnum;
import com.robo4j.commons.command.GenericCommand;
import com.robo4j.commons.concurrent.CoreBusQueue;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.core.platform.ClientPlatformException;
import com.robo4j.core.util.ConstantUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AutoScrollDemo;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ColorDemo;
import com.robo4j.hw.rpi.i2c.adafruitlcd.CursorDemo;
import com.robo4j.hw.rpi.i2c.adafruitlcd.DisplayDemo;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ExitTest;
import com.robo4j.hw.rpi.i2c.adafruitlcd.HelloWorldTest;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ILCD;
import com.robo4j.hw.rpi.i2c.adafruitlcd.LCDTest;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ScrollTest;
import com.robo4j.hw.rpi.i2c.adafruitlcd.impl.RealLCD;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 15.01.2017
 */
public class ClientLcdPlatformConsumer implements AgentConsumer, Callable<Boolean> {

    private static int currentTest = -1;

    private static final LCDTest[] STAGES_TEST = { new HelloWorldTest(),
            new ScrollTest(), new CursorDemo(), new DisplayDemo(),
            new ColorDemo(), new AutoScrollDemo(), new ExitTest() };

    private volatile ILCD lcd;
    private ExecutorService executor;
    private Exchanger<GenericCommand<AdafruitLcdCommandEnum>> exchanger;


    public ClientLcdPlatformConsumer(final ILCD lcd, final ExecutorService executor,
                                  final Exchanger<GenericCommand<AdafruitLcdCommandEnum>> exchanger) {
        this.lcd = lcd;
        this.executor = executor;
        this.exchanger = exchanger;
        SimpleLoggingUtil.debug(getClass(), "INIT");
    }

    @Override
    public void setMessageQueue(CoreBusQueue commandsQueue) {
        throw new ClientPlatformException("NOT IMPLEMENTED messageQueue");
    }

    @Override
    public Boolean call() throws Exception {
        final GenericCommand<AdafruitLcdCommandEnum> command = exchanger.exchange(null);
        SimpleLoggingUtil.debug(getClass(), "MAGIC COMMAND : " + command);
        SimpleLoggingUtil.debug(getClass(), "MAGIC COMMAND : " + currentTest);
        switch (command.getType()) {
            case BUTTON_UP:
                currentTest = --currentTest < 0 ? 0 : currentTest;
                lcd.clear();

                SimpleLoggingUtil.debug(getClass(), "UP: " + String.format("#%d:%s\nPress SET to run!",
                        currentTest, STAGES_TEST[currentTest].getName()));
                lcd.setText(String.format("#%d:%s\nPress SET to run!",
                        currentTest, STAGES_TEST[currentTest].getName()));
                return true;
            case BUTTON_DOWN:
                currentTest = ++currentTest > (STAGES_TEST.length - 1) ? STAGES_TEST.length - 1
                        : currentTest;
                lcd.clear();
                SimpleLoggingUtil.debug(getClass(), "DOWN: " + String.format("#%d:%s\nPress SET to run!",
                        currentTest, STAGES_TEST[currentTest].getName()));
                lcd.setText(String.format("#%d:%s\nPress SET to run!",
                        currentTest, STAGES_TEST[currentTest].getName()));
                return true;
            case BUTTON_LEFT:
                lcd.scrollDisplay(RealLCD.Direction.RIGHT);
                return true;
            case BUTTON_RIGHT:
                lcd.scrollDisplay(RealLCD.Direction.LEFT);
                return true;
            case BUTTON_SET:
                LCDTest test = STAGES_TEST[currentTest];
                SimpleLoggingUtil.debug(getClass(), "Running test " + test.getName());
                try {
                    test.run(lcd);
                } catch (IOException e) {
                    throw new ClientPlatformException("LCD Communication ", e);
                }
                return true;
            case EXIT:
                SimpleLoggingUtil.debug(getClass(), "MAGIC COMMAND EXIT : " + command);
                return false;
            default:
                throw new ClientPlatformException("PLATFORM COMMAND= " + command);
        }

    }


    // Private Methods
    private boolean commandEmpty(final String value) {
        return value.equals(ConstantUtil.EMPTY_STRING);
    }

}
