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

package com.robo4j.rpi.lcd.example.controller;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.LifecycleState;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboResult;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.client.util.RoboHttpUtils;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.rpi.lcd.example.demos.AbstractDemo;
import com.robo4j.units.rpi.lcd.AdafruitButtonPlateEnum;
import com.robo4j.units.rpi.lcd.LcdMessage;
import com.robo4j.units.rpi.lcd.LcdMessageType;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class LcdCommandExampleController extends RoboUnit<Object> {

    private String target;
    private String targetOut;
    private String client;
    private String clientPath;

    public LcdCommandExampleController(RoboContext context, String id) {
        super(context, id);
    }

    @Override
    public void onInitialization(Configuration configuration) throws ConfigurationException {
        target = configuration.getString("target", null);
        targetOut = configuration.getString("target_out", null);
        String tmpClient = configuration.getString("client", null);

        if (target == null || tmpClient == null || targetOut == null) {
            throw ConfigurationException.createMissingConfigNameException("target, client");
        }

        String clientPort = configuration.getString("client_port", null);
        client = clientPort == null ? tmpClient : tmpClient.concat(":").concat(clientPort);
        clientPath = configuration.getString("client_path", "?");


    }

    @SuppressWarnings("unchecked")
    @Override
    public RoboResult<String, ?> onMessage(Object message) {

        if (message instanceof AdafruitButtonPlateEnum) {
            AdafruitButtonPlateEnum myMessage = (AdafruitButtonPlateEnum) message;
            processAdaruitMessage(myMessage);
        }
        return null;
    }

    @Override
    public void stop() {
        System.out.println("clear and shutdown...");
        setState(LifecycleState.STOPPING);
        sendLcdMessage(getContext(), AbstractDemo.CLEAR);
        sendLcdMessage(getContext(), AbstractDemo.STOP);
        setState(LifecycleState.STOPPED);

    }

    public void shutdown() {
        setState(LifecycleState.SHUTTING_DOWN);
        setState(LifecycleState.SHUTDOWN);
        System.exit(0);
        System.out.println("shutting off LcdExample...");
    }

    // Private Methods
    private void sendLcdMessage(RoboContext ctx, LcdMessage message) {
        ctx.getReference(target).sendMessage(message);
    }

    private void sendClientMessage(RoboContext ctx, String message) {
        ctx.getReference(targetOut).sendMessage(message);
    }


    private void processAdaruitMessage(AdafruitButtonPlateEnum myMessage) {
        switch (myMessage) {
            case RIGHT:
                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                sendLcdMessage(getContext(), new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Right\nturn!"));
                sendClientMessage(getContext(), RoboHttpUtils.createGetRequest(client, clientPath.concat("command=right")));
                break;
            case LEFT:
                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                sendLcdMessage(getContext(), new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Left\nturn!"));
                sendClientMessage(getContext(), RoboHttpUtils.createGetRequest(client, clientPath.concat("command=left")));
                break;
            case UP:
                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                sendLcdMessage(getContext(), new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Move\nforward!"));
                sendClientMessage(getContext(), RoboHttpUtils.createGetRequest(client, clientPath.concat("command=move")));
                break;
            case DOWN:
                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                sendLcdMessage(getContext(), new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Back\nmove!"));
                sendClientMessage(getContext(), RoboHttpUtils.createGetRequest(client, clientPath.concat("command=back")));
                break;
            case SELECT:
                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                sendLcdMessage(getContext(), new LcdMessage(LcdMessageType.SET_TEXT, null, null, "STOP\nno move!"));
                sendClientMessage(getContext(), RoboHttpUtils.createGetRequest(client, clientPath.concat("command=stop")));
                break;
            default:
//                sendLcdMessage(getContext(), AbstractDemo.CLEAR);
                break;
        }
    }

}
