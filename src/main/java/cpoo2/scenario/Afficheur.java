package cpoo2.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Fourni : le scénario, ligne par ligne, tel qu'on l'écrirait. C'est le modèle des Q1 et Q3 : une
 * méthode par sorte d'instruction, un résultat gardé dans un champ, et rendu par un accesseur.
 */
public final class Afficheur implements VisiteurDeScenario {

    private final List<String> lignes = new ArrayList<>();

    /** Les lignes des instructions visitées, une par ligne. */
    public String texte() { return String.join("\n", lignes); }

    @Override public void visiterDeplacer(Deplacer d) { lignes.add(d.toString()); }

    @Override public void visiterPousser(Pousser p) { lignes.add(p.toString()); }

    @Override public void visiterTacler(Tacler t) { lignes.add(t.toString()); }

    @Override public void visiterSauter(Sauter s) { lignes.add(s.toString()); }

    @Override public void visiterVerifier(Verifier v) { lignes.add(v.toString()); }
}
