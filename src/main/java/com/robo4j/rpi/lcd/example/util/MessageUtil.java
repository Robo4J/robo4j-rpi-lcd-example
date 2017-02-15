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

package com.robo4j.rpi.lcd.example.util;

import com.robo4j.units.rpi.lcd.LcdMessage;
import com.robo4j.units.rpi.lcd.LcdMessageType;

/**
 * Used messages type
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public final class MessageUtil {
    public static LcdMessage CLEAR = new LcdMessage(LcdMessageType.CLEAR, null, null, null);
    public static LcdMessage STOP = new LcdMessage(LcdMessageType.STOP, null, null, null);
    public static LcdMessage TURN_ON = new LcdMessage(LcdMessageType.DISPLAY_ENABLE, null, null, "true");
    public static LcdMessage TURN_OFF = new LcdMessage(LcdMessageType.DISPLAY_ENABLE, null, null, "false");

}
