/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This SocketHttpProducer.java  is part of robo4j.
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
import java.util.function.Function;

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

/**
 * @author Miro Wengner (@miragemiko)
 * @since 18.01.2017
 */

@RoboUnitProducer(id = SocketHttpProducer.ID)
public class SocketHttpProducer extends DefaultUnit<UnitProducer> implements UnitProducer {

    public final static String ID = "socket_http";

    private String name;

    public SocketHttpProducer() {

        this.name = ID;
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
}
