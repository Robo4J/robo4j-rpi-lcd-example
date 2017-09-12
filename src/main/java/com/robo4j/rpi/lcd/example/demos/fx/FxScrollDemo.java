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

import com.robo4j.hw.rpi.i2c.adafruitlcd.impl.RealLcd.Direction;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxScrollDemo implements FxLcdDemo {

	private static final int DELAY = 200;
	private final String name = "Scroller";
	private TextArea textArea;
	private String text;
	private Task<Void> task;
	private int currentScroll;
	private AtomicBoolean active;

	public FxScrollDemo() {
		text = "Bouncing this scroller once.";
	}

	@Override
	public void initiate(AtomicBoolean active, TextArea textArea) {
		this.active = active;
		this.textArea = textArea;
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				updateMessage(text);
				for (int i = 0; i < 24; i++) {
					TimeUnit.MILLISECONDS.sleep(DELAY);
					String m = scrollDisplay(Direction.LEFT);
					updateMessage(m);
				}
				for (int i = 0; i < 24; i++) {
					TimeUnit.MILLISECONDS.sleep(DELAY);
					String m = scrollDisplay(Direction.RIGHT);
					updateMessage(m);
				}
				updateMessage(FxDemoUtil.doneMessage(name));
				TimeUnit.MILLISECONDS.sleep(DELAY);
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

	private String scrollDisplay(Direction direction) {
		String data = text;
		if (direction == Direction.LEFT) {
			currentScroll--;
		} else {
			currentScroll++;
		}
		return scroll(data, currentScroll);
	}

	private String scroll(String data, int currentScroll) {
		for (int i = 0; i < Math.abs(currentScroll); i++) {
			if (currentScroll < 0) {
				data = goLeft(data);
			} else {
				data = goRight(data);
			}
		}
		return data;
	}

	private static String goLeft(String data) {
		data = data.substring(1);
		int nlIndex = data.indexOf("\n");
		if (nlIndex == -1) {
			return data;
		}
		return data.substring(0, nlIndex) + data.substring(nlIndex + 2);
	}

	private static String goRight(String data) {
		data = " " + data;
		int nlIndex = data.indexOf("\n");
		if (nlIndex == -1) {
			return data;
		}
		return data.substring(0, nlIndex) + " " + data.substring(nlIndex + 1);
	}
}
