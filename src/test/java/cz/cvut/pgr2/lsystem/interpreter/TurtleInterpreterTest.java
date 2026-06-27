package cz.cvut.pgr2.lsystem.interpreter;

import cz.cvut.pgr2.lsystem.model.LineSegment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurtleInterpreterTest {

    private static final double EPS = 1e-9;
    private final TurtleInterpreter interpreter = new TurtleInterpreter();

    @Test
    void singleForward_producesOneSegment() {
        var result = interpreter.interpret("F", 10.0, 90.0);
        assertEquals(1, result.segments().size());
    }

    @Test
    void singleForward_correctEndpoint() {
        // starting angle = -90 (facing up), step 10 -> dy = -10 in screen coords
        var result = interpreter.interpret("F", 10.0, 90.0);
        LineSegment seg = result.segments().get(0);
        assertEquals(0.0, seg.x1(), EPS);
        assertEquals(0.0, seg.y1(), EPS);
        assertEquals(0.0, seg.x2(), EPS);
        assertEquals(-10.0, seg.y2(), EPS);
    }

    @Test
    void turnPlusForward_rightAnglePath() {
        // F+F at 90°: first F goes up (dy=-10), then turn left 90°, second F goes left (dx=-10)
        var result = interpreter.interpret("F+F", 10.0, 90.0);
        assertEquals(2, result.segments().size());
        LineSegment s2 = result.segments().get(1);
        assertEquals(0.0, s2.x1(), EPS);
        assertEquals(-10.0, s2.y1(), EPS);
        assertEquals(-10.0, s2.x2(), EPS);
        assertEquals(-10.0, s2.y2(), EPS);
    }

    @Test
    void pushPop_turtleReturnsToBranchPoint() {
        // F[+F]F: move, branch (turn left + move), return to branch point, move again
        var result = interpreter.interpret("F[+F]F", 10.0, 90.0);
        assertEquals(3, result.segments().size());
        // third segment starts where first segment ended
        LineSegment first = result.segments().get(0);
        LineSegment third = result.segments().get(2);
        assertEquals(first.x2(), third.x1(), EPS);
        assertEquals(first.y2(), third.y1(), EPS);
    }

    @Test
    void pipeSymbol_netZeroDisplacement() {
        // F|F: move forward, turn 180, move forward — should end at start
        var result = interpreter.interpret("F|F", 10.0, 90.0);
        assertEquals(2, result.segments().size());
        LineSegment first = result.segments().get(0);
        LineSegment second = result.segments().get(1);
        assertEquals(first.x1(), second.x2(), EPS);
        assertEquals(first.y1(), second.y2(), EPS);
    }

    @Test
    void smallF_movesWithoutDrawing() {
        var result = interpreter.interpret("fF", 10.0, 90.0);
        // 'f' moves without drawing, 'F' draws
        assertEquals(1, result.segments().size());
        LineSegment seg = result.segments().get(0);
        // segment starts at (0, -10) — after f moved turtle there
        assertEquals(0.0, seg.x1(), EPS);
        assertEquals(-10.0, seg.y1(), EPS);
    }

    @Test
    void boundingBox_sanityCheck() {
        var result = interpreter.interpret("F", 10.0, 90.0);
        assertTrue(result.maxY() >= result.minY());
        assertTrue(result.maxX() >= result.minX());
    }

    @Test
    void emptyString_noSegments() {
        var result = interpreter.interpret("", 10.0, 90.0);
        assertTrue(result.segments().isEmpty());
    }

    @Test
    void nonDrawingSymbols_noSegments() {
        var result = interpreter.interpret("ABXY+-", 10.0, 90.0);
        // A, B, X, Y are no-ops; +- rotate but produce no segments
        assertEquals(0, result.segments().size());
    }
}
