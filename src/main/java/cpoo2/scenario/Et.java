package cpoo2.scenario;

import cpoo2.jeu.Partie;

/** Non terminal : {@code c1 et c2}. */
public record Et(Condition gauche, Condition droite) implements Condition {

    @Override public boolean evaluer(Partie partie) {
        throw new UnsupportedOperationException("TODO S04 Q3");
    }

    @Override public String toString() { return gauche + " et " + droite; }
}
