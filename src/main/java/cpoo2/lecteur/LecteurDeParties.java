package cpoo2.lecteur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Partie;
import cpoo2.scenario.Condition;
import cpoo2.scenario.Executeur;
import cpoo2.scenario.Instruction;
import cpoo2.scenario.Scenario;
import cpoo2.scenario.Statistiques;

/**
 * Le lecteur de parties de la séance 4, que vous écrivez étape par étape (Q2, Q3, Q5). Il lit une
 * partie enregistrée, la rejoue, la juge contre son en-tête, la résume, et y cherche des positions.
 *
 * <pre>
 * mvn -q compile exec:java
 * mvn -q compile exec:java -Dexec.args="entrainement"
 * mvn -q compile exec:java -Dexec.args="entrainement --chercher ballon en e3"
 * </pre>
 */
public final class LecteurDeParties {

    /** Les parties de la boîte, rangées dans {@code src/main/resources/parties/}. */
    public static final List<String> PARTIES = List.of(
            "entrainement", "entrainement-recopiee", "patiente", "club-voisin", "eclair", "sans-touche");

    private final Scenario fichier;   // la partie enregistrée, telle que lue

    public LecteurDeParties(Scenario fichier) {
        this.fichier = fichier;
    }

    /** Fourni : {@code charger("entrainement")} lit {@code parties/entrainement.partie}. */
    public static LecteurDeParties charger(String nom) {
        String ressource = nom.contains("/") ? nom : "parties/" + nom + ".partie";
        return new LecteurDeParties(Scenario.charger(ressource));
    }

    /** Fourni : « Entraînement, 2026-10-07 : IA gloutonne (bleus) contre IA gloutonne (rouges) ». */
    public String presentation() {
        Map<String, String> e = fichier.entete();
        return e.getOrDefault("Événement", "Partie sans nom") + ", " + e.getOrDefault("Date", "sans date") + " : "
                + e.getOrDefault("Bleus", "?") + " (bleus) contre " + e.getOrDefault("Rouges", "?") + " (rouges)";
    }

    /** Q3 : « 41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts », sans rien jouer. */
    public String resume() {
        // TODO S04 Q3 : une Statistiques, fichier.parcourir(statistiques), puis la phrase
        throw new UnsupportedOperationException("TODO S04 Q3");
    }

    /** Fourni : une partie neuve, à la position de départ du fichier. */
    public Partie nouvellePartie() {
        return new Partie(fichier.depart(), fichier.trait());
    }

    /** Q2 : joue chaque instruction du fichier sur cette partie, et rend l'exécuteur, qui garde les échecs. */
    public Executeur rejouer(Partie sur) {
        // TODO S04 Q2 : un Executeur sur la partie « sur », fichier.parcourir(executeur), et le rendre
        throw new UnsupportedOperationException("TODO S04 Q2");
    }

    /**
     * Q2 : le jugement de la partie rejouée. « conforme : bleus », ou « non conforme : » suivi du
     * premier échec, de « la partie n'est pas finie », ou de « rouges, l'en-tête annonce bleus ».
     */
    public String verdict(Executeur executeur) {
        // TODO S04 Q2 : trois cas, dans l'ordre du sujet
        throw new UnsupportedOperationException("TODO S04 Q2");
    }

    /** Q5 : les numéros des instructions après lesquelles la condition est vraie. */
    public List<Integer> chercher(String texte) {
        // TODO S04 Q5 : la condition lue, une partie neuve et son Executeur, puis, instruction après
        // instruction : accept(executeur), et si la condition est vraie, noter le numéro
        throw new UnsupportedOperationException("TODO S04 Q5");
    }

    /**
     * Fourni : le lecteur au travail. La présentation, le résumé (Q3), la partie rejouée avec son plateau
     * final et son verdict (Q2), puis ce qu'on y cherche (Q5). Chaque morceau s'affiche dès que sa question
     * est écrite, et « pas encore écrit » avant : rien à écrire ici.
     */
    public void lire(Optional<String> recherche) {
        System.out.println(presentation());
        System.out.println(essayer(this::resume));
        System.out.println(essayer(() -> {
            Partie p = nouvellePartie();
            Executeur executeur = rejouer(p);
            return p.plateau().enTexte() + verdict(executeur);
        }));
        recherche.ifPresent(c -> System.out.println(essayer(() -> "« " + c + " » : après les demi-coups " + chercher(c))));
    }

    /** Fourni : « bleus », « rouges » ou « nulle », comme dans un en-tête. */
    static String resultat(Partie partie) {
        return partie.vainqueur().map(c -> c == Couleur.BLEUS ? "bleus" : "rouges").orElse("nulle");
    }

    /**
     * Fourni : la ligne de commande. Sans argument, le verdict de chaque partie de la boîte. Avec un
     * nom de partie, sa lecture. {@code --chercher} prend tout ce qui le suit comme condition.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            for (String nom : PARTIES) {
                System.out.println(nom + " : " + jugement(nom));
            }
            return;
        }
        List<String> mots = List.of(args);
        int i = mots.indexOf("--chercher");
        Optional<String> recherche = i < 0 ? Optional.empty()
                : Optional.of(String.join(" ", mots.subList(i + 1, mots.size())));
        charger(args[0]).lire(recherche);
    }

    /** Le verdict d'une partie de la boîte, ou « pas encore écrit » tant que la Q2 ne l'est pas. */
    private static String jugement(String nom) {
        LecteurDeParties lecteur = charger(nom);
        return essayer(() -> lecteur.verdict(lecteur.rejouer(lecteur.nouvellePartie())));
    }

    /** Le texte d'un morceau du lecteur, ou « pas encore écrit (TODO S04 Qn) » tant que sa question ne l'est pas. */
    private static String essayer(Supplier<String> morceau) {
        try {
            return morceau.get();
        } catch (UnsupportedOperationException e) {
            if (e.getMessage() == null || !e.getMessage().startsWith("TODO")) {
                throw e;
            }
            return "pas encore écrit (" + e.getMessage() + ")";
        }
    }
}
