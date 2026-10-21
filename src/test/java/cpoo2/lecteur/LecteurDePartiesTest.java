package cpoo2.lecteur;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import cpoo2.scenario.Executeur;
import cpoo2.scenario.Scenario;

/** S04 : le lecteur de parties, une étape après l'autre. Tous rouges sur le squelette. */
class LecteurDePartiesTest {

    static final LecteurDeParties ENTRAINEMENT = LecteurDeParties.charger("entrainement");

    static String bilan(LecteurDeParties lecteur) {
        return lecteur.bilan(lecteur.rejouer(lecteur.nouvellePartie()));
    }

    static Executeur rejouer(String partie) {
        LecteurDeParties lecteur = new LecteurDeParties(Scenario.lire(partie));
        return lecteur.rejouer(lecteur.nouvellePartie());
    }

    @Test
    void etape1LEntrainementEstGagneParLesBleus() {
        assertEquals("bleus (annoncé : bleus)", bilan(ENTRAINEMENT));
    }

    @Test
    void etape1LaCopieFautiveSignaleSonPremierEchec() {
        assertEquals("pas finie (annoncé : bleus)\n  17 échecs, le premier : instruction 18 (rouges tacle f2-e2) : coup refusé",
                bilan(LecteurDeParties.charger("entrainement-recopiee")),
                "une seule faute de recopie : le premier échec la désigne, les suivants en découlent");
    }

    @Test
    void etape1LeResultatSeLitSurLaPartieRejouee() {
        String attaque = """
                départ officiel
                bleus poussée c5-d4
                rouges déplacement c2-c3
                bleus poussée d4-e3
                rouges déplacement c3-c4
                bleus poussée e3-f2
                """;
        assertEquals("bleus", rejouer(attaque).resultat(), "le ballon est en g1 : les Bleus ont marqué");
        assertEquals("pas finie", rejouer("départ officiel\nbleus poussée c5-d4").resultat(),
                "un seul coup : personne n'a marqué, et la partie n'est pas nulle");
    }

    @Test
    void etape2LeResumeCompteSansJouer() {
        assertEquals("41 demi-coups : 24 déplacements, 9 poussées, 3 tacles, 5 sauts", ENTRAINEMENT.resume());
    }

    @Test
    void etape3LaRechercheTrouveLesPositions() {
        assertEquals(List.of(16, 17, 18, 19, 20, 21, 22, 23, 24, 25), ENTRAINEMENT.chercher("ballon en e4"));
        assertEquals(List.of(27, 29, 31, 33), ENTRAINEMENT.chercher("ballon en d5 et trait aux rouges"));
        assertEquals(List.of(3), ENTRAINEMENT.chercher("ballon en e3 et non trait aux bleus"));
        assertEquals(List.of(41), ENTRAINEMENT.chercher("vainqueur bleus"));
        assertEquals(List.of(), ENTRAINEMENT.chercher("vainqueur rouges"));
    }
}
