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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.robo4j.commons.agent.AgentConsumer;
import com.robo4j.commons.agent.AgentProducer;
import com.robo4j.commons.agent.AgentStatus;
import com.robo4j.commons.agent.GenericAgent;
import com.robo4j.commons.agent.ProcessAgent;
import com.robo4j.commons.annotation.RoboUnitProducer;
import com.robo4j.commons.command.AdafruitLcdCommandEnum;
import com.robo4j.commons.command.GenericCommand;
import com.robo4j.commons.command.RoboUnitCommand;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.commons.unit.DefaultUnit;
import com.robo4j.commons.unit.GenericUnit;
import com.robo4j.commons.unit.UnitProducer;
import com.robo4j.core.client.command.ClientCommandProperties;
import com.robo4j.core.util.ConstantUtil;
import com.robo4j.hw.rpi.i2c.adafruitlcd.*;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 18.01.2017
 */

@RoboUnitProducer(id = LcdButtonProducer.ID)
public class LcdButtonProducer extends DefaultUnit<UnitProducer> implements UnitProducer, GeneralLcdProducer {

    public final static String ID = "lcd_buttons";
    private volatile GenericUnit genericUnit;
    private volatile ILCD lcd;
    private String name;

    public LcdButtonProducer() {
        this.name = ID;
        this.active = new AtomicBoolean(false);
    }

    @Override
    public void setParentUnit(GenericUnit genericUnit) {
        this.genericUnit = genericUnit;
    }

    @Override
    public void setLcdPlate(ILCD lcd){
        this.lcd = lcd;
    }

    @Override
    protected GenericAgent createAgent(String s, AgentProducer agentProducer, AgentConsumer agentConsumer) {
        return null;
    }

    @Override
    public Map<RoboUnitCommand, Function<ProcessAgent, AgentStatus>> initLogic() {
        ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
        observer.addButtonListener((Button button) -> {
            SimpleLoggingUtil.debug(getClass(), "Button Press: " + button + " lcd: " + lcd);
            switch (button) {
                case UP:
                    genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.BUTTON_UP));
                    break;
                case DOWN:
                    genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.BUTTON_DOWN));
                    break;
                case RIGHT:
                    genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.BUTTON_RIGHT));
                    break;
                case LEFT:
                    genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.BUTTON_LEFT));
                    break;
                case SELECT:
                     genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.BUTTON_SET));
                    break;
                default:
                    genericUnit.process(getLcdCommand(AdafruitLcdCommandEnum.EXIT));
            }
        });

        return null;
    }

    private GenericCommand<AdafruitLcdCommandEnum> getLcdCommand(final AdafruitLcdCommandEnum command){
        final ClientCommandProperties properties = new ClientCommandProperties(0);
        return new GenericCommand<>(properties, command, "", ConstantUtil.DEFAULT_PRIORITY);
    }

    @Override
    public UnitProducer init(Object o) {
        SimpleLoggingUtil.debug(getClass(), "INIT STARTED : " + executorForAgents);
        if (Objects.nonNull(executorForAgents)) {
            active.set(true);
            logic = initLogic();
            SimpleLoggingUtil.debug(getClass(), "INIT finished");
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
        SimpleLoggingUtil.debug(getClass(), "process", roboUnitCommand.toString());
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

}
