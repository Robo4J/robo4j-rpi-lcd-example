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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxColorDemo implements FxLcdDemo {

    private static final String[] STYLES = new String[] {"text-area-background: white;"
            , "text-area-background: green;", "text-area-background: blue;", "text-area-background: yellow"};
    private final String name = "Color";
    private TextArea textArea;
    private Label colorLabel;
    private String text;
    private Task<Void> task;

    @Override
    public void initiate(TextArea textArea, String text) {
        this.text = text;
        this.textArea = textArea;
        textArea.setStyle(STYLES[1]);

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                colorLabel.setVisible(true);
                for(int i=0; i<STYLES.length; i++){
                    Thread.sleep(500);
                    textArea.setStyle(STYLES[i]);
                    System.out.println("COLOR: " + STYLES[i]);
                    updateMessage(STYLES[i]);
                    Thread.sleep(500);
                }
                colorLabel.setVisible(false);
                updateMessage("DONE!");
                return null;
            }
        };
        textArea.textProperty().bind(task.messageProperty());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setColorLabel(Label colorLabel) {
        this.colorLabel = colorLabel;
    }

    @Override
    public Task<Void> getTask() {
        return task;
    }
}
