package cpoo2.scenario;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Partie;

/** Terminal : {@code trait aux bleus}. */
public record TraitAux(Couleur equipe) implements Condition {

    @Override public boolean evaluer(Partie partie) {
        throw new UnsupportedOperationException("TODO S04 Q4");
    }

    @Override public String toString() { return "trait aux " + Scenario.nom(equipe); }
}
