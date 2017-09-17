/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This LcdExampleDeclarativeMain.java  is part of robo4j.
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
package com.robo4j.rpi.lcd.example;

import java.io.IOException;

import com.robo4j.core.RoboBuilder;
import com.robo4j.core.RoboBuilderException;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboReference;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.units.rpi.lcd.LcdMessage;

/**
 * Variant initialized from XML. Is the same as {@link LcdExampleMain}.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
public class LcdExampleDeclarativeMain {
	static final String INIT_MESSAGE = "Robo4J: Welcome!\nPress Up/Down!";

	public static void main(String[] args) throws RoboBuilderException, IOException {
		RoboBuilder builder = new RoboBuilder().add(LcdExampleDeclarativeMain.class.getClassLoader().getResourceAsStream("robo4j.xml"));
		RoboContext ctx = builder.build();

		System.out.println("State before start:");
		System.out.println(SystemUtil.printStateReport(ctx));
		ctx.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.printStateReport(ctx));

		ctx.getReference("lcd").sendMessage(new LcdMessage(INIT_MESSAGE));

		final RoboReference<?> httpRef = ctx.getReference("http");
		final RoboReference<?> ctrlRef = ctx.getReference("controller");
		System.out.println(SystemUtil.printSocketEndPoint(httpRef, ctrlRef));

		System.out.println("Press enter to quit!");
		System.in.read();
		ctx.shutdown();
	}

}

