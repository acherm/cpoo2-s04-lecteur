package cpoo2.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Position;

/**
 * S04, étapes 1 et 2 : jouer les instructions (Q1, {@code accept} et {@link Executeur}), puis les compter
 * (Q2, {@link Statistiques}). L'afficheur est fourni : son test passe dès que {@code accept} est écrit.
 */
class VisiteursTest {

    static final Scenario ATTAQUE = Scenario.lire(Exemples.PREMIERE_ATTAQUE);

    /** Un visiteur témoin : il note quelle méthode a été appelée. */
    static final class Temoin implements VisiteurDeScenario {
        final List<String> appels = new ArrayList<>();
        @Override public void visiterDeplacer(Deplacer d) { appels.add("Deplacer"); }
        @Override public void visiterPousser(Pousser p) { appels.add("Pousser"); }
        @Override public void visiterTacler(Tacler t) { appels.add("Tacler"); }
        @Override public void visiterSauter(Sauter s) { appels.add("Sauter"); }
        @Override public void visiterVerifier(Verifier v) { appels.add("Verifier"); }
    }

    @Test
    void acceptAppelleLaBonneMethode() {
        Temoin t = new Temoin();
        for (String ligne : List.of("bleus déplacement b6-b5", "bleus poussée c5-d4", "bleus tacle d5-c4",
                                    "bleus saut c5-e3", "vérifier trait aux bleus")) {
            Scenario.instruction(ligne).accept(t);
        }
        assertEquals(List.of("Deplacer", "Pousser", "Tacler", "Sauter", "Verifier"), t.appels,
                "le type déclaré est Instruction : c'est accept qui retrouve le type réel");
    }

    @Test
    void lAfficheurRendLeTexte() {
        Afficheur a = new Afficheur();
        ATTAQUE.parcourir(a);
        assertEquals("""
                bleus poussée c5-d4
                rouges déplacement c2-c3
                vérifier ballon en e3 et trait aux bleus
                bleus poussée d4-e3
                rouges déplacement c3-c4
                bleus poussée e3-f2
                vérifier vainqueur bleus et ballon en g1""", a.texte());
        Scenario relu = Scenario.lire("départ officiel\n" + a.texte());
        assertEquals(ATTAQUE, relu, "ce que l'afficheur écrit, le lecteur le relit à l'identique");
    }

    @Test
    void lesStatistiques() {
        Statistiques s = new Statistiques();
        ATTAQUE.parcourir(s);
        assertEquals(List.of(2, 3, 0, 0, 2), List.of(s.deplacements(), s.poussees(), s.tacles(), s.sauts(), s.verifications()));
    }

    @Test
    void lExecuteurRejoueUnePartieEnregistree() {
        Executeur e = Executeur.executer(Scenario.charger("parties/entrainement.partie"));
        assertEquals(List.of(), e.echecs(), "chaque coup est joué par la bonne équipe, et accepté");
        assertTrue(e.partie().estTerminee());
        assertEquals(Optional.of(Couleur.BLEUS), e.partie().vainqueur());
        assertEquals(Position.of("f1"), e.partie().plateau().ballon(), "le but");
    }

    @Test
    void lExecuteurNoteChaqueEchec() {
        Executeur e = Executeur.executer(Scenario.lire("""
                départ officiel
                bleus poussée c5-d4
                bleus déplacement e5-e4
                rouges saut c2-c4
                rouges déplacement c2-c3
                """));
        assertEquals(List.of(
                "instruction 2 (bleus déplacement e5-e4) : ce n'est pas aux bleus de jouer",
                "instruction 3 (rouges saut c2-c4) : coup refusé"), e.echecs(),
                "un coup refusé est noté, et l'exécution continue : l'instruction 4 est jouée");
        assertEquals(Couleur.BLEUS, e.partie().trait(), "deux coups joués sur quatre");
    }
}
