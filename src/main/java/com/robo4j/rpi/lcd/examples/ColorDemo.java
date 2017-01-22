/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This ColorDemo.java  is part of robo4j.
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

import com.robo4j.commons.io.RoboContext;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Color;
import com.robo4j.rpi.lcd.LcdMessage;

/**
 * This demo should cycle through the background colors.
 * 
 * @author Marcus Hirt
 */
public class ColorDemo extends AbstractDemo {

	@Override
	public String getName() {
		return "Backlight";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.robo4j.rasp.lcd.examples.LCDTest#run(com.robo4j.core.RoboContext)
	 */
	@Override
	public void run(RoboContext ctx) throws IOException {
		String prefix = "Color changes:\n";
		sendLcdMessage(ctx, CLEAR);
		for (Color c : Color.values()) {
			sendLcdMessage(ctx, new LcdMessage(prefix + "Color: " + c.toString() + "      ", c));
			sleep(1000);
		}
		sendLcdMessage(ctx, CLEAR);
		sendLcdMessage(ctx, new LcdMessage("Backlight Test:\nDone!", Color.ON));
	}
}
