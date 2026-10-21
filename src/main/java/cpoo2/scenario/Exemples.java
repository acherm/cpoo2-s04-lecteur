package cpoo2.scenario;

/** Fourni : un scénario court, pour les tests. Il joue, et il vérifie. */
public final class Exemples {

    /** La première attaque bleue : trois poussées, un but en g1, et deux vérifications. */
    public static final String PREMIERE_ATTAQUE = """
            # la première attaque bleue : trois poussées, un but
            départ officiel
            bleus poussée c5-d4
            rouges déplacement c2-c3
            vérifier ballon en e3 et trait aux bleus
            bleus poussée d4-e3
            rouges déplacement c3-c4
            bleus poussée e3-f2
            vérifier vainqueur bleus et ballon en g1
            """;

    private Exemples() {}
}
