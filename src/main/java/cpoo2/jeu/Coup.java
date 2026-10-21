package cpoo2.jeu;

import java.util.Optional;

/**
 * Les quatre actions de ChessBall. Chacune part d'une case, dans une direction : {@code q1} est la
 * voisine dans cette direction, {@code q2} celle d'après.
 *
 * <p>Notation (celle du kit officiel) : la sorte du coup, puis la case de départ et la case
 * d'arrivée <b>de la pièce qui agit</b>. {@code "poussée d4-d5"} : la pièce va de d4 en d5, et le
 * ballon, qui était en d5, part en d6.</p>
 */
public sealed interface Coup {

    Position origine();

    Direction direction();

    /** La case où arrive la pièce qui agit : {@code q1}, ou {@code q2} pour un saut. */
    default Position arrivee() {
        return origine().voisine(direction()).orElseThrow();
    }

    /** Toute pièce : vers {@code q1}, si elle est libre. */
    record Deplacement(Position origine, Direction direction) implements Coup {
        @Override public String toString() { return "déplacement " + origine + "-" + arrivee(); }
    }

    /** Toute pièce : le ballon est en {@code q1}, il part en {@code q2}, la pièce prend sa place. */
    record Poussee(Position origine, Direction direction) implements Coup {
        /** La case où s'arrête le ballon, {@code q2}. */
        public Optional<Position> arriveeDuBallon() {
            return origine.voisine(direction).flatMap(q1 -> q1.voisine(direction));
        }

        @Override public String toString() { return "poussée " + origine + "-" + arrivee(); }
    }

    /** Défenseur seulement : l'adversaire en {@code q1} est repoussé en {@code q2}. */
    record Tacle(Position origine, Direction direction) implements Coup {
        @Override public String toString() { return "tacle " + origine + "-" + arrivee(); }
    }

    /** Attaquant seulement : par-dessus {@code q1} occupée, jusqu'en {@code q2}. */
    record Saut(Position origine, Direction direction) implements Coup {
        @Override public Position arrivee() {
            return origine.voisine(direction).flatMap(q1 -> q1.voisine(direction)).orElseThrow();
        }

        @Override public String toString() { return "saut " + origine + "-" + arrivee(); }
    }

    /** L'inverse de {@code toString} : {@code Coup.lire("poussée d4-d5")}. */
    static Coup lire(String texte) {
        String[] mots = texte.trim().split(" ");
        String[] cases = mots.length == 2 ? mots[1].split("-") : new String[0];
        if (cases.length != 2) {
            throw new IllegalArgumentException("coup attendu comme « poussée d4-d5 » : " + texte);
        }
        Position de = Position.of(cases[0]);
        Position vers = Position.of(cases[1]);
        int dc = vers.colonne() - de.colonne();
        int dr = vers.rangee() - de.rangee();
        boolean saut = mots[0].equals("saut");
        int pas = saut ? 2 : 1;
        Direction direction = null;
        for (Direction d : Direction.values()) {
            if (d.dColonne() * pas == dc && d.dRangee() * pas == dr) direction = d;
        }
        if (direction == null) {
            throw new IllegalArgumentException("de " + de + " à " + vers + " : ce n'est pas un " + mots[0]);
        }
        return switch (mots[0]) {
            case "déplacement" -> new Deplacement(de, direction);
            case "poussée" -> new Poussee(de, direction);
            case "tacle" -> new Tacle(de, direction);
            case "saut" -> new Saut(de, direction);
            default -> throw new IllegalArgumentException("sorte de coup inconnue : " + mots[0]);
        };
    }
}
