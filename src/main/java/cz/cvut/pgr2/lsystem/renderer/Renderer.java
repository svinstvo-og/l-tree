package cz.cvut.pgr2.lsystem.renderer;

import cz.cvut.pgr2.lsystem.interpreter.TurtleInterpreter.InterpretResult;
import cz.cvut.pgr2.lsystem.model.LineSegment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    private static final double MARGIN_RATIO = 0.05;

    public static void render(GraphicsContext gc, InterpretResult result,
                               double canvasW, double canvasH, Color strokeColor, double lineWidth) {
        gc.clearRect(0, 0, canvasW, canvasH);

        if (result.segments().isEmpty()) return;

        double bboxW = result.maxX() - result.minX();
        double bboxH = result.maxY() - result.minY();

        double marginX = canvasW * MARGIN_RATIO;
        double marginY = canvasH * MARGIN_RATIO;

        double scale;
        if (bboxW < 1e-9 && bboxH < 1e-9) {
            scale = 1.0;
        } else if (bboxW < 1e-9) {
            scale = (canvasH - 2 * marginY) / bboxH;
        } else if (bboxH < 1e-9) {
            scale = (canvasW - 2 * marginX) / bboxW;
        } else {
            scale = Math.min((canvasW - 2 * marginX) / bboxW,
                             (canvasH - 2 * marginY) / bboxH);
        }

        double offsetX = (canvasW - bboxW * scale) / 2.0 - result.minX() * scale;
        double offsetY = (canvasH - bboxH * scale) / 2.0 - result.minY() * scale;

        gc.setLineWidth(lineWidth);

        for (LineSegment seg : result.segments()) {
            gc.setStroke(colorForDepth(seg.depth(), strokeColor));
            gc.strokeLine(
                seg.x1() * scale + offsetX, seg.y1() * scale + offsetY,
                seg.x2() * scale + offsetX, seg.y2() * scale + offsetY
            );
        }
    }

    private static Color colorForDepth(int depth, Color base) {
        if (base != null) return base;
        return Color.hsb(depth * 30.0 % 360, 0.8, 0.5);
    }
}
