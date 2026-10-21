package cpoo2.jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import arbitre.Arbitre;

/**
 * Une vraie partie de ChessBall : elle arbitre, applique, et sait quand elle est finie. Les coups
 * possibles et leur effet viennent de l'arbitre, livré compilé ({@code arbitre.Arbitre}, dans
 * {@code lib/}) : il ne connaît qu'une position, jamais l'histoire de la partie. Ce qui dépend de
 * l'histoire est ici :
 *
 * <ul>
 * <li>les <b>représailles</b> : juste après un tacle, la victime ne peut ni tacler son tacleur ni
 * sauter par-dessus lui ;</li>
 * <li>la <b>fin</b> : un but (le ballon sur une ligne de but), ou une partie nulle, quand
 * l'équipe au trait n'a aucun coup, ou après {@value #DEMI_COUPS_SANS_POUSSEE_MAX} demi-coups
 * sans poussée du ballon.</li>
 * </ul>
 *
 * <p>Fournie : le lecteur de parties la rejoue, il ne la modifie pas.</p>
 */
public class Partie {

    public static final int DEMI_COUPS_SANS_POUSSEE_MAX = 80;

    private Plateau plateau;
    private Couleur trait;
    private Position tacleur;        // non nuls juste après un tacle
    private Position victime;
    private boolean terminee;
    private Couleur vainqueur;       // null tant que personne n'a marqué
    private int demiCoupsSansPoussee;

    public Partie(Plateau plateau, Couleur trait) {
        this.plateau = plateau;
        this.trait = trait;
    }

    /** La position de départ de la boîte, les Bleus au trait. */
    public static Partie officielle() {
        return new Partie(Plateau.officiel(), Couleur.BLEUS);
    }

    /** L'équipe qui doit jouer. */
    public Couleur trait() { return trait; }

    /** La position, figée : la lire ne change rien. */
    public Plateau plateau() { return plateau; }

    public boolean estTerminee() { return terminee; }

    /** Les demi-coups joués depuis la dernière poussée du ballon. */
    public int demiCoupsSansPoussee() { return demiCoupsSansPoussee; }

    /** Les coups que permettent les règles dans cette position, sans tenir compte de l'histoire. */
    public List<Coup> coupsPossibles() {
        return Arbitre.coupsPossibles(plateau.enLigne(), trait.name()).stream().map(Coup::lire).toList();
    }

    /** Le vainqueur, ou vide si la partie est en cours ou nulle. */
    public Optional<Couleur> vainqueur() { return Optional.ofNullable(vainqueur); }

    /** Les coups légaux de l'équipe au trait : aucun si la partie est finie. */
    public List<Coup> coupsLegaux() {
        if (terminee) {
            return List.of();
        }
        List<Coup> coups = new ArrayList<>();
        for (Coup coup : coupsPossibles()) {
            if (!represailles(coup)) {
                coups.add(coup);
            }
        }
        return List.copyOf(coups);
    }

    /**
     * Joue le coup s'il est légal.
     *
     * @return vrai si le coup est accepté, faux s'il est refusé
     */
    public boolean jouer(Coup coup) {
        if (!coupsLegaux().contains(coup)) {
            return false;
        }
        plateau = Plateau.depuisLigne(Arbitre.apres(plateau.enLigne(), coup.toString()));
        if (coup instanceof Coup.Tacle) {
            tacleur = coup.arrivee();
            victime = coup.arrivee().voisine(coup.direction()).orElseThrow();
        } else {
            tacleur = null;
            victime = null;
        }
        demiCoupsSansPoussee = coup instanceof Coup.Poussee ? 0 : demiCoupsSansPoussee + 1;
        trait = trait.adverse();
        int rangeeDuBallon = plateau.ballon().rangee();
        if (rangeeDuBallon == Couleur.BLEUS.rangeeOuElleMarque()) {
            terminee = true;
            vainqueur = Couleur.BLEUS;
        } else if (rangeeDuBallon == Couleur.ROUGES.rangeeOuElleMarque()) {
            terminee = true;
            vainqueur = Couleur.ROUGES;
        } else if (demiCoupsSansPoussee >= DEMI_COUPS_SANS_POUSSEE_MAX || coupsLegaux().isEmpty()) {
            terminee = true;
        }
        return true;
    }

    /** Juste après un tacle, la victime ne peut ni tacler son tacleur, ni sauter par-dessus lui. */
    private boolean represailles(Coup coup) {
        return victime != null
                && coup.origine().equals(victime)
                && (coup instanceof Coup.Tacle || coup instanceof Coup.Saut)
                && coup.origine().voisine(coup.direction()).orElseThrow().equals(tacleur);
    }
}
