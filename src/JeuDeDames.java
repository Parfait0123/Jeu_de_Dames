import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class JeuDeDames extends JFrame {
    private Plateau plateau;
    private DamesIA ia;
    private JPanel plateauPanel;
    private JButton[][] cases;
    private JLabel statusLabel;
    private Position positionInitiale;
    private boolean tourJoueur;

    public JeuDeDames() {
        plateau = new Plateau();
        ia = new DamesIA();
        positionInitiale = null;
        tourJoueur = true;
        initialiserInterfaceGraphique();
    }

    private void initialiserInterfaceGraphique() {
        setTitle("Jeu de Dames");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        plateauPanel = new JPanel();
        plateauPanel.setLayout(new GridLayout(8, 8));
        cases = new JButton[8][8];

        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                int finalLigne = ligne;
                int finalColonne = colonne;
                cases[ligne][colonne] = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Piece piece = plateau.getPiece(new Position(finalLigne, finalColonne));
                        if (piece != null) {
                            Graphics2D graph = (Graphics2D) g;
                            if (piece.getType() == Piece.Type.DAME) {
                                if (piece.getCouleur() == Piece.Couleur.ROUGE)
                                    graph.setPaint(new GradientPaint(0, 0, Color.RED, 5, 5, Color.YELLOW, true));
                                else
                                    graph.setPaint(new GradientPaint(0, 0, Color.BLUE, 5, 5, Color.YELLOW, true));
                            } else {
                                if (piece.getCouleur() == Piece.Couleur.ROUGE) {
                                    g.setColor(Color.RED);
                                } else {
                                    g.setColor(Color.BLUE);
                                }
                            }
                            int a = this.getWidth();
                            int b = this.getHeight();
                            g.fillOval(a / 10, b / 10, a - a / 5, b - b / 5);
                        }
                    }
                };
                if ((ligne + colonne) % 2 == 0) {
                    cases[ligne][colonne].setBackground(Color.WHITE);
                } else {
                    cases[ligne][colonne].setBackground(Color.DARK_GRAY);
                }
                if ((ligne + colonne) % 2 == 0)
                    cases[ligne][colonne].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JButton bouton = (JButton) e.getSource();
                            Position position = new Position();
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    if (cases[i][j] == bouton) {
                                        position = new Position(i, j);
                                        break;
                                    }
                                }
                            }
                            gererClic(position);
                        }
                    });
                plateauPanel.add(cases[ligne][colonne]);
            }
        }
        ajouterPieces();

        statusLabel = new JLabel("Tours : 1 | Pions Bleus : 12 | Pions Rouges : 12 | Tour : Bleu");
        add(statusLabel, BorderLayout.SOUTH);
        add(plateauPanel, BorderLayout.CENTER);
    }

    private void ajouterPieces() {
        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                cases[ligne][colonne].repaint();
            }
        }
    }

    private void gererClic(Position position) {
        if (tourJoueur) {
            if (positionInitiale == null) {
                positionInitiale = position;
            } else {
                if (plateau.getPiece(position) != null) {
                    positionInitiale = position;
                } else {

                    if (positionInitiale.estDiagonale(position) && plateau.DeplacementValide(positionInitiale, position)) {
                        plateau.deplacerPiece(positionInitiale, position);
                        ajouterPieces();
                        tourJoueur = false;
                        Timer timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        tourIA();
                                    }
                                }, 1500);
                        positionInitiale = null;
                    }

                }
            }


        }

    }

    private void tourIA() {

        int[] mouvementIA = ia.obtenirMouvement(plateau, Piece.Couleur.ROUGE);
        plateau.deplacerPiece(new Position(mouvementIA[0], mouvementIA[1]), new Position(mouvementIA[2], mouvementIA[3]));
        ajouterPieces();
        mettreAJourStatus();
        tourJoueur = true;
    }

    private void mettreAJourStatus() {
        statusLabel.setText(" Pions Bleus : " + plateau.getNbPionsBleus() + " | Pions Rouges : " + plateau.getNbPionsRouges() + " | Tour : " + (tourJoueur ? "Bleu" : "Rouge"));
    }


    private void afficherMessageVictoire(String couleur) {
        JOptionPane.showMessageDialog(this, "Le joueur " + couleur + " a gagné !", "Victoire", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JeuDeDames().setVisible(true);
            }
        });
    }
}
