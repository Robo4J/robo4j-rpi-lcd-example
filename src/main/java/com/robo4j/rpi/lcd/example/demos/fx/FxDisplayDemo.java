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

package com.robo4j.rpi.lcd.example.demos.fx;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxDisplayDemo implements FxLcdDemo {

	private final static int REPEATING = 10;
	private static final String[] COLORS = new String[] { "snow", "blue" };
	private static final int DELAY = 500;

	private final String name = "Display";
	private TextArea textArea;
	private String text;
	private Task<Void> task;
	private AtomicBoolean active;

	public FxDisplayDemo() {
		text = "Turning off/on\ndisplay 10 times!";
	}

	@Override
	public void initiate(AtomicBoolean active, TextArea textArea) {
		this.active = active;
		this.textArea = textArea;

		this.task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (int i = 0; i < REPEATING; i++) {
					textArea.setStyle(FxDemoUtil.getCssBackground(COLORS[i % 2]));
					updateMessage(text);
					Thread.sleep(DELAY);
				}
				updateMessage(FxDemoUtil.doneMessage(name));
				textArea.setStyle(FxDemoUtil.getCssBackground(COLORS[0]));
				TimeUnit.MILLISECONDS.sleep(FxDemoUtil.UNBIND_DELAY);
				textArea.textProperty().unbind();
				active.set(false);
				return null;
			}
		};
		textArea.textProperty().bind(task.messageProperty());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Task<Void> getTask() {
		return task;
	}
}
