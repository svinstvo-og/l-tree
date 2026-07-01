package cz.cvut.pgr2.lsystem.interpreter;

import cz.cvut.pgr2.lsystem.model.LineSegment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Converts an L-system string into drawable line segments using turtle graphics. */
public class TurtleInterpreter {

    /** Segments produced by interpretation plus their collective bounding box. */
    public record InterpretResult(
            List<LineSegment> segments,
            double minX, double minY,
            double maxX, double maxY
    ) {}

    private record TurtleState(double x, double y, double angle, int depth) {}

    /**
     * Walks {@code lString} symbol by symbol, executing turtle commands.
     * {@code F} draws, {@code f} moves silently, {@code +/-} turn, {@code [/]} push/pop state, {@code |} reverses heading.
     */
    public InterpretResult interpret(String lString, double stepLength, double angleDeg) {
        List<LineSegment> segments = new ArrayList<>();
        Deque<TurtleState> stack = new ArrayDeque<>();

        double x = 0, y = 0;
        double angle = 90.0; // 90° in math coords = facing screen-up when y is negated
        int depth = 0;

        double minX = 0, maxX = 0, minY = 0, maxY = 0;

        for (int i = 0; i < lString.length(); i++) {
            char c = lString.charAt(i);
            switch (c) {
                case 'F' -> {
                    double rad = Math.toRadians(angle);
                    double nx = x + stepLength * Math.cos(rad);
                    double ny = y - stepLength * Math.sin(rad); // negate: screen y grows downward
                    segments.add(new LineSegment(x, y, nx, ny, depth));
                    x = nx;
                    y = ny;
                    minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                }
                case 'f' -> {
                    double rad = Math.toRadians(angle);
                    x += stepLength * Math.cos(rad);
                    y -= stepLength * Math.sin(rad); // negate: screen y grows downward
                    minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                }
                case '+' -> angle += angleDeg;
                case '-' -> angle -= angleDeg;
                case '[' -> {
                    stack.push(new TurtleState(x, y, angle, depth));
                    depth++;
                }
                case ']' -> {
                    TurtleState s = stack.pop();
                    x = s.x(); y = s.y(); angle = s.angle(); depth = s.depth();
                }
                case '|' -> angle += 180.0;
                default -> { /* no-op for context-free symbols like A, B, X, Y */ }
            }
        }

        // ensure bounding box covers the origin
        minX = Math.min(minX, 0); maxX = Math.max(maxX, 0);
        minY = Math.min(minY, 0); maxY = Math.max(maxY, 0);

        return new InterpretResult(segments, minX, minY, maxX, maxY);
    }
}
