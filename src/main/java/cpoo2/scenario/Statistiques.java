package cpoo2.scenario;

/** S04, Q3 : combien d'instructions de chaque sorte, sans rien jouer. Sur le modèle d'{@link Afficheur}. */
public final class Statistiques implements VisiteurDeScenario {

    private int deplacements;
    private int poussees;
    private int tacles;
    private int sauts;
    private int verifications;

    public int deplacements() { return deplacements; }
    public int poussees() { return poussees; }
    public int tacles() { return tacles; }
    public int sauts() { return sauts; }
    public int verifications() { return verifications; }

    @Override public void visiterDeplacer(Deplacer d) { throw new UnsupportedOperationException("TODO S04 Q3"); }

    @Override public void visiterPousser(Pousser p) { throw new UnsupportedOperationException("TODO S04 Q3"); }

    @Override public void visiterTacler(Tacler t) { throw new UnsupportedOperationException("TODO S04 Q3"); }

    @Override public void visiterSauter(Sauter s) { throw new UnsupportedOperationException("TODO S04 Q3"); }

    @Override public void visiterVerifier(Verifier v) { throw new UnsupportedOperationException("TODO S04 Q3"); }
}
