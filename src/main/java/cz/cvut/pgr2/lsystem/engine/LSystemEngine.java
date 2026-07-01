package cz.cvut.pgr2.lsystem.engine;

import java.util.Map;

/** Rewrites an L-system axiom by applying production rules in parallel for {@code depth} iterations. */
public class LSystemEngine {

    /**
     * Expands {@code axiom} by substituting each character with its rule replacement {@code depth} times.
     * Characters with no matching rule are copied unchanged.
     */
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
