package cpoo2.jeu;

/** Une pièce : son camp et sa sorte. C'est le plateau qui sait où elle est. */
public record Piece(Couleur couleur, TypePiece type) {

    /** Le symbole du plateau en texte : A et D pour les Bleus, a et d pour les Rouges. */
    public char symbole() {
        char c = type == TypePiece.ATTAQUANT ? 'A' : 'D';
        return couleur == Couleur.BLEUS ? c : Character.toLowerCase(c);
    }
}
