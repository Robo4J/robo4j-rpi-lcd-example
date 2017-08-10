/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This ExitTest.java  is part of robo4j.
 * module: robo4j-hw-rpi
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

/**
 * This one really doesn't anything but clean up and exit.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class ExitDemo extends AbstractDemo {

	@Override
	public String getName() {
		return "<Exit>";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.robo4j.rasp.lcd.examples.LCDTest#run(com.robo4j.core.RoboContext)
	 */
	@Override
	public void runDemo() throws IOException {
		ctx.shutdown();
	}

}
