package cpoo2.jeu;

/** Les huit directions : toute action se fait d'une case à sa voisine. Le nord est vers le rang 6. */
public enum Direction {
    NORD(0, 1), SUD(0, -1), EST(1, 0), OUEST(-1, 0),
    NORD_EST(1, 1), NORD_OUEST(-1, 1), SUD_EST(1, -1), SUD_OUEST(-1, -1);

    private final int dColonne;
    private final int dRangee;

    Direction(int dColonne, int dRangee) {
        this.dColonne = dColonne;
        this.dRangee = dRangee;
    }

    public int dColonne() { return dColonne; }

    public int dRangee() { return dRangee; }
}
