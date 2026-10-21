package cpoo2.motifs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import cpoo2.jeu.Coup;
import cpoo2.jeu.Partie;
import cpoo2.scenario.Condition;
import cpoo2.scenario.Exemples;
import cpoo2.scenario.Scenario;

/** S04, Q9 : le lecteur sans visiteur, un switch par opération. Tous rouges sur le squelette. */
class MotifsTest {

    static final LecteurParMotifs ENTRAINEMENT = LecteurParMotifs.charger("entrainement");

    @Test
    void leResumeSansStatistiques() {
        assertEquals("41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts", ENTRAINEMENT.resume());
        assertEquals("5 demi-coups : 2 déplacements, 3 poussées, 0 tacles, 0 sauts",
                new LecteurParMotifs(Scenario.lire(Exemples.PREMIERE_ATTAQUE)).resume(),
                "les vérifications ne sont pas des demi-coups");
    }

    @Test
    void evaluerSansMethodeDansLesNoeuds() {
        Partie p = Partie.officielle();
        p.jouer(Coup.lire("poussée c5-d4"));            // le ballon part en e3, les Rouges ont le trait
        assertTrue(LecteurParMotifs.evaluer(Condition.lire("ballon en e3 et non trait aux bleus"), p));
        assertFalse(LecteurParMotifs.evaluer(Condition.lire("ballon en e3 et trait aux bleus"), p));
        assertFalse(LecteurParMotifs.evaluer(Condition.lire("vainqueur bleus"), p), "personne n'a encore gagné");
        assertFalse(LecteurParMotifs.evaluer(Condition.lire("non non trait aux bleus"), p),
                "la récursion va jusqu'au bout");
    }

    @Test
    void rejouerSansExecuteur() {
        Partie p = ENTRAINEMENT.nouvellePartie();
        assertEquals(List.of(), ENTRAINEMENT.rejouer(p));
        assertEquals("bleus (annoncé : bleus)", ENTRAINEMENT.bilan(p, List.of()));
        LecteurParMotifs copie = LecteurParMotifs.charger("entrainement-recopiee");
        List<String> echecs = copie.rejouer(copie.nouvellePartie());
        assertEquals(17, echecs.size(), "une faute de recopie, et tout ce qui suit déraille");
        assertEquals("instruction 18 (rouges tacle f2-e2) : coup refusé", echecs.get(0));
    }

    @Test
    void rejouerNoteLesEchecsEtLesVerificationsFausses() {
        LecteurParMotifs lecteur = new LecteurParMotifs(Scenario.lire("""
                départ officiel
                bleus poussée c5-d4
                bleus déplacement e5-e4
                vérifier ballon en d4
                rouges saut c2-c4
                vérifier trait aux rouges et ballon en e3
                """));
        assertEquals(List.of(
                "instruction 2 (bleus déplacement e5-e4) : ce n'est pas aux bleus de jouer",
                "instruction 3 (vérifier ballon en d4) : fausse",
                "instruction 4 (rouges saut c2-c4) : coup refusé"), lecteur.rejouer(lecteur.nouvellePartie()));
    }

    @Test
    void lesMemesBilansQueLeLecteur() {
        assertEquals(List.of(
                "entrainement : bleus (annoncé : bleus)",
                "entrainement-recopiee : pas finie (annoncé : bleus)\n  17 échecs, le premier : instruction 18 (rouges tacle f2-e2) : coup refusé",
                "patiente : bleus (annoncé : bleus)",
                "club-voisin : pas finie (annoncé : rouges)\n  9 échecs, le premier : instruction 31 (bleus tacle c2-c3) : coup refusé",
                "eclair : pas finie (annoncé : nulle)",
                "sans-touche : pas finie (annoncé : bleus)\n  30 échecs, le premier : instruction 4 (rouges poussée e2-f2) : coup refusé"),
                LecteurParMotifs.bilans());
    }
}
