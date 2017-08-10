/*
 * Copyright (C) 2016, 2017. Miroslav Wengner, Marcus Hirt
 * This ScrollDemo.java  is part of robo4j.
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
import com.robo4j.core.scheduler.Scheduler;
import com.robo4j.units.rpi.lcd.LcdMessage;
import com.robo4j.units.rpi.lcd.LcdMessageType;

/**
 * Scrolls the view area back and forth a few times. Check out the documentation
 * for the HD44780 for more info on how the tiny (DDRAM) buffer is handled.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class ScrollDemo extends AbstractDemo {

	@Override
	public String getName() {
		return "Scroller";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.robo4j.rasp.lcd.examples.LCDTest#run(com.robo4j.core.RoboContext)
	 */
	@Override
	public void runDemo() throws IOException {
		final LcdMessage left = new LcdMessage(LcdMessageType.SCROLL, null, null, "left");
		final LcdMessage right = new LcdMessage(LcdMessageType.SCROLL, null, null, "right");
		final Scheduler scheduler = ctx.getScheduler();
		lcd.sendMessage(new LcdMessage("Running scroller. Be patient!\nBouncing this scroller once."));

		scheduler.schedule(lcd, left, 100, 100, TimeUnit.MILLISECONDS, 24, new FinalInvocationListener() {
			@Override
			public void onFinalInvocation(RoboContext context) {
				scheduler.schedule(lcd, right, 100, 100, TimeUnit.MILLISECONDS, 24, new FinalInvocationListener() {
					@Override
					public void onFinalInvocation(RoboContext context) {
						lcd.sendMessage(new LcdMessage("Scroller Demo:  \nDone!           "));
						setDone();
					}
				});
			}
		});
	}
}
