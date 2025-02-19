public class Piece {
    public enum Type { PION, DAME }
    public enum Couleur { ROUGE, BLEU }

    private Type type;
    private Couleur couleur;

    public Piece(Couleur couleur) {
        this.couleur = couleur;
        this.type = Type.PION;
    }

    public Type getType() {
        return type;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void promouvoir() {
        this.type = Type.DAME;
    }
}
