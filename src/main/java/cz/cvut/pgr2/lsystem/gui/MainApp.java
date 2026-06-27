package cz.cvut.pgr2.lsystem.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainWindow().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
