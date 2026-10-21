package cpoo2.scenario;

import java.util.ArrayList;
import java.util.List;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Coup;
import cpoo2.jeu.Partie;

/**
 * S04, Q1 : jouer un scénario, ou une partie enregistrée, sur une vraie partie. Un coup est joué
 * s'il est à l'équipe au trait et si la partie l'accepte, une vérification est évaluée (sa
 * condition, Q4). Chaque échec est noté, avec le numéro de l'instruction, et l'exécution continue.
 */
public final class Executeur implements VisiteurDeScenario {

    private final Partie partie;
    private final List<String> echecs = new ArrayList<>();
    private int numero;   // le numéro de l'instruction en cours

    public Executeur(Partie partie) {
        this.partie = partie;
    }

    /** Joue le scénario depuis sa position de départ. */
    public static Executeur executer(Scenario scenario) {
        Executeur e = new Executeur(new Partie(scenario.depart(), scenario.trait()));
        scenario.parcourir(e);
        return e;
    }

    public Partie partie() { return partie; }

    /** Les échecs, « instruction 3 (rouges saut c2-c4) : coup refusé ». Vide si tout s'est bien passé. */
    public List<String> echecs() { return List.copyOf(echecs); }

    // TODO S04 Q1 : les quatre coups appellent tous jouer(...), une seule ligne chacun :
    // visiterDeplacer(d) fait jouer(d, d.equipe(), d.enCoup()), et de même pour les trois autres.

    @Override public void visiterDeplacer(Deplacer d) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterPousser(Pousser p) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterTacler(Tacler t) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterSauter(Sauter s) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    /** TODO S04 Q1 : numero++, puis, si la condition est fausse sur la partie, echec(v, "fausse"). */
    @Override public void visiterVerifier(Verifier v) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    /**
     * TODO S04 Q1 : numero++, puis : si ce n'est pas le trait de cette équipe, l'échec
     * « ce n'est pas aux bleus de jouer » (Scenario.nom(equipe) donne « bleus » ou « rouges ») ;
     * sinon, si la partie refuse le coup (jouer rend faux), l'échec « coup refusé ».
     */
    private void jouer(Instruction i, Couleur equipe, Coup coup) {
        throw new UnsupportedOperationException("TODO S04 Q1");
    }

    private void echec(Instruction i, String pourquoi) {
        echecs.add("instruction " + numero + " (" + i + ") : " + pourquoi);
    }
}
