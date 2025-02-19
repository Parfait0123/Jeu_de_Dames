public class Position {
    private int ligne;
    private int colonne;

    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public Position() {
        ligne = -1;
        colonne = -1;
    }

    public boolean estDiagonale(Position pos) {
        int coeff=0;
        try {
            coeff=Math.abs((ligne-pos.ligne)/(colonne- pos.colonne));
        } catch (Exception ignored){}
        return (coeff==1);
    }

    public boolean estEnAvantDe(Position pos) {
        return (ligne == pos.ligne+1);
    }

    public Position cloner() {
        return (new Position(ligne, colonne));
    }

    public boolean eguale(Position pos) {
        return (colonne == pos.colonne && ligne == pos.ligne);
    }

}
