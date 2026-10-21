package cpoo2.scenario;

import cpoo2.jeu.Partie;

/** Non terminal : {@code non c}. */
public record Non(Condition condition) implements Condition {

    @Override public boolean evaluer(Partie partie) {
        throw new UnsupportedOperationException("TODO S04 Q3");
    }

    @Override public String toString() { return "non " + condition; }
}
