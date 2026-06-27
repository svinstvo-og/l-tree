package cz.cvut.pgr2.lsystem.gui;

import cz.cvut.pgr2.lsystem.interpreter.TurtleInterpreter.InterpretResult;
import cz.cvut.pgr2.lsystem.renderer.Renderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasPanel extends Pane {

    private final Canvas canvas = new Canvas();
    private InterpretResult lastResult;
    private Color strokeColor = Color.BLACK;

    public CanvasPanel() {
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> repaint());
        canvas.heightProperty().addListener((obs, o, n) -> repaint());
    }

    public void display(InterpretResult result) {
        lastResult = result;
        repaint();
    }

    public void setStrokeColor(Color color) {
        this.strokeColor = color;
        repaint();
    }

    private void repaint() {
        if (lastResult == null) return;
        Renderer.render(canvas.getGraphicsContext2D(), lastResult,
                canvas.getWidth(), canvas.getHeight(), strokeColor);
    }
}
