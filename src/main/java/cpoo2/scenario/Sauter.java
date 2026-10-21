package cpoo2.scenario;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Coup;
import cpoo2.jeu.Position;

/** {@code bleus saut c5-d4} : un saut, par une équipe, de la case de départ à la case d'arrivée de la pièce. */
public record Sauter(Couleur equipe, Position de, Position vers) implements Instruction {

    /** Le coup du jeu que décrit cette ligne. */
    public Coup enCoup() { return Coup.lire("saut " + de + "-" + vers); }

    @Override public void accept(VisiteurDeScenario visiteur) {
        throw new UnsupportedOperationException("TODO S04 Q1");
    }

    @Override public String toString() { return Scenario.nom(equipe) + " saut " + de + "-" + vers; }
}
