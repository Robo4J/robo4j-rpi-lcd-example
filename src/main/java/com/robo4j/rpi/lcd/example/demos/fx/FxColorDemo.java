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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxColorDemo implements FxLcdDemo {

	private static final int DELAY = 1;
	private static final String[] COLORS = new String[] { "red", "blue", "green", "yellow", "snow" };
	private final String name = "Color";
	private TextArea textArea;
	private String text;
	private Task<Void> task;
	private AtomicBoolean active;

	public FxColorDemo() {
		text = "Changed color";
	}

	@Override
	public void initiate(AtomicBoolean active, TextArea textArea) {
		this.active = active;
		this.textArea = textArea;
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (int i = 0; i < COLORS.length; i++) {
					textArea.setStyle(FxDemoUtil.getCssBackground(COLORS[i]));
					updateMessage(text + ": " + COLORS[i]);
					TimeUnit.SECONDS.sleep(DELAY);
				}
				updateMessage(FxDemoUtil.doneMessage(name));
				TimeUnit.SECONDS.sleep(200);
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
