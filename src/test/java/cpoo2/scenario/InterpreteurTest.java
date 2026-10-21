package cpoo2.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import cpoo2.jeu.Coup;
import cpoo2.jeu.Couleur;
import cpoo2.jeu.Partie;
import cpoo2.jeu.Position;

/**
 * S04, étape 3 (Q3) : les conditions s'évaluent sur une partie, chaque nœud à sa façon. Tous rouges
 * sur le squelette.
 */
class InterpreteurTest {

    static Partie apresUnePoussee() {
        Partie p = Partie.officielle();
        p.jouer(Coup.lire("poussée c5-d4"));            // le ballon part en e3, les Rouges ont le trait
        return p;
    }

    @Test
    void lesTerminaux() {
        Partie p = apresUnePoussee();
        assertTrue(new BallonEn(Position.of("e3")).evaluer(p));
        assertFalse(new BallonEn(Position.of("d4")).evaluer(p));
        assertTrue(new TraitAux(Couleur.ROUGES).evaluer(p));
        assertFalse(new TraitAux(Couleur.BLEUS).evaluer(p));
        assertFalse(new Vainqueur(Couleur.BLEUS).evaluer(p), "personne n'a encore gagné");
    }

    @Test
    void lesNonTerminauxEvaluentLeursEnfants() {
        Partie p = apresUnePoussee();
        Condition vraie = new BallonEn(Position.of("e3"));
        Condition fausse = new TraitAux(Couleur.BLEUS);
        assertTrue(new Et(vraie, vraie).evaluer(p));
        assertFalse(new Et(vraie, fausse).evaluer(p));
        assertFalse(new Et(fausse, vraie).evaluer(p));
        assertTrue(new Non(fausse).evaluer(p));
        assertFalse(new Non(new Non(fausse)).evaluer(p), "la récursion va jusqu'au bout");
    }

    @Test
    void uneConditionLueSEvalue() {
        Partie p = apresUnePoussee();
        assertTrue(Condition.lire("ballon en e3 et non trait aux bleus").evaluer(p));
        assertFalse(Condition.lire("vainqueur rouges et ballon en e3").evaluer(p));
    }

    @Test
    void lExecuteurEvalueLesVerifications() {
        assertEquals(List.of(), Executeur.executer(PartieEnregistree.lire(Exemples.PREMIERE_ATTAQUE)).echecs(),
                "les deux vérifications de la première attaque sont vraies");
        Executeur e = Executeur.executer(PartieEnregistree.lire("""
                départ officiel
                bleus poussée c5-d4
                vérifier ballon en d4
                rouges déplacement c2-c3
                vérifier trait aux bleus et ballon en e3
                """));
        assertEquals(List.of("instruction 2 (vérifier ballon en d4) : fausse"), e.echecs(),
                "une vérification compte comme une instruction");
    }

    @Test
    void leVainqueurApresUnBut() {
        Partie p = Executeur.executer(PartieEnregistree.lire(Exemples.PREMIERE_ATTAQUE)).partie();
        assertTrue(new Vainqueur(Couleur.BLEUS).evaluer(p));
        assertFalse(new Vainqueur(Couleur.ROUGES).evaluer(p));
    }
}
