# l-tree

A JavaFX desktop app that generates and renders plant-like and fractal structures using [L-systems](https://en.wikipedia.org/wiki/L-system).

## What it does

An L-system rewrites a starting string (axiom) by repeatedly applying production rules in parallel. The resulting string is fed to a turtle graphics interpreter that draws line segments — producing fractals and organic-looking plants from simple rule definitions.

## Architecture

```
LSystemEngine       — pure string rewriter (axiom + rules → expanded string)
TurtleInterpreter   — converts string symbols to line segments
Renderer            — draws segments onto a JavaFX canvas with auto-scale + center
PresetLibrary       — static data: 5 built-in presets
GUI                 — preset selector, editable fields, canvas
```

`LSystemEngine` and `TurtleInterpreter` have no UI dependency and are independently unit-tested.

## Presets

| Name | Type |
|------|------|
| Sierpiński Triangle | Fractal |
| Dragon Curve | Fractal |
| Koch Snowflake | Fractal |
| Fractal Plant | Plant |
| Fractal Tree | Plant |

Adding a new preset is a single data entry — no code changes.

## Running

**Requirements:** Java 26+, Maven 3.8+

```bash
mvn javafx:run
```

Or build a fat JAR and run it:

```bash
mvn package
java -jar target/l-tree-1.0-SNAPSHOT.jar
```

## Controls

- Select a preset from the dropdown
- Adjust axiom, rules, angle, step length, and iteration depth
- Click **Generate** to render
- Use **Reset** to restore preset defaults
- Scroll to zoom, drag to pan
