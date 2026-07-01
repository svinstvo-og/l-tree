package cz.cvut.pgr2.lsystem.model;

import java.util.Map;

/**
 * Immutable data bundle describing one L-system configuration.
 *
 * @param name         display name shown in the preset selector
 * @param axiom        starting string for the rewriter
 * @param rules        production rules mapping each symbol to its replacement
 * @param angle        turtle turn angle δ in degrees
 * @param stepLength   forward step length in logical units (auto-scaled on render)
 * @param defaultDepth iteration depth pre-selected when the preset is loaded
 */
public record LSystemPreset(
        String name,
        String axiom,
        Map<Character, String> rules,
        double angle,
        double stepLength,
        int defaultDepth
) {}
