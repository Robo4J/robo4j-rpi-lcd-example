/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This DisplayDemo.java  is part of robo4j.
 * module: robo4j-hw-rpi
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
package com.robo4j.rpi.lcd.examples;

import com.robo4j.core.RoboContext;
import com.robo4j.rpi.lcd.LcdMessage;
import com.robo4j.rpi.lcd.LcdMessageType;

/**
 * Simply turns off and on the display a few times.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public abstract class AbstractDemo implements LcdDemo {
	public static LcdMessage CLEAR = new LcdMessage(LcdMessageType.CLEAR, null, null, null);
	public static LcdMessage STOP = new LcdMessage(LcdMessageType.STOP, null, null, null);
	public static LcdMessage TURN_ON = new LcdMessage(LcdMessageType.DISPLAY_ENABLE, null, null, "true");
	public static LcdMessage TURN_OFF = new LcdMessage(LcdMessageType.DISPLAY_ENABLE, null, null, "false");

	protected void sendLcdMessage(RoboContext ctx, LcdMessage message) {
		ctx.getRoboUnit("controller").sendMessage("lcd", message);
	}
	
	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
