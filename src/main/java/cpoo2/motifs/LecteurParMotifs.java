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
import cpoo2.scenario.Scenario;
import cpoo2.scenario.Tacler;
import cpoo2.scenario.TraitAux;
import cpoo2.scenario.Vainqueur;
import cpoo2.scenario.Verifier;

/**
 * S04, Q10 : le lecteur de parties sans visiteur. {@link Instruction} et {@link Condition} sont scellées :
 * un {@code switch} sur leur type remplace {@code accept}, les visiteurs, et les méthodes {@code evaluer}
 * des nœuds. Une opération, une méthode. Indépendant des Q1 à Q5 : rien ici n'appelle {@code accept}.
 *
 * <pre>
 * mvn -q compile exec:java -Dexec.mainClass=cpoo2.motifs.LecteurParMotifs
 * </pre>
 */
public final class LecteurParMotifs {

    private final Scenario fichier;   // la partie enregistrée, telle que lue

    public LecteurParMotifs(Scenario fichier) {
        this.fichier = fichier;
    }

    /** Fourni : {@code charger("entrainement")} lit {@code parties/entrainement.partie}. */
    public static LecteurParMotifs charger(String nom) {
        return new LecteurParMotifs(Scenario.charger("parties/" + nom + ".partie"));
    }

    /** Fourni : une partie neuve, à la position de départ du fichier. */
    public Partie nouvellePartie() {
        return new Partie(fichier.depart(), fichier.trait());
    }

    /**
     * Q10 : la phrase de la Q3, « 41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts », sans
     * Statistiques : une boucle sur fichier.instructions(), et un switch sur chaque instruction.
     */
    public String resume() {
        // TODO S04 Q10 : quatre compteurs, puis switch (instruction) { case Deplacer d -> ... ; ... }
        throw new UnsupportedOperationException("TODO S04 Q10");
    }

    /**
     * Q10 : la Q4 en une seule méthode, sans evaluer dans les nœuds : un switch sur la condition. Les
     * motifs d'enregistrement donnent les composants : case Et(Condition gauche, Condition droite) -> ...
     * Et et Non s'appellent récursivement.
     */
    public static boolean evaluer(Condition condition, Partie partie) {
        // TODO S04 Q10 : return switch (condition) { case BallonEn(Position cas) -> ... ; ... };
        throw new UnsupportedOperationException("TODO S04 Q10");
    }

    /**
     * Q10 : la Q1 sans Executeur. Joue chaque instruction du fichier sur la partie « sur », dans l'ordre,
     * et rend les échecs, au format de l'exécuteur (voir echec) : « ce n'est pas aux bleus de jouer »,
     * « coup refusé », ou « fausse » pour une vérification. Vide si tout s'est bien passé.
     */
    public List<String> rejouer(Partie sur) {
        // TODO S04 Q10 : un numéro par instruction, et un switch (instruction) pour la jouer
        throw new UnsupportedOperationException("TODO S04 Q10");
    }

    /** Fourni : « instruction 18 (rouges tacle f2-e2) : coup refusé », comme l'exécuteur. */
    static String echec(int numero, Instruction instruction, String pourquoi) {
        return "instruction " + numero + " (" + instruction + ") : " + pourquoi;
    }

    /** Fourni : « bleus » ou « rouges ». */
    static String nom(Couleur equipe) {
        return equipe == Couleur.BLEUS ? "bleus" : "rouges";
    }

    /** Fourni : le verdict de la Q2, sur la partie rejouée et les échecs que rejouer a rendus. */
    public String verdict(Partie rejouee, List<String> echecs) {
        if (!echecs.isEmpty()) {
            return "non conforme : " + echecs.get(0);
        }
        if (!rejouee.estTerminee()) {
            return "non conforme : la partie n'est pas finie";
        }
        String resultat = rejouee.vainqueur().map(LecteurParMotifs::nom).orElse("nulle");
        String annonce = fichier.entete().get("Résultat");
        return resultat.equals(annonce) ? "conforme : " + resultat
                : "non conforme : " + resultat + ", l'en-tête annonce " + annonce;
    }

    /** Fourni : le verdict de chaque partie de la boîte, ou « pas encore écrit » tant que la Q10 ne l'est pas. */
    public static List<String> verdicts() {
        List<String> lignes = new ArrayList<>();
        for (String nom : LecteurDeParties.PARTIES) {
            LecteurParMotifs lecteur = charger(nom);
            Partie p = lecteur.nouvellePartie();
            try {
                lignes.add(nom + " : " + lecteur.verdict(p, lecteur.rejouer(p)));
            } catch (UnsupportedOperationException e) {
                if (e.getMessage() == null || !e.getMessage().startsWith("TODO")) {
                    throw e;
                }
                lignes.add(nom + " : pas encore écrit (" + e.getMessage() + ")");
            }
        }
        return lignes;
    }

    /** Fourni : les mêmes six lignes que LecteurDeParties sans argument, sans un seul visiteur. */
    public static void main(String[] args) {
        verdicts().forEach(System.out::println);
    }
}
