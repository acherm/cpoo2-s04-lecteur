package cpoo2.motifs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Coup;
import cpoo2.jeu.Partie;
import cpoo2.jeu.Position;
import cpoo2.lecteur.LecteurDeParties;
import cpoo2.scenario.BallonEn;
import cpoo2.scenario.Condition;
import cpoo2.scenario.Deplacer;
import cpoo2.scenario.Et;
import cpoo2.scenario.Instruction;
import cpoo2.scenario.Non;
import cpoo2.scenario.Pousser;
import cpoo2.scenario.Sauter;
import cpoo2.scenario.PartieEnregistree;
import cpoo2.scenario.Tacler;
import cpoo2.scenario.TraitAux;
import cpoo2.scenario.Vainqueur;
import cpoo2.scenario.Verifier;

/**
 * S04, Q9 : le lecteur de parties sans visiteur. {@link Instruction} et {@link Condition} sont scellées :
 * un {@code switch} sur leur type remplace {@code accept}, les visiteurs, et les méthodes {@code evaluer}
 * des nœuds. Une opération, une méthode. Indépendant des Q1 à Q4 : rien ici n'appelle {@code accept}.
 *
 * <pre>
 * mvn -q compile exec:java -Dexec.mainClass=cpoo2.motifs.LecteurParMotifs
 * </pre>
 */
public final class LecteurParMotifs {

    private final PartieEnregistree partieEnregistree;   // la partie enregistrée, telle que lue

    public LecteurParMotifs(PartieEnregistree partieEnregistree) {
        this.partieEnregistree = partieEnregistree;
    }

    /** Fourni : {@code charger("entrainement")} lit {@code parties/entrainement.partie}. */
    public static LecteurParMotifs charger(String nom) {
        return new LecteurParMotifs(PartieEnregistree.charger("parties/" + nom + ".partie"));
    }

    /** Fourni : une partie neuve, à la position de départ du fichier. */
    public Partie nouvellePartie() {
        return new Partie(partieEnregistree.depart(), partieEnregistree.trait());
    }

    /**
     * Q9 : la phrase de la Q2, « 41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts », sans
     * Statistiques : une boucle sur partieEnregistree.instructions(), et un switch sur chaque instruction.
     */
    public String resume() {
        // TODO S04 Q9 : quatre compteurs, puis switch (instruction) { case Deplacer d -> ... ; ... }
        throw new UnsupportedOperationException("TODO S04 Q9");
    }

    /**
     * Q9 : la Q3 en une seule méthode, sans evaluer dans les nœuds : un switch sur la condition. Les
     * motifs d'enregistrement donnent les composants : case Et(Condition gauche, Condition droite) -> ...
     * Et et Non s'appellent récursivement.
     */
    public static boolean evaluer(Condition condition, Partie partie) {
        // TODO S04 Q9 : return switch (condition) { case BallonEn(Position cas) -> ... ; ... };
        throw new UnsupportedOperationException("TODO S04 Q9");
    }

    /**
     * Q9 : la Q1 sans ChessBallGamePlayer. Joue chaque instruction du fichier sur la partie « sur », dans l'ordre,
     * et rend les échecs, au format du ChessBallGamePlayer (voir echec) : « ce n'est pas aux bleus de jouer »,
     * « coup refusé », ou « fausse » pour une vérification. Vide si tout s'est bien passé.
     */
    public List<String> rejouer(Partie sur) {
        // TODO S04 Q9 : un numéro par instruction, et un switch (instruction) pour la jouer
        throw new UnsupportedOperationException("TODO S04 Q9");
    }

    /** Fourni : « instruction 18 (rouges tacle f2-e2) : coup refusé », comme le ChessBallGamePlayer. */
    static String echec(int numero, Instruction instruction, String pourquoi) {
        return "instruction " + numero + " (" + instruction + ") : " + pourquoi;
    }

    /** Fourni : « bleus » ou « rouges ». */
    static String nom(Couleur equipe) {
        return equipe == Couleur.BLEUS ? "bleus" : "rouges";
    }

    /**
     * Fourni : le bilan du lecteur, « bleus (annoncé : bleus) », sur la partie rejouée et les échecs que
     * rejouer a rendus. Même mise en forme que LecteurDeParties.
     */
    public String bilan(Partie rejouee, List<String> echecs) {
        return LecteurDeParties.bilan(resultat(rejouee), partieEnregistree.entete().getOrDefault("Résultat", "?"), echecs);
    }

    /** Fourni : « bleus », « rouges », « nulle », ou « pas finie », sans ChessBallGamePlayer. */
    static String resultat(Partie partie) {
        if (!partie.estTerminee()) {
            return "pas finie";
        }
        return partie.vainqueur().map(LecteurParMotifs::nom).orElse("nulle");
    }

    /** Fourni : le bilan de chaque partie de la boîte, ou « pas encore écrit » tant que la Q9 ne l'est pas. */
    public static List<String> bilans() {
        List<String> lignes = new ArrayList<>();
        for (String nom : LecteurDeParties.PARTIES) {
            LecteurParMotifs lecteur = charger(nom);
            Partie p = lecteur.nouvellePartie();
            try {
                lignes.add(nom + " : " + lecteur.bilan(p, lecteur.rejouer(p)));
            } catch (UnsupportedOperationException e) {
                if (e.getMessage() == null || !e.getMessage().startsWith("TODO")) {
                    throw e;
                }
                lignes.add(nom + " : pas encore écrit (" + e.getMessage() + ")");
            }
        }
        return lignes;
    }

    /** Fourni : les mêmes bilans que LecteurDeParties sans argument, sans un seul visiteur. */
    public static void main(String[] args) {
        bilans().forEach(System.out::println);
    }
}
