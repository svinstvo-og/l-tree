package cz.cvut.pgr2.lsystem.model;

import java.util.Map;

public record LSystemPreset(
        String name,
        String axiom,
        Map<Character, String> rules,
        double angle,
        double stepLength,
        int defaultDepth
) {}
