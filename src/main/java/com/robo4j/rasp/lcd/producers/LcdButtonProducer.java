/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This LcdButtonProducer.java  is part of robo4j.
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

package com.robo4j.rasp.lcd.producers;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import com.pi4j.io.i2c.I2CFactory;
import com.robo4j.commons.agent.AgentConsumer;
import com.robo4j.commons.agent.AgentProducer;
import com.robo4j.commons.agent.AgentStatus;
import com.robo4j.commons.agent.GenericAgent;
import com.robo4j.commons.agent.ProcessAgent;
import com.robo4j.commons.annotation.RoboUnitProducer;
import com.robo4j.commons.command.RoboUnitCommand;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.commons.unit.DefaultUnit;
import com.robo4j.commons.unit.UnitProducer;
import com.robo4j.core.platform.ClientPlatformException;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AutoScrollDemo;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Button;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ButtonListener;
import com.robo4j.hw.rpi.i2c.adafruitlcd.ButtonPressedObserver;
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
 * @since 18.01.2017
 */

//TODO: implement extends DefaultUnit<UnitProducer>
@RoboUnitProducer(id = LcdButtonProducer.ID)
public class LcdButtonProducer extends DefaultUnit<UnitProducer> implements UnitProducer {

    public final static String ID = "lcd_buttons";
    private static final LCDTest[] STAGES_TEST = { new HelloWorldTest(),
            new ScrollTest(), new CursorDemo(), new DisplayDemo(),
            new ColorDemo(), new AutoScrollDemo(), new ExitTest() };

    private int currentTest = -1;
    private ILCD lcd;
    private String name;

    public LcdButtonProducer() {
        this.name = ID;
        try {
            lcd = new RealLCD();
            lcd.setText("Robo4J.io LCD!\nSocket & up/down...");

            basicInitiation(lcd);
        } catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
            SimpleLoggingUtil.error(getClass(), "error: " + e);
        }
    }

    @Override
    protected GenericAgent createAgent(String s, AgentProducer agentProducer, AgentConsumer agentConsumer) {
        return null;
    }

    @Override
    public Map<RoboUnitCommand, Function<ProcessAgent, AgentStatus>> initLogic() {
        SimpleLoggingUtil.debug(getClass(), "INIT LOGIC");
        return null;
    }

    @Override
    public UnitProducer init(Object o) {
        SimpleLoggingUtil.debug(getClass(), "INIT");
        if (Objects.nonNull(executorForAgents)) {
            if (!agents.isEmpty()) {
                active.set(true);
                logic = initLogic();
            }
        }

        return this;
    }

    @Override
    public void setExecutor(ExecutorService executor) {
        SimpleLoggingUtil.debug(getClass(), "SET EXECUTOR");
        this.executorForAgents = executor;
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public boolean process(RoboUnitCommand roboUnitCommand) {
        return false;
    }

    @Override
    public String getUnitName() {
        return name;
    }

    @Override
    public String getSystemName() {
        return null;
    }

    @Override
    public String[] getProducerName() {
        return new String[0];
    }

    @Override
    public String getConsumerName() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    /* to be replace */
    private void basicInitiation(final ILCD lcd) throws IOException, I2CFactory.UnsupportedBusNumberException {
        ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
        observer.addButtonListener(new ButtonListener() {
            @Override
            public void onButtonPressed(Button button) {
                try {
                    switch (button) {
                        case UP:
                            currentTest = --currentTest < 0 ? 0 : currentTest;
                            lcd.clear();
                            lcd.setText(String.format("#%d:%s\nPress SET to run!",
                                    currentTest, STAGES_TEST[currentTest].getName()));
                            break;
                        case DOWN:
                            currentTest = ++currentTest > (STAGES_TEST.length - 1) ? STAGES_TEST.length - 1
                                    : currentTest;
                            lcd.clear();
                            lcd.setText(String.format("#%d:%s\nPress SET to run!",
                                    currentTest, STAGES_TEST[currentTest].getName()));
                            break;
                        case RIGHT:
                            lcd.scrollDisplay(RealLCD.Direction.LEFT);
                            break;
                        case LEFT:
                            lcd.scrollDisplay(RealLCD.Direction.RIGHT);
                            break;
                        case SELECT:
                            runTest(currentTest);
                            break;
                        default:
                            lcd.clear();
                            lcd.setText(String.format(
                                    "Button %s\nis not in use...",
                                    button.toString()));
                    }
                } catch (IOException e) {
                    throw new ClientPlatformException("LCD communication", e);
                }
            }

            private void runTest(int currentTest) {
                LCDTest test = STAGES_TEST[currentTest];
                SimpleLoggingUtil.debug(getClass(), "Running test " + test.getName());
                try {
                    test.run(lcd);
                } catch (IOException e) {
                    throw new ClientPlatformException("LCD Communication ", e);
                }
            }
        });

    }
}
