package cpoo2.scenario;

/**
 * S04, Q7 : l'expérience. « Pourquoi les méthodes {@code accept} sont-elles nécessaires ? »
 *
 * <p>Supposons un visiteur qui, au lieu de cinq noms distincts, <b>surcharge</b> un unique nom
 * {@code visiter}, et un client qui l'appelle directement, sans passer par {@code accept}.
 * Lancez ce {@code main} et lisez la sortie :</p>
 *
 * <pre>java -cp target/classes cpoo2.scenario.SurchargeDemo</pre>
 *
 * <p>Répondez ensuite : à quel moment Java choisit-il une surcharge ?</p>
 */
public final class SurchargeDemo {

    static class VisiteurSurcharge {
        String visiter(Instruction i) { return "visiter(Instruction)"; }
        String visiter(Pousser p) { return "visiter(Pousser)"; }
        String visiter(Verifier v) { return "visiter(Verifier)"; }
    }

    public static void main(String[] args) {
        VisiteurSurcharge v = new VisiteurSurcharge();
        Pousser poussee = (Pousser) PartieEnregistree.instruction("bleus poussée c5-d4");
        Instruction memeObjet = poussee;
        System.out.println("type déclaré Pousser     -> " + v.visiter(poussee));
        System.out.println("type déclaré Instruction -> " + v.visiter(memeObjet));
        System.out.println();
        System.out.println("C'est le même objet dans les deux cas.");
        System.out.println("Que conclure ? Et que change accept() ?");
    }

    private SurchargeDemo() {}
}
