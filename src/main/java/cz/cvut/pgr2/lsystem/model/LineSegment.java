package cz.cvut.pgr2.lsystem.model;

/**
 * A single line drawn by the turtle.
 *
 * @param depth nesting level at the time of drawing — used to vary color by branch depth
 */
public record LineSegment(
        double x1, double y1,
        double x2, double y2,
        int depth
) {}
