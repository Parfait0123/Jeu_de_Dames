public class Plateau {
    private Piece[][] plateau;
    private int nbPionsRouges;
    private int nbPionsBleus;

    public Plateau() {
        plateau = new Piece[8][8];
        nbPionsRouges = 12;
        nbPionsBleus = 12;
        initialiserPlateau();
    }

    private void initialiserPlateau() {
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = (ligne % 2); colonne < 8; colonne += 2) {
                plateau[ligne][colonne] = new Piece(Piece.Couleur.BLEU);
            }
        }
        for (int ligne = 5; ligne < 8; ligne++) {
            for (int colonne = (ligne % 2); colonne < 8; colonne += 2) {
                plateau[ligne][colonne] = new Piece(Piece.Couleur.ROUGE);
            }
        }
    }

    public Piece getPiece(Position position) {
        if (position == null) return null;
        else return plateau[position.getLigne()][position.getColonne()];
    }

    public void deplacerPiece(Position depart, Position arrive) {

        Piece piece = PieceCapture(depart, arrive);
        plateau[arrive.getLigne()][arrive.getColonne()] = plateau[depart.getLigne()][depart.getColonne()];
        plateau[depart.getLigne()][depart.getColonne()] = null;
        if (piece != null) {
            if (piece.getCouleur() == Piece.Couleur.BLEU) System.out.println(" Un bleu vient de partir ");
            if (piece.getCouleur() == Piece.Couleur.ROUGE) System.out.println(" Un rouge vient de partir ");
            Position pos = getPosition(piece);
            supprimerPiece(pos.getLigne(), pos.getColonne());

        }

        if ((arrive.getLigne() == 0 && plateau[arrive.getLigne()][arrive.getColonne()].getCouleur() == Piece.Couleur.ROUGE) ||
                (arrive.getLigne() == 7 && plateau[arrive.getLigne()][arrive.getColonne()].getCouleur() == Piece.Couleur.BLEU)) {
            plateau[arrive.getLigne()][arrive.getColonne()].promouvoir();


        }
    }

    public void supprimerPiece(int ligne, int colonne) {
        Piece piece = plateau[ligne][colonne];
        if (piece != null) {
            if (piece.getCouleur() == Piece.Couleur.ROUGE) {
                nbPionsRouges--;
            } else {
                nbPionsBleus--;
            }
            plateau[ligne][colonne] = null;
        }
    }

    public int getNbPionsRouges() {
        return nbPionsRouges;
    }

    public int getNbPionsBleus() {
        return nbPionsBleus;
    }

    public boolean DeplacementValide(Position depart, Position arrive) {
        Piece piece1 = this.getPiece(depart);
        Piece piece2 = this.getPiece(arrive);
        boolean temp1 = piece1 != null && piece2 == null;
        if (!temp1) return false;
        boolean temp2 = false;

        if (piece1.getType() == Piece.Type.PION) {
            if (piece1.getCouleur() == Piece.Couleur.BLEU) {
                temp2 = arrive.estEnAvantDe(depart) || (PieceCapture(depart, arrive) != null);
                return temp2;
            }
        } else {
            System.out.println(" Je suis ici ");

            if (!depart.estDiagonale(arrive)) {
                return false;
            }

            Position caseInferieur = null;
            Position caseSuperieur = null;
            if (depart.getLigne() < arrive.getLigne()) {
                caseInferieur = depart;
                caseSuperieur = arrive;
            } else {
                caseInferieur = arrive;
                caseSuperieur = depart;
            }

            int NbrpieceRouge = 0;
            int NbrpieceBleue = 0;
            for (int i = caseInferieur.getLigne() + 1; i < caseSuperieur.getLigne(); i++) {
                if (caseInferieur.getColonne() > caseSuperieur.getColonne()) {
                    for (int j = caseSuperieur.getColonne() + 1; j < caseInferieur.getColonne(); j++) {
                        if (this.getPiece(new Position(i, j)) != null) {
                            if (this.getPiece(new Position(i, j)).getCouleur() == Piece.Couleur.ROUGE) {
                                ++NbrpieceRouge;
                                System.out.println(plateau[i][j]);
                            }
                            if (this.getPiece(new Position(i, j)).getCouleur() == Piece.Couleur.BLEU) {
                                ++NbrpieceBleue;
                                System.out.println(plateau[i][j]);
                            }
                        }
                    }
                } else {
                    for (int j = caseInferieur.getColonne() + 1; j < caseSuperieur.getColonne(); j++) {
                        if (this.getPiece(new Position(i, j)) != null) {
                            if (this.getPiece(new Position(i, j)).getCouleur() == Piece.Couleur.ROUGE) {
                                ++NbrpieceRouge;
                                System.out.println(plateau[i][j]);
                            }
                            if (this.getPiece(new Position(i, j)).getCouleur() == Piece.Couleur.BLEU) {
                                ++NbrpieceBleue;
                                System.out.println(plateau[i][j]);
                            }
                        }
                    }
                }
            }
            System.out.println(" Rouge : " + NbrpieceRouge + " Bleu :" + NbrpieceBleue);
            if (NbrpieceRouge > 1 || NbrpieceBleue > 1) return false;
            if (NbrpieceRouge == 1 && NbrpieceBleue != 0) return false;
            if ((NbrpieceBleue == 0 && NbrpieceRouge == 0)) return true;
        }
        return true;
    }

    private Piece PieceCapture(Position depart, Position arrive) {
        Piece piece1 = this.getPiece(depart);
        Piece piece2 = this.getPiece(arrive);
        Piece piece3 = null;
        if (piece2 != null || piece1 == null) return null;

        boolean temp = false;
        if (piece1.getType() == Piece.Type.PION) {
            int aSoustraitre;
            if (piece1.getCouleur() == Piece.Couleur.BLEU) {
                aSoustraitre = 1;
            } else {
                aSoustraitre = -1;
            }
            if (Math.abs(depart.getLigne() - arrive.getLigne()) == Math.abs(depart.getColonne() - arrive.getColonne())
                    && Math.abs(depart.getColonne() - arrive.getColonne()) == 2) {
                if (piece1.getCouleur() == Piece.Couleur.BLEU) {
                    if (depart.getLigne() < arrive.getLigne()) {
                        if (depart.getColonne() > arrive.getColonne())
                            piece3 = this.getPiece(new Position(depart.getLigne() + aSoustraitre, depart.getColonne() - 1));
                        else
                            piece3 = this.getPiece(new Position(depart.getLigne() + aSoustraitre, depart.getColonne() + 1));
                    } else {
                        if (depart.getColonne() > arrive.getColonne())
                            piece3 = this.getPiece(new Position(depart.getLigne() - aSoustraitre, depart.getColonne() - 1));
                        else
                            piece3 = this.getPiece(new Position(depart.getLigne() - aSoustraitre, depart.getColonne() + 1));
                    }
                } else {

                }
            } else
                return null;

        } else {

            Position caseInferieur;
            Position caseSuperieur;
            if (depart.getLigne() < arrive.getLigne()) {
                caseInferieur = depart;
                caseSuperieur = arrive;
            } else {
                caseInferieur = arrive;
                caseSuperieur = depart;
            }
            for (int i = caseInferieur.getLigne() + 1; i < caseSuperieur.getLigne(); i++) {
                if (caseInferieur.getColonne() > caseSuperieur.getColonne()) {
                    for (int j = caseInferieur.getColonne() + 1; j > caseSuperieur.getColonne(); j--) {
                        if (getPiece(new Position(i, j)) != null) {
                            piece3 = getPiece(new Position(i, j));
                            break;
                        }
                    }
                } else {
                    for (int j = caseInferieur.getColonne() + 1; j < caseSuperieur.getColonne(); j++) {
                        if (getPiece(new Position(i, j)) != null) {
                            piece3 = getPiece(new Position(i, j));
                            break;
                        }
                    }
                }
            }
        }

        return piece3;
    }

    public Position getPosition(Piece piece) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.getPiece(new Position(i, j)) == piece)
                    return (new Position(i, j));
            }
        }
        return null;
    }

}
