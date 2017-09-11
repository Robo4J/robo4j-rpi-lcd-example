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

import java.net.URL;

import com.robo4j.core.RoboBuilder;
import com.robo4j.core.RoboContext;

import com.robo4j.rpi.lcd.example.controller.LcdFxExampleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFx Example for Adafruit 16x2 LCD Kit for RaspberryPi
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class FxLcdExample extends Application {

	private static final String ROBO4J_CENTER_FXML = "rpiLcdKit.fxml";
	private RoboContext roboSystem;

	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL file = Thread.currentThread().getContextClassLoader().getResource(ROBO4J_CENTER_FXML);
		FXMLLoader fxmlLoader = new FXMLLoader(file);
		Pane myPane = fxmlLoader.load();
        FxLcdController controller = fxmlLoader.getController();

		RoboBuilder builder = new RoboBuilder();
		builder.add(Thread.currentThread().getContextClassLoader().getResourceAsStream("robo4jFx.xml"));
		LcdFxExampleController fxController = new LcdFxExampleController(builder.getContext(), "controller");
		fxController.setController(controller);
		builder.add(fxController);

		roboSystem = builder.build();
		roboSystem.start();

		controller.init(roboSystem);

		stage.setScene(new Scene(myPane, 800, 526));
		myPane.setStyle("-fx-border-color:black");
		initializeStage(stage);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		roboSystem.shutdown();
	}

	// Private Methods
	private void initializeStage(Stage stage) {
		stage.setTitle("Robo4J LcdKit");
		stage.getIcons().add(createIcon("robo4j256.png"));
		stage.getIcons().add(createIcon("robo4j128.png"));
		stage.getIcons().add(createIcon("robo4j64.png"));
		stage.getIcons().add(createIcon("robo4j32.png"));
		stage.getIcons().add(createIcon("robo4j16.png"));
	}

	private Image createIcon(String iconName) {
		return new Image(getClass().getClassLoader().getResourceAsStream(iconName));
	}
}
