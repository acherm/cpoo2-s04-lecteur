package cpoo2.jeu;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Une position figée : où sont les pièces, où est le ballon. <b>Immuable</b> : le lire ne change
 * rien, le donner à qui que ce soit ne risque rien.
 *
 * <p>Une case contient une pièce, ou le ballon, ou rien. Elle est <b>libre</b> quand elle ne
 * contient ni pièce ni ballon : on n'entre sur la case du ballon qu'en le poussant.</p>
 */
public final class Plateau {

    private final Map<Position, Piece> pieces;
    private final Position ballon;

    private Plateau(Map<Position, Piece> pieces, Position ballon) {
        this.pieces = Collections.unmodifiableMap(new LinkedHashMap<>(pieces));
        this.ballon = ballon;
    }

    /** La position de départ de la boîte : défenseurs sur les lignes de but, ballon en d4. */
    public static Plateau officiel() {
        return depuis(
                ".D.D.D.",   // rang 6
                "..A.A..",   // rang 5
                "...o...",   // rang 4
                ".......",   // rang 3
                "..a.a..",   // rang 2
                ".d.d.d.");  // rang 1
    }

    /**
     * Un plateau écrit comme on le lit : six lignes de sept caractères, du rang 6 (en haut) au
     * rang 1 (en bas). {@code A} et {@code D} pour les Bleus, {@code a} et {@code d} pour les
     * Rouges, {@code o} pour le ballon, {@code .} pour une case vide.
     */
    public static Plateau depuis(String... rangs) {
        if (rangs.length != Position.RANGEES) {
            throw new IllegalArgumentException(Position.RANGEES + " rangs attendus, du rang 6 au rang 1");
        }
        Map<Position, Piece> pieces = new LinkedHashMap<>();
        Position ballon = null;
        for (int i = 0; i < rangs.length; i++) {
            int rangee = Position.RANGEES - 1 - i;
            if (rangs[i].length() != Position.COLONNES) {
                throw new IllegalArgumentException("rang " + (rangee + 1) + " : sept cases attendues");
            }
            for (int colonne = 0; colonne < Position.COLONNES; colonne++) {
                Position p = new Position(colonne, rangee);
                switch (rangs[i].charAt(colonne)) {
                    case '.' -> { }
                    case 'o' -> {
                        if (ballon != null) throw new IllegalArgumentException("un seul ballon");
                        ballon = p;   // en zone de touche aussi : c'est une règle du jeu, l'arbitre l'applique, une variante la lève
                    }
                    case 'A' -> pieces.put(p, new Piece(Couleur.BLEUS, TypePiece.ATTAQUANT));
                    case 'D' -> pieces.put(p, new Piece(Couleur.BLEUS, TypePiece.DEFENSEUR));
                    case 'a' -> pieces.put(p, new Piece(Couleur.ROUGES, TypePiece.ATTAQUANT));
                    case 'd' -> pieces.put(p, new Piece(Couleur.ROUGES, TypePiece.DEFENSEUR));
                    default -> throw new IllegalArgumentException("symbole inconnu en " + p + " : " + rangs[i].charAt(colonne));
                }
            }
        }
        if (ballon == null) throw new IllegalArgumentException("pas de ballon");
        return new Plateau(pieces, ballon);
    }

    public Optional<Piece> pieceEn(Position p) { return Optional.ofNullable(pieces.get(p)); }

    public Position ballon() { return ballon; }

    /** Ni pièce ni ballon. */
    public boolean estLibre(Position p) { return !pieces.containsKey(p) && !ballon.equals(p); }

    /** Les pièces et leurs cases, en lecture seule. */
    public Map<Position, Piece> pieces() { return pieces; }

    /** Le plateau en une ligne, les six rangs du rang 6 au rang 1 séparés par {@code /}. */
    public String enLigne() {
        StringBuilder sb = new StringBuilder();
        for (int r = Position.RANGEES - 1; r >= 0; r--) {
            for (int c = 0; c < Position.COLONNES; c++) {
                Position p = new Position(c, r);
                sb.append(p.equals(ballon) ? 'o' : pieceEn(p).map(Piece::symbole).orElse('.'));
            }
            if (r > 0) sb.append('/');
        }
        return sb.toString();
    }

    /** L'inverse de {@link #enLigne()}. */
    public static Plateau depuisLigne(String ligne) {
        return depuis(ligne.split("/"));
    }

    /** Le plateau tel qu'on le lit, rang 6 en haut, avec les coordonnées. */
    public String enTexte() {
        StringBuilder sb = new StringBuilder();
        for (int r = Position.RANGEES - 1; r >= 0; r--) {
            sb.append(r + 1);
            for (int c = 0; c < Position.COLONNES; c++) {
                Position p = new Position(c, r);
                char s = p.equals(ballon) ? 'o' : pieceEn(p).map(Piece::symbole).orElse('.');
                sb.append(' ').append(s);
            }
            sb.append('\n');
        }
        return sb.append("  a b c d e f g\n").toString();
    }

    @Override public boolean equals(Object o) {
        return o instanceof Plateau autre && pieces.equals(autre.pieces) && ballon.equals(autre.ballon);
    }

    @Override public int hashCode() { return 31 * pieces.hashCode() + ballon.hashCode(); }

    @Override public String toString() { return enTexte(); }
}
