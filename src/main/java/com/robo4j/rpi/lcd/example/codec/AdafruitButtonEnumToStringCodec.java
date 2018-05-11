/*
 * Copyright (c) 2014, 2018, Marcus Hirt, Miroslav Wengner
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

package com.robo4j.rpi.lcd.example.codec;


import com.robo4j.socket.http.codec.StringCodec;
import com.robo4j.socket.http.codec.StringMessage;
import com.robo4j.socket.http.units.HttpProducer;
import com.robo4j.socket.http.units.SocketDecoder;
import com.robo4j.socket.http.units.SocketEncoder;
import com.robo4j.units.rpi.lcd.AdafruitButtonEnum;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
@HttpProducer
public class AdafruitButtonEnumToStringCodec
        implements SocketDecoder<String, AdafruitButtonEnum>, SocketEncoder<AdafruitButtonEnum, String> {
    private final StringCodec codec = new StringCodec();

    @Override
    public AdafruitButtonEnum decode(String json) {
        final StringMessage message = codec.decode(json);
        return AdafruitButtonEnum.getByText(message.getMessage());
    }

    @Override
    public Class<AdafruitButtonEnum> getDecodedClass() {
        return AdafruitButtonEnum.class;
    }

    @Override
    public String encode(AdafruitButtonEnum adafruitButtonEnum) {
        final StringMessage message = new StringMessage(adafruitButtonEnum.getText());
        return codec.encode(message);
    }

    @Override
    public Class<AdafruitButtonEnum> getEncodedClass() {
        return AdafruitButtonEnum.class;
    }
}
