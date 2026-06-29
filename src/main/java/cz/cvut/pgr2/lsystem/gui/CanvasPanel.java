package cz.cvut.pgr2.lsystem.gui;

import cz.cvut.pgr2.lsystem.interpreter.TurtleInterpreter.InterpretResult;
import cz.cvut.pgr2.lsystem.renderer.Renderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasPanel extends Pane {

    private final Canvas canvas = new Canvas();
    private InterpretResult lastResult;
    private Color strokeColor = Color.BLACK;

    private double zoom = 1.0;
    private double panX = 0.0;
    private double panY = 0.0;
    private double lastDragX, lastDragY;

    public CanvasPanel() {
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> repaint());
        canvas.heightProperty().addListener((obs, o, n) -> repaint());

        canvas.setOnScroll(e -> {
            double factor = e.getDeltaY() > 0 ? 1.1 : 1.0 / 1.1;
            panX = e.getX() * (1 - factor) + panX * factor;
            panY = e.getY() * (1 - factor) + panY * factor;
            zoom *= factor;
            repaint();
            e.consume();
        });

        canvas.setOnMousePressed(e -> {
            lastDragX = e.getX();
            lastDragY = e.getY();
        });

        canvas.setOnMouseDragged(e -> {
            panX += e.getX() - lastDragX;
            panY += e.getY() - lastDragY;
            lastDragX = e.getX();
            lastDragY = e.getY();
            repaint();
        });

        canvas.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
                resetView();
                repaint();
            }
        });
    }

    public void display(InterpretResult result) {
        resetView();
        lastResult = result;
        repaint();
    }

    public void setStrokeColor(Color color) {
        this.strokeColor = color;
        repaint();
    }

    private void resetView() {
        zoom = 1.0;
        panX = 0.0;
        panY = 0.0;
    }

    private void repaint() {
        if (lastResult == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.save();
        gc.translate(panX, panY);
        gc.scale(zoom, zoom);
        Renderer.render(gc, lastResult, canvas.getWidth(), canvas.getHeight(), strokeColor);
        gc.restore();
    }
}
