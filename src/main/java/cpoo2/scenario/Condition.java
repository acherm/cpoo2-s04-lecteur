package cpoo2.scenario;

import java.util.List;

import cpoo2.jeu.Partie;

/**
 * Une condition sur une partie : celle qui suit {@code vérifier} dans un scénario, ou celle que le
 * lecteur de parties cherche ({@code --chercher}). S04, étape 3 (Q3) : chaque nœud sait s'évaluer
 * sur une partie, telle qu'elle est maintenant.
 *
 * <pre>
 * condition := facteur ("et" facteur)*
 * facteur   := "non" facteur | "ballon en" CASE | "trait aux" ÉQUIPE | "vainqueur" ÉQUIPE
 * </pre>
 */
public sealed interface Condition permits BallonEn, TraitAux, Vainqueur, Et, Non {

    /** Vrai si la condition est satisfaite par cette partie, telle qu'elle est maintenant. */
    boolean evaluer(Partie partie);

    /** Fourni : {@code Condition.lire("ballon en e3 et non trait aux bleus")} construit l'arbre. */
    static Condition lire(String texte) {
        return Scenario.condition(List.of(texte.strip().split("\\s+")));
    }
}
