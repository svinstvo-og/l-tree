package cz.cvut.pgr2.lsystem.engine;

import java.util.Map;

public class LSystemEngine {

    public String rewrite(String axiom, Map<Character, String> rules, int depth) {
        String current = axiom;
        for (int i = 0; i < depth; i++) {
            StringBuilder next = new StringBuilder(current.length() * 4);
            for (int j = 0; j < current.length(); j++) {
                char c = current.charAt(j);
                String replacement = rules.get(c);
                if (replacement != null) {
                    next.append(replacement);
                } else {
                    next.append(c);
                }
            }
            current = next.toString();
        }
        return current;
    }
}
