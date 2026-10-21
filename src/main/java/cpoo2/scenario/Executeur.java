package cpoo2.scenario;

import java.util.ArrayList;
import java.util.List;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Coup;
import cpoo2.jeu.Partie;

/**
 * S04, Q1 : jouer un scénario, ou une partie enregistrée, sur une vraie partie. Un coup est joué
 * s'il est à l'équipe au trait et si la partie l'accepte, une vérification est évaluée (sa
 * condition, Q3). Chaque échec est noté, avec le numéro de l'instruction, et l'exécution continue.
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

    /**
     * Le résultat de la partie rejouée : « bleus » ou « rouges » si une équipe a marqué, « nulle » si
     * la partie s'est finie sans but, « pas finie » sinon. Le lecteur l'appelle une fois toutes les
     * lignes passées : un visiteur ne sait pas quand le parcours se termine, c'est celui qui a lancé
     * parcourir qui le sait, et qui lui demande ensuite son résultat (comme Afficheur.texte()).
     *
     * <p>TODO S04 Q1 : partie.estTerminee() dit si la partie est finie, partie.vainqueur() qui a
     * marqué (vide si personne), et Scenario.nom(equipe) donne « bleus » ou « rouges ».</p>
     */
    public String resultat() {
        throw new UnsupportedOperationException("TODO S04 Q1");
    }

    // TODO S04 Q1 : les quatre coups se jouent de la même façon, et confient ce travail à jouer, en
    // une ligne chacun : visiterDeplacer(d) fait jouer(d, d.equipe(), d.enCoup()), et de même pour
    // les trois autres.

    @Override public void visiterDeplacer(Deplacer d) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterPousser(Pousser p) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterTacler(Tacler t) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    @Override public void visiterSauter(Sauter s) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    /**
     * TODO S04 Q1 : une vérification ne joue rien, elle affirme un fait sur la partie. Elle compte
     * comme une ligne (numero++), et si sa condition est fausse sur la partie, l'échec « fausse ».
     */
    @Override public void visiterVerifier(Verifier v) { throw new UnsupportedOperationException("TODO S04 Q1"); }

    /**
     * Fait jouer le coup par la partie : c'est l'appel partie.jouer(coup) qui joue le coup, la Partie
     * connaît les règles. Le travail commun aux quatre coups, pas une sixième méthode visiter : elle
     * ne regarde jamais la sorte de la ligne, et ne la reçoit que pour le message d'échec.
     *
     * <p>TODO S04 Q1, en trois temps :</p>
     * <ol>
     * <li>compter la ligne : numero++, le numéro qu'affiche echec.</li>
     * <li>avant de jouer, vérifier l'équipe. Un Coup n'a pas de couleur, la partie le joue pour
     * l'équipe au trait : si equipe n'est pas partie.trait(), ne pas jouer, et
     * echec(ligne, "ce n'est pas aux " + Scenario.nom(equipe) + " de jouer").</li>
     * <li>sinon, jouer le coup : appeler partie.jouer(coup). Cet appel joue le coup et rend vrai, ou
     * rend faux si le coup est interdit (rien n'a bougé). Le coup et le test tiennent donc dans le
     * même appel, if (!partie.jouer(coup)), et dans ce cas : echec(ligne, "coup refusé").</li>
     * </ol>
     */
    private void jouer(Instruction ligne, Couleur equipe, Coup coup) {
        throw new UnsupportedOperationException("TODO S04 Q1");
    }

    /** Fourni : note l'échec de la ligne en cours, « instruction 18 (rouges tacle f2-e2) : coup refusé ». */
    private void echec(Instruction ligne, String pourquoi) {
        echecs.add("instruction " + numero + " (" + ligne + ") : " + pourquoi);
    }
}
