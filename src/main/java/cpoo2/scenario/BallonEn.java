package cpoo2.scenario;

import cpoo2.jeu.Partie;
import cpoo2.jeu.Position;

/** Terminal : {@code ballon en e3}. */
public record BallonEn(Position cas) implements Condition {

    @Override public boolean evaluer(Partie partie) {
        throw new UnsupportedOperationException("TODO S04 Q4");
    }

    @Override public String toString() { return "ballon en " + cas; }
}
