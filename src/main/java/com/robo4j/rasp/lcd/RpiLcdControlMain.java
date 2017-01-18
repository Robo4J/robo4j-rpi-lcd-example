/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This RpiLcdControlExample.java  is part of robo4j.
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

package com.robo4j.rasp.lcd;

import com.robo4j.commons.control.RoboSystemConfig;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.commons.registry.RegistryTypeEnum;
import com.robo4j.commons.registry.RoboRegistry;
import com.robo4j.core.Robo4jBrick;
import com.robo4j.core.client.enums.RequestStatusEnum;
import com.robo4j.core.client.request.RequestProcessorCallable;
import com.robo4j.core.client.request.RequestProcessorFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 15.01.2017
 */
public class RpiLcdControlMain {

    private static final int PORT = 8025;

    public static void main(String[] args) {
        SimpleLoggingUtil.debug(RpiLcdControlMain.class, "LCD CONTROLLER START");
        new RpiLcdControlMain();
    }

    @SuppressWarnings(value = "unchecked")
    public RpiLcdControlMain() {
        SimpleLoggingUtil.print(getClass(), "SERVER starts...");
        boolean test = false;
        Robo4jBrick robo4jBrick = new Robo4jBrick(getClass(), test);
        final RoboRegistry<RoboRegistry, RoboSystemConfig> systemServiceRegistry = robo4jBrick
                .getRegistryByType(RegistryTypeEnum.SERVICES);
        SimpleLoggingUtil.debug(getClass(), "systemServiceRegistry: " + systemServiceRegistry.getRegistry().entrySet()
                .stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        robo4jBrick.activateEngineRegistry();
        final AtomicBoolean active = new AtomicBoolean(true);

        robo4jBrick.submit(new RequestProcessorCallable(null));

        try (ServerSocket server = new ServerSocket(PORT)) {
            final RequestProcessorFactory factory = RequestProcessorFactory.getInstance();
            while (active.get()) {
                Socket request = server.accept();
                Future<RequestStatusEnum> result = robo4jBrick.submit(new RequestProcessorCallable(request));
                SimpleLoggingUtil.debug(getClass(), "RESULT result: " + result);
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

        robo4jBrick.end();
        SimpleLoggingUtil.print(getClass(), "FINAL END");
        System.exit(0);
    }

}
