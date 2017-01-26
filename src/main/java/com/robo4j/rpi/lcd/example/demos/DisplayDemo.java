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
package com.robo4j.rpi.lcd.example.demos;

import java.io.IOException;

import com.robo4j.core.RoboContext;
import com.robo4j.units.rpi.lcd.LcdMessage;
import com.robo4j.units.rpi.lcd.LcdMessageType;

/**
 * Simply turns off and on the display a few times.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 * @since 18.01.2017
 */
public class DisplayDemo extends AbstractDemo implements LcdDemo {

	@Override
	public String getName() {
		return "Display";
	}

	@Override
	public void run(RoboContext ctx) throws IOException {
		sendLcdMessage(ctx, CLEAR);
		sendLcdMessage(ctx, new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Turning off/on\ndisplay 10 times!"));
		sleep(1000);
		for (int i = 0; i < 10; i++) {
			ctx.getReference("lcd").sendMessage(TURN_OFF);
			sleep(300);
			ctx.getReference("lcd").sendMessage(TURN_ON);
			sleep(300);
		}
		sendLcdMessage(ctx, CLEAR);
		sendLcdMessage(ctx, new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Display Demo:\nDone!           "));
	}
}