package cpoo2.jeu;

/**
 * Les deux camps. Les Bleus partent du haut (rangs 5 et 6) et marquent sur le rang 1, les Rouges
 * partent du bas et marquent sur le rang 6. Les Bleus jouent en premier.
 */
public enum Couleur {
    BLEUS, ROUGES;

    public Couleur adverse() { return this == BLEUS ? ROUGES : BLEUS; }

    /** La rangée (0..5) où cette équipe marque : la ligne de but adverse. */
    public int rangeeOuElleMarque() { return this == BLEUS ? 0 : Position.RANGEES - 1; }

    /** La rangée (0..5) de sa propre ligne de but : y pousser le ballon, c'est marquer contre son camp. */
    public int rangeeQuElleDefend() { return this == BLEUS ? Position.RANGEES - 1 : 0; }
}
