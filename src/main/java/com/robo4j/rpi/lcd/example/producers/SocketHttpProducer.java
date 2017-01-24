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

package com.robo4j.rpi.lcd.example.producers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.robo4j.core.agent.AgentConsumer;
import com.robo4j.core.agent.AgentProducer;
import com.robo4j.core.agent.AgentStatus;
import com.robo4j.core.agent.GenericAgent;
import com.robo4j.core.agent.ProcessAgent;
import com.robo4j.core.annotation.RoboUnitProducer;
import com.robo4j.core.command.RoboUnitCommand;
import com.robo4j.core.logging.SimpleLoggingUtil;
import com.robo4j.core.unit.DefaultUnit;
import com.robo4j.core.unit.GenericUnit;
import com.robo4j.core.unit.UnitProducer;
import com.robo4j.core.client.enums.RequestStatusEnum;
import com.robo4j.core.client.request.RequestProcessorCallable;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 * @since 18.01.2017
 */

@RoboUnitProducer(id = SocketHttpProducer.ID)
public class SocketHttpProducer extends DefaultUnit<UnitProducer> implements UnitProducer, GeneralLcdProducer {

    private static final int PORT = 8025;
    public final static String ID = "socket_http";

    private String name;

    public SocketHttpProducer() {
        this.name = ID;
        this.active = new AtomicBoolean(false);
    }

    //TODO : this unit currently doesn't need reference -> will be refactored
    @Override
    public void setParentUnit(GenericUnit genericUnit) {
    }

    public void setLcdPlate(AdafruitLcd tmpLcd){
    }


    @SuppressWarnings("rawtypes")
	@Override
    protected GenericAgent createAgent(String s, AgentProducer agentProducer, AgentConsumer agentConsumer) {
        return null;
    }


    @SuppressWarnings("rawtypes")
	@Override
    public Map<RoboUnitCommand, Function<ProcessAgent, AgentStatus>> initLogic() {
        this.executorForAgents.submit(() -> {
            SimpleLoggingUtil.debug(getClass(), "INIT LOGIC on PORT : " + PORT);
            try (ServerSocket server = new ServerSocket(PORT)) {
                while (active.get()) {
                    Socket request = server.accept();
                    Future<RequestStatusEnum> result = executorForAgents.submit(new RequestProcessorCallable(request));
                    SimpleLoggingUtil.debug(getClass(), "RESULT result: " + result.get());
                    switch (result.get()) {
                        case ACTIVE:
                            break;
                        case NONE:
                            break;
                        case EXIT:
                            SimpleLoggingUtil.debug(getClass(), "IS EXIT: " + result.get());
                            active.set(false);
                            break;
                        default:
                            break;
                    }
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                SimpleLoggingUtil.print(getClass(), "SERVER CLOSED");
            }
        });
        return null;
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
        SimpleLoggingUtil.debug(getClass(), "process command: " + roboUnitCommand);
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
