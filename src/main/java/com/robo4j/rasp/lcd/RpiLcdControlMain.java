/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This RpiLcdControlExample.java  is part of robo4j.
 * module: robo4j-rpi-lcd-example
 *
 * robo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * robo4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robo4j.rasp.lcd;

import com.robo4j.commons.logging.SimpleLoggingUtil;
import com.robo4j.core.Robo4jBrick;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 15.01.2017
 */
public class RpiLcdControlMain {

    public static void main(String[] args) {
        SimpleLoggingUtil.debug(RpiLcdControlMain.class, "LCD CONTROLLER START");
        new RpiLcdControlMain();
    }

    @SuppressWarnings(value = "unchecked")
    public RpiLcdControlMain() {
        SimpleLoggingUtil.print(getClass(), "SERVER starts...");
        boolean test = false;
        Robo4jBrick robo4jBrick = new Robo4jBrick(getClass(), test);
        robo4jBrick.init();
        while(robo4jBrick.isActive()){

        }
        robo4jBrick.end();
        SimpleLoggingUtil.print(getClass(), "FINAL END");
        System.exit(0);
    }

}
