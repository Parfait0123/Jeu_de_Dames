import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamesIA {
    private Random random = new Random();

    public int[] obtenirMouvement(Plateau plateau, Piece.Couleur couleur) {
        List<int[]> mouvements = obtenirTousLesMouvementsPossibles(plateau, couleur);
        return mouvements.get(random.nextInt(mouvements.size()));
    }

    private List<int[]> obtenirTousLesMouvementsPossibles(Plateau plateau, Piece.Couleur couleur) {
        List<int[]> mouvements = new ArrayList<>();
        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                Piece piece = plateau.getPiece(new Position(ligne, colonne));
                if (piece != null && piece.getCouleur() == couleur) {
                    List<int[]> mouvementsPiece = obtenirMouvementsPossiblesPourPiece(plateau, ligne, colonne);
                    mouvements.addAll(mouvementsPiece);
                }
            }
        }
        return mouvements;
    }

    private List<int[]> obtenirMouvementsPossiblesPourPiece(Plateau plateau, int ligne, int colonne) {
        List<int[]> mouvements = new ArrayList<>();
        Piece piece = plateau.getPiece(new Position(ligne, colonne));
        int direction = (piece.getCouleur() == Piece.Couleur.ROUGE) ? -1 : 1;

        ajouterMouvementSiValide(plateau, mouvements, ligne, colonne, ligne + direction, colonne + 1);
        ajouterMouvementSiValide(plateau, mouvements, ligne, colonne, ligne + direction, colonne - 1);
        if (piece.getType() == Piece.Type.DAME) {
            ajouterMouvementSiValide(plateau, mouvements, ligne, colonne, ligne - direction, colonne + 1);
            ajouterMouvementSiValide(plateau, mouvements, ligne, colonne, ligne - direction, colonne - 1);
        }

        return mouvements;
    }

    private void ajouterMouvementSiValide(Plateau plateau, List<int[]> mouvements, int deLigne, int deColonne, int aLigne, int aColonne) {
        if (aLigne >= 0 && aLigne < 8 && aColonne >= 0 && aColonne < 8 && plateau.getPiece(new Position(aLigne, aColonne)) == null) {
            mouvements.add(new int[]{deLigne, deColonne, aLigne, aColonne});
        }
    }
}
