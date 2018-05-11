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
package com.robo4j.rpi.lcd.example.demos;

import java.io.IOException;

import com.robo4j.RoboContext;
import com.robo4j.RoboReference;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * Superclass for the demos.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public abstract class AbstractDemo implements LcdDemo {

	protected RoboReference<LcdMessage> lcd;
	protected RoboContext ctx;
	private volatile boolean isRunning;

	public AbstractDemo(RoboContext ctx, RoboReference<LcdMessage> lcd) {
		this.ctx = ctx;
		this.lcd = lcd;
	}

	@Override
	public void run() throws IOException {
		isRunning = true;
		runDemo();
	}

	public boolean isRunning() {
		return isRunning;
	}

	protected abstract void runDemo() throws IOException;

	protected void setDone() {
		isRunning = false;
	}
}
