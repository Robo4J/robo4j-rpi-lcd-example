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

package com.robo4j.rpi.lcd.example;

import com.robo4j.core.RoboContext;
import com.robo4j.hw.rpi.i2c.adafruitlcd.impl.RealLcd.Direction;

import com.robo4j.rpi.lcd.example.demos.FxColorDemo;
import com.robo4j.rpi.lcd.example.demos.FxLcdDemo;
import com.robo4j.rpi.lcd.example.demos.FxScrollDemo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxLcdController {

	private static final int DDRAM_SIZE = 40;
	private volatile int cursorColumn;
	private volatile int cursorRow;
	private volatile int maskVal;
	private final char[] FIRST_ROW = new char[DDRAM_SIZE];
	private final char[] SECOND_ROW = new char[DDRAM_SIZE];
	private RoboContext system;
	private String text;
	private int currentScroll;
    FxLcdDemo[] FX_DEMOS = new FxLcdDemo[]{new FxScrollDemo()};

	@FXML
	private TextArea lcdTA;

	@FXML
    private Label colorLabel;

	private Task<Void> task;

	public void init(RoboContext system) {
		this.system = system;
	}

	@FXML
	private void selectButtonAction(ActionEvent event) {
		text = "Bouncing this scroller once.";

//        FxScrollDemo demo = new FxScrollDemo();
//        demo.initiate(lcdTA, "Bouncing this scroller once.");
//

        FxColorDemo demo = new FxColorDemo();
        demo.initiate(lcdTA, "COLOR demo");
        demo.setColorLabel(colorLabel);

        System.out.println("Start Thread: " + demo.getName());

		system.getScheduler().execute(demo.getTask());
	}

	@FXML
	public void initialize() {
		System.out.println("Initialize");
	}

	public void setText(String s) {
		String[] str = s.split("\n");
		for (int i = 0; i < str.length; i++) {
			setText(i, str[i]);
		}
	}

	public void setText(int row, String text) {
		setCursorPosition(row, 0);
		internalWrite(text);
	}

	public void setCursorPosition(int row, int column) {
		cursorRow = row;
		cursorColumn = column;
	}

	// Private Methods
	private void internalWrite(String s) {
		char[] buffer = cursorRow == 0 ? FIRST_ROW : SECOND_ROW;
		for (int i = 0; i < s.length() && cursorColumn < DDRAM_SIZE; i++) {
			buffer[cursorColumn++] = s.charAt(i);
		}
		text = s;
		lcdTA.setText(createStringFromBuffers());
	}

	private String createStringFromBuffers() {
		return String.format("%16s\n%16s", new String(FIRST_ROW), new String(SECOND_ROW));
	}

}
