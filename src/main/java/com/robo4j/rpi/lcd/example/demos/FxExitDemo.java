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

import java.util.concurrent.TimeUnit;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxExitDemo implements FxLcdDemo {

    private static final int DELAY = 2;
    private final String name = "Exit";
    private TextArea textArea;
    private String text;
    private Task<Void> task;

    public FxExitDemo() {
        text = "exit...";
    }

    @Override
    public void initiate(TextArea textArea) {
        this.textArea = textArea;

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage(text);
                TimeUnit.SECONDS.sleep(DELAY);
                textArea.textProperty().unbind();
                System.exit(0);
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
