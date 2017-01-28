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
import java.util.concurrent.TimeUnit;

import com.robo4j.core.RoboContext;
import com.robo4j.core.scheduler.FinalInvocationListener;
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
	public void runDemo() throws IOException {		
		lcd.sendMessage(CLEAR);
		lcd.sendMessage(new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Turning off/on\ndisplay 10 times!"));
		
		ctx.getScheduler().schedule(lcd, TURN_OFF, 300, 600, TimeUnit.MILLISECONDS, 10);
		ctx.getScheduler().schedule(lcd, TURN_ON, 600, 600, TimeUnit.MILLISECONDS, 10, new FinalInvocationListener() {			
			@Override
			public void onFinalInvocation(RoboContext context) {
				lcd.sendMessage(CLEAR);
				lcd.sendMessage(new LcdMessage(LcdMessageType.SET_TEXT, null, null, "Display Demo:\nDone!           "));
			}
		});
	}
}
