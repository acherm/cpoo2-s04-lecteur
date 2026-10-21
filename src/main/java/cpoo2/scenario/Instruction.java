package cpoo2.scenario;

/**
 * Une ligne d'un scénario ou d'une partie enregistrée : un coup joué par une équipe, ou une
 * vérification. Les opérations sur une partie (la résumer, la rejouer, l'afficher) ne vivent pas
 * dans les instructions, mais dans des visiteurs ({@link VisiteurDeScenario}).
 */
public sealed interface Instruction permits Deplacer, Pousser, Tacler, Sauter, Verifier {

    /** Q1 : une ligne, et une seule, dans chaque classe concrète. */
    void accept(VisiteurDeScenario visiteur);
}
