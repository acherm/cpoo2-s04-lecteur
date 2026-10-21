package cpoo2.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Plateau;
import cpoo2.jeu.Position;

/** La lecture des scénarios, fournie : ces tests passent déjà. */
class ScenarioTest {

    @Test
    void lesLignesDeviennentDesInstructions() {
        Scenario s = Scenario.lire(Exemples.PREMIERE_ATTAQUE);
        assertEquals(Plateau.officiel(), s.depart());
        assertEquals(Couleur.BLEUS, s.trait());
        assertEquals(7, s.instructions().size(), "les commentaires et la ligne de départ ne sont pas des instructions");
        assertEquals(new Pousser(Couleur.BLEUS, Position.of("c5"), Position.of("d4")), s.instructions().get(0));
    }

    @Test
    void lesConditionsSontLuesDeGaucheADroite() {
        assertEquals(new Et(new Et(new BallonEn(Position.of("e3")), new Non(new TraitAux(Couleur.BLEUS))), new Vainqueur(Couleur.ROUGES)),
                Scenario.condition(List.of("ballon", "en", "e3", "et", "non", "trait", "aux", "bleus", "et", "vainqueur", "rouges")));
    }

    @Test
    void unDepartQuelconque() {
        Scenario s = Scenario.lire("départ .D.D.../..A.A../......./....D../..a.ao./.d.d.d. bleus\nbleus poussée e3-f2");
        assertEquals(Position.of("f2"), s.depart().ballon());
    }

    @Test
    void uneLigneIllisibleEstRefusee() {
        assertThrows(IllegalArgumentException.class, () -> Scenario.lire("départ officiel\nbleus lob c5-e5"));
        assertThrows(IllegalArgumentException.class, () -> Scenario.lire("bleus poussée c5-d4"));
        assertThrows(IllegalArgumentException.class, () -> Scenario.lire("départ officiel\nbleus déplacement e5-e3"),
                "de e5 à e3, ce n'est pas un déplacement : refusé dès la lecture");
    }

    @Test
    void lEnTeteEstLuTelQuel() {
        Scenario s = Scenario.charger("parties/entrainement.partie");
        assertEquals("officielles", s.entete().get("Règles"));
        assertEquals("bleus", s.entete().get("Résultat"));
        assertEquals(41, s.instructions().size(), "les métadonnées ne sont pas des instructions");
        assertEquals(Map.of(), Scenario.lire(Exemples.PREMIERE_ATTAQUE).entete(), "pas d'en-tête, pas de métadonnées");
        assertThrows(IllegalArgumentException.class, () -> Scenario.lire("[Résultat bleus]\ndépart officiel"));
    }
}
