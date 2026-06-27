package cz.cvut.pgr2.lsystem.presets;

import cz.cvut.pgr2.lsystem.model.LSystemPreset;

import java.util.List;
import java.util.Map;

public class PresetLibrary {

    public static List<LSystemPreset> getAll() {
        return List.of(
            new LSystemPreset(
                "Sierpiński Triangle",
                "A",
                Map.of('A', "B-A-B", 'B', "A+B+A"),
                60.0, 8.0, 6
            ),
            new LSystemPreset(
                "Dragon Curve",
                "FX",
                Map.of('X', "X+YF+", 'Y', "-FX-Y"),
                90.0, 8.0, 12
            ),
            new LSystemPreset(
                "Koch Snowflake",
                "F++F++F",
                Map.of('F', "F-F++F-F"),
                60.0, 5.0, 5
            ),
            new LSystemPreset(
                "Fractal Plant",
                "X",
                Map.of('X', "F+[[X]-X]-F[-FX]+X", 'F', "FF"),
                25.0, 8.0, 6
            ),
            new LSystemPreset(
                "Fractal Tree",
                "F",
                Map.of('F', "F[+F]F[-F]F"),
                25.7, 8.0, 5
            )
        );
    }
}
