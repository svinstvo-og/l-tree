package cz.cvut.pgr2.lsystem.gui;

/** Thin launcher wrapper required so the fat JAR can start without the JavaFX module on the classpath. */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
