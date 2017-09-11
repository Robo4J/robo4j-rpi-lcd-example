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

package com.robo4j.rpi.lcd.example.controller;

import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboUnit;
import com.robo4j.rpi.lcd.example.FxLcdController;
import com.robo4j.units.rpi.lcd.AdafruitButtonEnum;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class LcdFxExampleController extends RoboUnit<AdafruitButtonEnum> {

    private FxLcdController controller;

    public LcdFxExampleController(RoboContext context, String id) {
        super(AdafruitButtonEnum.class, context, id);
    }

    public void setController(FxLcdController controller){
        this.controller = controller;
    }

    @Override
    public void onMessage(AdafruitButtonEnum message) {

        switch (message){
            case UP:
                controller.up();
                break;
            case DOWN:
                controller.down();
                break;
            case LEFT:
                controller.left();
                break;
            case RIGHT:
                controller.right();
                break;
            case SELECT:
                controller.select();
                break;


        }
    }
}
