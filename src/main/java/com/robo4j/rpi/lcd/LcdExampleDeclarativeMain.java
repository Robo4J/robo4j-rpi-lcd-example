/*
 * Copyright (c) 2014, 2017, Miroslav Wengner, Marcus Hirt
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
package com.robo4j.rpi.lcd;

import java.io.IOException;

import com.robo4j.commons.io.RoboContext;
import com.robo4j.core.RoboBuilderException;
import com.robo4j.core.system.RoboBuilder;

/**
 * Variant initialized from XML. Is the same as {@link LcdExampleMain}.
 * 
 * @author Marcus Hirt (@hirt)
 */
public class LcdExampleDeclarativeMain {
	public static void main(String[] args) throws RoboBuilderException, IOException {
		RoboBuilder builder = new RoboBuilder().add(LcdExampleDeclarativeMain.class.getResourceAsStream("robo4j.xml"));
		RoboContext ctx = builder.build();
		ctx.start();
		ctx.sendMessage("lcd", "Robo4J: Welcome!\nPress Up/Down!");

		System.out.println("Press enter to quit!");
		System.in.read();
	}

}
