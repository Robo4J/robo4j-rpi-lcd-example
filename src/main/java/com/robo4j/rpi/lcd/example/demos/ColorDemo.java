/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This ColorDemo.java  is part of robo4j.
 * module: robo4j-rpi-lcd-example
 *
 * robo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * robo4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */
package com.robo4j.rpi.lcd.example.demos;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.robo4j.core.RoboContext;
import com.robo4j.core.scheduler.FinalInvocationListener;
import com.robo4j.hw.rpi.i2c.adafruitlcd.Color;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * This demo should cycle through the background colors.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
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
	public void runDemo() throws IOException {
		String prefix = "Color changes:\n";
		lcd.sendMessage(LcdMessage.MESSAGE_CLEAR);

		int delay = 0;
		int i = 0;
		for (; i < Color.values().length - 1; i++) {
			Color c = Color.values()[i];
			ctx.getScheduler().schedule(lcd, getColorMessage(prefix, c), delay += 1, 1, TimeUnit.SECONDS, 1);
		}
		ctx.getScheduler().schedule(lcd, getColorMessage(prefix, Color.values()[i]), delay += 1, 1, TimeUnit.SECONDS, 1,
				new FinalInvocationListener() {
					@Override
					public void onFinalInvocation(RoboContext context) {
						lcd.sendMessage(new LcdMessage("Backlight Demo: \nDone!           ", Color.ON));
						setDone();
					}
				});
	}

	private LcdMessage getColorMessage(String prefix, Color c) {
		LcdMessage lcdMessage = new LcdMessage(prefix + "Color: " + c.toString() + "      ", c);
		return lcdMessage;
	}
}
