/*
 * Copyright (C) 2016-2017. Miroslav Wengner, Marcus Hirt
 * This GeneralLcdProducer.java  is part of robo4j.
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

package com.robo4j.rpi.lcd.producers;

import com.robo4j.commons.unit.GenericUnit;
import com.robo4j.hw.rpi.i2c.adafruitlcd.AdafruitLcd;

/**
 * @author Miro Wengner (@miragemiko)
 * @since 19.01.2017
 */
public interface GeneralLcdProducer {

    void setParentUnit(GenericUnit genericUnit);

    void setLcdPlate(AdafruitLcd lcd);

}
