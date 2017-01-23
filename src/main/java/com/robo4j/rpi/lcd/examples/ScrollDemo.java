/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This ScrollTest.java  is part of robo4j.
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

import java.io.IOException;

import com.robo4j.core.RoboContext;
import com.robo4j.rpi.lcd.LcdMessage;
import com.robo4j.rpi.lcd.LcdMessageType;

/**
 * Scrolls the view area back and forth a few times. Check out the documentation
 * for the HD44780 for more info on how the tiny (DDRAM) buffer is handled.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class ScrollDemo extends  AbstractDemo {

	@Override
	public String getName() {
		return "Scroller";
	}

	/* (non-Javadoc)
	 * @see com.robo4j.rasp.lcd.examples.LCDTest#run(com.robo4j.core.RoboContext)
	 */
	@Override
	public void run(RoboContext ctx) throws IOException {
		LcdMessage left = new LcdMessage(LcdMessageType.SCROLL, null, null, "left");
		LcdMessage right = new LcdMessage(LcdMessageType.SCROLL, null, null, "right");
		
		String message = "Running scroller. Be patient!\nBouncing this scroller once.";
		sendLcdMessage(ctx, new LcdMessage(message));
		for (int i = 0; i < 24; i++) {
			sleep(100);
			sendLcdMessage(ctx, left);
		}
		for (int i = 0; i < 24; i++) {
			sleep(100);
			sendLcdMessage(ctx, right);
		}
		sendLcdMessage(ctx, new LcdMessage("Scroller Demo:  \nDone!             "));
	}
}
