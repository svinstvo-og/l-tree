package cz.cvut.pgr2.lsystem.engine;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LSystemEngineTest {

    private final LSystemEngine engine = new LSystemEngine();

    @Test
    void identity_noRules_axiomUnchanged() {
        assertEquals("ABC", engine.rewrite("ABC", Map.of(), 3));
    }

    @Test
    void singleRule_depth1() {
        assertEquals("AB", engine.rewrite("A", Map.of('A', "AB"), 1));
    }

    @Test
    void singleRule_depth2() {
        // A -> AB; depth 2: A -> AB -> ABB
        assertEquals("ABB", engine.rewrite("A", Map.of('A', "AB"), 2));
    }

    @Test
    void dragonCurve_depth1() {
        // FX, X->X+YF+, Y->-FX-Y, depth 1
        assertEquals("FX+YF+", engine.rewrite("FX", Map.of('X', "X+YF+", 'Y', "-FX-Y"), 1));
    }

    @Test
    void kochSnowflake_depth1() {
        // F++F++F, F->F-F++F-F, depth 1
        String expected = "F-F++F-F++F-F++F-F++F-F++F-F";
        assertEquals(expected, engine.rewrite("F++F++F", Map.of('F', "F-F++F-F"), 1));
    }

    @Test
    void sierpinski_depth1() {
        // A, A->B-A-B, B->A+B+A, depth 1
        assertEquals("B-A-B", engine.rewrite("A", Map.of('A', "B-A-B", 'B', "A+B+A"), 1));
    }

    @Test
    void sierpinski_depth2() {
        // depth 1: B-A-B
        // depth 2: A+B+A - B-A-B - A+B+A
        assertEquals("A+B+A-B-A-B-A+B+A",
                engine.rewrite("A", Map.of('A', "B-A-B", 'B', "A+B+A"), 2));
    }
}
