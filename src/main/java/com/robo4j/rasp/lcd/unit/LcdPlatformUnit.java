/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This LcdUnit.java  is part of robo4j.
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

package com.robo4j.rasp.lcd.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.robo4j.commons.agent.AgentConsumer;
import com.robo4j.commons.agent.AgentProducer;
import com.robo4j.commons.agent.AgentStatus;
import com.robo4j.commons.agent.GenericAgent;
import com.robo4j.commons.agent.ProcessAgent;
import com.robo4j.commons.agent.ProcessAgentBuilder;
import com.robo4j.commons.annotation.RoboUnit;
import com.robo4j.commons.command.AdafruitLcdCommandEnum;
import com.robo4j.commons.command.GenericCommand;
import com.robo4j.commons.command.RoboUnitCommand;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.commons.unit.DefaultUnit;
import com.robo4j.core.platform.ClientPlatformException;
import com.robo4j.rasp.lcd.platform.ClientLcdPlatformConsumer;
import com.robo4j.rasp.lcd.platform.ClientLcdPlatformProducer;
import com.robo4j.rpi.unit.RpiUnit;

/**
 *
 * Lcd Unit is responsible to init and manage interaction
 * between Consumer and Producer
 *
 * @author Miro Wengner (@miragemiko)
 * @since 15.01.2017
 */


@RoboUnit(id = LcdPlatformUnit.UNIT_NAME,
        system = LcdPlatformUnit.SYSTEM_NAME,
        producer = LcdPlatformUnit.PRODUCER_NAME,
        consumer = "lcd_display")
public class LcdPlatformUnit extends DefaultUnit<RpiUnit> implements RpiUnit {

    private static final int AGENT_PLATFORM_POSITION = 0;
    static final String UNIT_NAME = "lcdUnit";
    static final String SYSTEM_NAME = "lcdBrick";
    static final String PRODUCER_NAME = "default";
    static final String CONSUMER_NAME = "adafruitLCD";

    private volatile LinkedBlockingQueue<GenericCommand<AdafruitLcdCommandEnum>> commandQueue;

    public LcdPlatformUnit() {
        SimpleLoggingUtil.debug(getClass(), "Constructor: LcdUnit");
    }

    @Override
    public void setExecutor(final ExecutorService executor) {
        this.executorForAgents = executor;
    }

    @Override
    protected GenericAgent createAgent(String name, AgentProducer producer, AgentConsumer consumer) {
        return Objects.nonNull(producer) && Objects.nonNull(consumer)
                ? ProcessAgentBuilder.Builder(executorForAgents)
                .setProducer(producer).setConsumer(consumer).build() : null;
    }

    @Override
    public Map<RoboUnitCommand, Function<ProcessAgent, AgentStatus>> initLogic() {
        return null;
    }

    @Override
    public boolean isActive() {
        return active.get();
    }


    @Override
    public RpiUnit init(Object input) {
        if (Objects.nonNull(executorForAgents)) {
            this.agents = new ArrayList<>();
            this.active = new AtomicBoolean(false);
            this.commandQueue = new LinkedBlockingQueue<>();
            SimpleLoggingUtil.print(getClass(), "LcdRpi: INIT");
            final Exchanger<GenericCommand<AdafruitLcdCommandEnum>> lcdExchanger = new Exchanger<>();

            this.agents.add(createAgent("lcdPlatformAgent", new ClientLcdPlatformProducer(commandQueue, lcdExchanger),
                    new ClientLcdPlatformConsumer(executorForAgents, lcdExchanger)));

            if (!agents.isEmpty()) {
                active.set(true);
                logic = initLogic();
            }
        }

        return this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public boolean process(RoboUnitCommand command) {
        try {
            GenericCommand<AdafruitLcdCommandEnum> processCommand = (GenericCommand<AdafruitLcdCommandEnum>) command;
            SimpleLoggingUtil.debug(getClass(), "LcdPlatform Command: " + command);
            commandQueue.put(processCommand);
            ProcessAgent platformAgent = (ProcessAgent) agents.get(AGENT_PLATFORM_POSITION);
            platformAgent.setActive(true);
            platformAgent.getExecutor().execute((Runnable) platformAgent.getProducer());
            final Future<Boolean> engineActive = platformAgent.getExecutor()
                    .submit((Callable<Boolean>) platformAgent.getConsumer());
            try {
                platformAgent.setActive(engineActive.get());
            } catch (InterruptedException | ConcurrentModificationException | ExecutionException e) {
                throw new ClientPlatformException("SOMETHING ERROR CYCLE COMMAND= ", e);
            }
            return true;

        } catch (InterruptedException e) {
            throw new ClientPlatformException("PLATFORM COMMAND e= ", e);
        }
    }

    @Override
    public String getUnitName() {
        return UNIT_NAME;
    }

    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String[] getProducerName() {
        return new String[] { PRODUCER_NAME };
    }

    @Override
    public String getConsumerName() {
        return Arrays.asList(CONSUMER_NAME).toString();
    }

}
