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

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

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

    public FxDisplayDemo() {
        text = "Turning off/on\ndisplay 10 times!";
    }

    @Override
	public void initiate(TextArea textArea) {
		this.textArea = textArea;

		this.task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (int i = 0; i < REPEATING; i++) {
					textArea.setStyle(DemoUtil.getCssBackground(COLORS[i % 2]));
					updateMessage(text);
                    Thread.sleep(DELAY);
                }
                updateMessage(DemoUtil.doneMessage(name));
                textArea.setStyle(DemoUtil.getCssBackground(COLORS[0]));
                textArea.textProperty().unbind();
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
