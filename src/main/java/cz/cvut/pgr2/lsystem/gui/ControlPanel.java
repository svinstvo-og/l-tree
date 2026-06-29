package cz.cvut.pgr2.lsystem.gui;

import cz.cvut.pgr2.lsystem.model.LSystemPreset;
import cz.cvut.pgr2.lsystem.presets.PresetLibrary;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlPanel extends VBox {

    public record LSystemParams(String axiom, Map<Character, String> rules,
                                double angle, double stepLength, int depth) {}

    private final ComboBox<LSystemPreset> presetBox = new ComboBox<>();
    private final TextField axiomField = new TextField();
    private final TextArea rulesArea = new TextArea();
    private final TextField angleField = new TextField();
    private final TextField stepField = new TextField();
    private final Slider depthSlider = new Slider(1, 50, 5);
    private final Label depthLabel = new Label("5");
    private final Button generateButton = new Button("Generate");
    private final Button resetButton = new Button("Reset");

    private Runnable onGenerate;

    public ControlPanel() {
        setPadding(new Insets(12));
        setSpacing(8);
        setPrefWidth(270);

        List<LSystemPreset> presets = PresetLibrary.getAll();
        presetBox.getItems().addAll(presets);
        presetBox.setConverter(new StringConverter<>() {
            @Override public String toString(LSystemPreset p) { return p == null ? "" : p.name(); }
            @Override public LSystemPreset fromString(String s) { return null; }
        });
        presetBox.setMaxWidth(Double.MAX_VALUE);
        presetBox.getSelectionModel().selectedItemProperty().addListener((obs, o, p) -> {
            if (p != null) populateFrom(p);
        });

        rulesArea.setPrefRowCount(6);
        rulesArea.setWrapText(false);
        rulesArea.setStyle("-fx-font-family: monospace;");

        depthSlider.setBlockIncrement(1);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setMinorTickCount(0);
        depthSlider.setSnapToTicks(true);
        depthSlider.setShowTickLabels(true);
        depthSlider.valueProperty().addListener((obs, o, n) ->
                depthLabel.setText(String.valueOf((int) Math.round(n.doubleValue()))));

        generateButton.setMaxWidth(Double.MAX_VALUE);
        generateButton.setDefaultButton(true);
        generateButton.setOnAction(e -> { if (onGenerate != null) onGenerate.run(); });

        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setOnAction(e -> {
            LSystemPreset p = presetBox.getSelectionModel().getSelectedItem();
            if (p != null) populateFrom(p);
        });

        getChildren().addAll(
            new Label("Preset:"), presetBox,
            new Label("Axiom:"), axiomField,
            new Label("Rules (one per line, X=…):"), rulesArea,
            new Label("Angle δ (degrees):"), withNumericFormatter(angleField),
            new Label("Step length:"), withNumericFormatter(stepField),
            new Label("Depth:"), new HBox(6, depthSlider, depthLabel),
            generateButton, resetButton
        );

        presetBox.getSelectionModel().selectFirst();
    }

    public void setOnGenerate(Runnable r) { this.onGenerate = r; }

    public Button getGenerateButton() { return generateButton; }

    public LSystemParams readParams() {
        String axiom = axiomField.getText().trim();
        Map<Character, String> rules = parseRules(rulesArea.getText());
        double angle = parseDouble(angleField.getText(), 90.0);
        double step = parseDouble(stepField.getText(), 8.0);
        int depth = (int) Math.round(depthSlider.getValue());
        return new LSystemParams(axiom, rules, angle, step, depth);
    }

    private void populateFrom(LSystemPreset p) {
        axiomField.setText(p.axiom());
        StringBuilder sb = new StringBuilder();
        p.rules().forEach((k, v) -> sb.append(k).append('=').append(v).append('\n'));
        rulesArea.setText(sb.toString().trim());
        angleField.setText(String.valueOf(p.angle()));
        stepField.setText(String.valueOf(p.stepLength()));
        depthSlider.setValue(p.defaultDepth());
    }

    private Map<Character, String> parseRules(String text) {
        Map<Character, String> rules = new HashMap<>();
        for (String line : text.split("\\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            int eq = line.indexOf('=');
            if (eq < 1) continue;
            String lhs = line.substring(0, eq).trim();
            String rhs = line.substring(eq + 1);
            if (lhs.length() == 1) rules.put(lhs.charAt(0), rhs);
        }
        return rules;
    }

    private double parseDouble(String text, double fallback) {
        try { return Double.parseDouble(text.trim()); }
        catch (NumberFormatException e) { return fallback; }
    }

    private TextField withNumericFormatter(TextField field) {
        field.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0,
                c -> c.getControlNewText().matches("-?\\d*\\.?\\d*") ? c : null));
        return field;
    }
}
