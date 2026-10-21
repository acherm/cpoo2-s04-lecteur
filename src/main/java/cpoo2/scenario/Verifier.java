package cpoo2.scenario;

/** {@code vérifier ballon en e3 et trait aux bleus} : une condition, que l'exécution évalue. */
public record Verifier(Condition condition) implements Instruction {

    @Override public void accept(VisiteurDeScenario visiteur) {
        throw new UnsupportedOperationException("TODO S04 Q1");
    }

    @Override public String toString() { return "vérifier " + condition; }
}
