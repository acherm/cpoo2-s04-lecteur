package cpoo2.lecteur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import cpoo2.jeu.Partie;
import cpoo2.scenario.Condition;
import cpoo2.scenario.Executeur;
import cpoo2.scenario.Instruction;
import cpoo2.scenario.Scenario;
import cpoo2.scenario.Statistiques;

/**
 * Le lecteur de parties de la séance 4, que vous complétez étape par étape (Q2, Q4). Il lit une
 * partie enregistrée, la rejoue et en donne le résultat, la résume, et y cherche des positions.
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

    /** Q2 : « 41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts », sans rien jouer. */
    public String resume() {
        // TODO S04 Q2 : une Statistiques, fichier.parcourir(statistiques), puis la phrase
        throw new UnsupportedOperationException("TODO S04 Q2");
    }

    /** Fourni : une partie neuve, à la position de départ du fichier. */
    public Partie nouvellePartie() {
        return new Partie(fichier.depart(), fichier.trait());
    }

    /**
     * Fourni : rejoue toutes les lignes du fichier sur cette partie. Un Executeur passe sur chaque
     * ligne (fichier.parcourir), puis on le rend : il garde la partie rejouée, son résultat et ses
     * échecs. C'est le modèle du résumé (Q2) : un objet, un parcours, puis on lui demande ce qu'il a vu.
     */
    public Executeur rejouer(Partie sur) {
        Executeur executeur = new Executeur(sur);
        fichier.parcourir(executeur);
        return executeur;
    }

    /**
     * Fourni : « bleus (annoncé : bleus) », le résultat de la partie rejouée (Q1) et celui
     * qu'annonce l'en-tête, puis, s'il y en a, le nombre d'échecs et le premier, le seul qui désigne
     * la faute (les suivants en découlent).
     */
    public String bilan(Executeur executeur) {
        return bilan(executeur.resultat(), fichier.entete().getOrDefault("Résultat", "?"), executeur.echecs());
    }

    /** Fourni : la mise en forme du bilan, commune à ce lecteur et à celui de la Q9. */
    public static String bilan(String resultat, String annonce, List<String> echecs) {
        String bilan = resultat + " (annoncé : " + annonce + ")";
        if (echecs.size() == 1) {
            bilan += "\n  1 échec : " + echecs.get(0);
        } else if (echecs.size() > 1) {
            bilan += "\n  " + echecs.size() + " échecs, le premier : " + echecs.get(0);
        }
        return bilan;
    }

    /** Q4 : les numéros des instructions après lesquelles la condition est vraie. */
    public List<Integer> chercher(String texte) {
        // TODO S04 Q4 : la condition lue, une partie neuve et son Executeur, puis, instruction après
        // instruction : accept(executeur), et si la condition est vraie, noter le numéro
        throw new UnsupportedOperationException("TODO S04 Q4");
    }

    /**
     * Fourni : le lecteur au travail. La présentation, le résumé (Q2), la partie rejouée avec son plateau
     * final et son bilan (Q1), puis ce qu'on y cherche (Q4). Chaque morceau s'affiche dès que sa question
     * est écrite, et « pas encore écrit » avant : rien à écrire ici.
     */
    public void lire(Optional<String> recherche) {
        System.out.println(presentation());
        System.out.println(essayer(this::resume));
        System.out.println(essayer(() -> {
            Partie p = nouvellePartie();
            Executeur executeur = rejouer(p);
            return p.plateau().enTexte() + "résultat : " + bilan(executeur);
        }));
        recherche.ifPresent(c -> System.out.println(essayer(() -> "« " + c + " » : après les demi-coups " + chercher(c))));
    }

    /**
     * Fourni : la ligne de commande. Sans argument, le bilan de chaque partie de la boîte. Avec un
     * nom de partie, sa lecture. {@code --chercher} prend tout ce qui le suit comme condition.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            for (String nom : PARTIES) {
                System.out.println(nom + " : " + bilanDe(nom));
            }
            return;
        }
        List<String> mots = List.of(args);
        int i = mots.indexOf("--chercher");
        Optional<String> recherche = i < 0 ? Optional.empty()
                : Optional.of(String.join(" ", mots.subList(i + 1, mots.size())));
        charger(args[0]).lire(recherche);
    }

    /** Le bilan d'une partie de la boîte, ou « pas encore écrit » tant que la Q1 ne l'est pas. */
    private static String bilanDe(String nom) {
        LecteurDeParties lecteur = charger(nom);
        return essayer(() -> lecteur.bilan(lecteur.rejouer(lecteur.nouvellePartie())));
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
