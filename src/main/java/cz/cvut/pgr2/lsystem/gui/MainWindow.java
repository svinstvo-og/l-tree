package cz.cvut.pgr2.lsystem.gui;

import cz.cvut.pgr2.lsystem.engine.LSystemEngine;
import cz.cvut.pgr2.lsystem.gui.ControlPanel.LSystemParams;
import cz.cvut.pgr2.lsystem.interpreter.TurtleInterpreter;
import cz.cvut.pgr2.lsystem.interpreter.TurtleInterpreter.InterpretResult;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** Root UI coordinator: wires {@link ControlPanel} and {@link CanvasPanel} and runs generation on a daemon thread. */
public class MainWindow {

    private final LSystemEngine engine = new LSystemEngine();
    private final TurtleInterpreter interpreter = new TurtleInterpreter();

    public void show(Stage stage) {
        ControlPanel controlPanel = new ControlPanel();
        CanvasPanel canvasPanel = new CanvasPanel();

        controlPanel.setOnGenerate(() -> runGeneration(controlPanel, canvasPanel));

        BorderPane root = new BorderPane();
        root.setLeft(controlPanel);
        root.setCenter(canvasPanel);

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("L-System Modelling — POGR2");
        stage.setScene(scene);
        stage.show();

        // render the initial preset on startup
        controlPanel.getGenerateButton().fire();
    }

    private void runGeneration(ControlPanel controlPanel, CanvasPanel canvasPanel) {
        LSystemParams params = controlPanel.readParams();
        controlPanel.getGenerateButton().setDisable(true);

        Task<InterpretResult> task = new Task<>() {
            @Override
            protected InterpretResult call() {
                String lString = engine.rewrite(params.axiom(), params.rules(), params.depth());
                return interpreter.interpret(lString, params.stepLength(), params.angle());
            }
        };

        task.setOnSucceeded(e -> {
            canvasPanel.setLineWidth(params.lineWidth());
            canvasPanel.display(task.getValue());
            controlPanel.getGenerateButton().setDisable(false);
        });

        task.setOnFailed(e -> {
            controlPanel.getGenerateButton().setDisable(false);
            Throwable ex = task.getException();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    ex != null ? ex.getMessage() : "Unknown error");
            alert.show();
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}
