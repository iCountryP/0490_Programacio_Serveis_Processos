package com.project;

import com.clientFX.UtilsViews;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static String nombreJugador;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        UtilsViews.addView(getClass(), "Connexio", "/assets/vista_connexio.fxml");
        UtilsViews.addView(getClass(), "Joc", "/assets/vista_joc.fxml");
        UtilsViews.addView(getClass(), "Classificacio", "/assets/vista_classificacio.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);
        stage.setTitle("Sudoku!");
        stage.setScene(scene);
        stage.show();

    }

}