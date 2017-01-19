/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This ClientLcdProducer.java  is part of robo4j.
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

import com.robo4j.commons.agent.AgentProducer;
import com.robo4j.commons.command.AdafruitLcdCommandEnum;
import com.robo4j.commons.command.GenericCommand;
import com.robo4j.commons.concurrent.CoreBusQueue;
import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.core.platform.ClientPlatformException;

import java.util.concurrent.Exchanger;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 15.01.2017
 */
public class ClientLcdPlatformProducer implements AgentProducer, Runnable{

    private LinkedBlockingQueue<GenericCommand<AdafruitLcdCommandEnum>> commandQueue;
    private Exchanger<GenericCommand<AdafruitLcdCommandEnum>> exchanger;

    public ClientLcdPlatformProducer(final LinkedBlockingQueue<GenericCommand<AdafruitLcdCommandEnum>> commandQueue,
                                  final Exchanger<GenericCommand<AdafruitLcdCommandEnum>> exchanger) {
        this.commandQueue = commandQueue;
        this.exchanger = exchanger;
        SimpleLoggingUtil.debug(getClass(), "INIT");
    }

    @Override
    public CoreBusQueue getMessageQueue() {
        return null;
    }

    @Override
    public void run() {
        GenericCommand<AdafruitLcdCommandEnum> command = null;
        try {
            command = commandQueue.take();
            SimpleLoggingUtil.debug(getClass(), "MAIN Producer command: " + command);
            exchanger.exchange(command);
        } catch (InterruptedException e) {
            throw new ClientPlatformException("LcdPlatform PRODUCER e", e);
        } finally {
            SimpleLoggingUtil.print(getClass(), "ClientLcdPlatformProducer exchanged= " + command);
        }
    }

}
