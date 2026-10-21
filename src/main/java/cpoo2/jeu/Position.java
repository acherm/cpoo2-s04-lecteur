package cpoo2.jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Une case du plateau 7 × 6 : colonne 0..6 (a..g), rangée 0..5 (rang 1 en bas, rang 6 en haut).
 * {@code Position.of("d4")} est la colonne 3, la rangée 3.
 */
public record Position(int colonne, int rangee) {

    public static final int COLONNES = 7;
    public static final int RANGEES = 6;

    private static final List<Position> TOUTES = new ArrayList<>();
    static {
        for (int r = 0; r < RANGEES; r++) {
            for (int c = 0; c < COLONNES; c++) {
                TOUTES.add(new Position(c, r));
            }
        }
    }

    public Position {
        if (colonne < 0 || colonne >= COLONNES || rangee < 0 || rangee >= RANGEES) {
            throw new IllegalArgumentException("hors plateau : colonne " + colonne + ", rangée " + rangee);
        }
    }

    /** {@code Position.of("d4")}. */
    public static Position of(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("notation attendue de a1 à g6 : " + notation);
        }
        return new Position(notation.charAt(0) - 'a', notation.charAt(1) - '1');
    }

    /** Les 42 cases, de a1 à g6, rang par rang. */
    public static List<Position> toutes() { return Collections.unmodifiableList(TOUTES); }

    /** La voisine dans cette direction, ou vide si elle sort du plateau. */
    public Optional<Position> voisine(Direction d) {
        int c = colonne + d.dColonne();
        int r = rangee + d.dRangee();
        if (c < 0 || c >= COLONNES || r < 0 || r >= RANGEES) {
            return Optional.empty();
        }
        return Optional.of(new Position(c, r));
    }

    /** Colonnes a et g, rangs 2 à 5 : le ballon n'y va jamais, les pièces si. */
    public boolean estZoneDeTouche() {
        return (colonne == 0 || colonne == COLONNES - 1) && rangee >= 1 && rangee <= RANGEES - 2;
    }

    @Override public String toString() {
        return "" + (char) ('a' + colonne) + (char) ('1' + rangee);
    }
}
