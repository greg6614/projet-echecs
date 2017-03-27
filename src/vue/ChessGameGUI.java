package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import tools.ChessImageProvider;
import tools.ChessPiecePos;
import model.Coord;
import model.Couleur;
import model.PieceIHM;
import controler.ChessGameControlers;
import controler.controlerLocal.ChessGameControler;

public class ChessGameGUI extends JFrame implements MouseListener, MouseMotionListener, Observer {

    private JLayeredPane layeredPane;
    private JPanel chessBoard;
    private JLabel chessPiece;
    private int xAdjustment;
    private int yAdjustment;
    private ChessGameControlers chessGameControler;
    private int xInit;
    private int yInit;
    private Dimension dim;
    private List<PieceIHM> listPiecesIHM;

    public ChessGameGUI(String name, ChessGameControlers chessGameControler, Dimension boardSize){
 
        //  Definition l'element principal de notre affichage graphique
        this.layeredPane = new JLayeredPane();
        getContentPane().add(this.layeredPane);
        this.layeredPane.setPreferredSize(boardSize);
        this.layeredPane.addMouseListener(this);
        this.layeredPane.addMouseMotionListener(this);
        
        // Recuperation des infos du controleur
        this.chessGameControler = chessGameControler;
        
        // Recuperation des infos sur la dimension
        this.dim = boardSize;

        // Creation du damier de notre jeu
        this.chessBoard = new JPanel();
        this.layeredPane.add(this.chessBoard, JLayeredPane.DEFAULT_LAYER);
        this.chessBoard.setLayout( new GridLayout(8, 8) );
        this.chessBoard.setPreferredSize( boardSize );
        this.chessBoard.setBounds(0, 0, this.dim.width, this.dim.height);
        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel( new BorderLayout() );
            this.chessBoard.add( square );
            /* Creation des carres du plateau */
            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground( i % 2 == 0 ? Color.BLACK : Color.WHITE );
            else
                square.setBackground( i % 2 == 0 ? Color.WHITE : Color.BLACK );
        }

    }
    
    /**
     * 
     * @return la map faisant correspondre les indices des cases du plateau aux coordonnees x et y des pieces
     */
    private Map<Coord, Integer> setMap() {
        int x, y;
		Map<Coord, Integer> m = new HashMap<Coord, Integer>();
        for (int i = 0; i < 64; i++) {
			x = i % 8;
			y = (i-x)/8;
			m.put(new Coord(x, y), i);
        }
        return m;
    }

	/**
	 * Ajoute les pieces sur le plateau suivant leur position donnee par la liste de pieces IHM
	 */
	private void setPieces() {
    	Map<Coord, Integer> m = this.setMap();
        /* Recuperation des donnees des pieces et ajout sur le plateau */
		for(PieceIHM p : this.listPiecesIHM) {
			String type = p.getTypePiece();
			Couleur couleur = p.getCouleur();
			for(Coord coord : p.getList()) {
				int valMap = m.get(coord);
				JLabel piece;
				JPanel panel;
				String path = ChessImageProvider.getImageFile(type, couleur);
				piece = new JLabel(new ImageIcon(path));
				panel = (JPanel)this.chessBoard.getComponent(valMap);
				panel.add(piece);
			}
		}
    }
    
    /**
     * Supprime toute les pieces du plateau
     */
    private void cleanPlateau() {
    	JPanel panel;
    	for(int i = 0; i < 64; i++) {
        	panel = (JPanel)this.chessBoard.getComponent(i);
        	panel.removeAll();
    	}
    }
    
    /**
     * Rafraichit l'affichage a chaque fois qu'un mouvement a ete effectue
     */
	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(this.chessGameControler.getMessage() + "\n");	
		this.listPiecesIHM = (List<PieceIHM>) arg1;
		this.cleanPlateau();
		this.setPieces();	
	}

	/**
	 * Permet de deplacer la piece selectionnee
	 * @param arg0
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(this.chessPiece == null) return;
		this.chessPiece.setLocation(arg0.getX() + this.xAdjustment, arg0.getY() + this.yAdjustment);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	/**
	 *
	 * @param val
	 * @return la valeur de notre indice x ou y suivant la valeur en pixels donnee en parametre
	 */
	private int tradPixelsToIndex(int valPixels) {
		return (int) (valPixels/(this.dim.width/8.0));
	}

	/**
	 * Recupere la piece selectionnee
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		this.chessPiece = null;
		Component comp = this.chessBoard.findComponentAt(e.getX(), e.getY());
		if(comp instanceof JPanel) return;
		Point parentLocation = comp.getParent().getLocation();
		this.xAdjustment = parentLocation.x - e.getX();
		this.yAdjustment = parentLocation.y - e.getY();
		this.chessPiece = (JLabel) comp;
		this.chessPiece.setLocation(parentLocation.x, parentLocation.y);
		this.chessPiece.setSize(this.chessPiece.getWidth(), this.chessPiece.getHeight());
		this.layeredPane.add(this.chessPiece, this.layeredPane.DRAG_LAYER);
		this.xInit = this.tradPixelsToIndex(e.getX());
		this.yInit = this.tradPixelsToIndex(e.getY());
	}

	/**
	 * Bouge la piece selectionnee si possible, repositionne les pieces suivant la liste donnee sinon.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
        if(this.chessPiece == null) return;
        this.chessPiece.setVisible(false);
        int xFinal = this.tradPixelsToIndex(e.getX());
        int yFinal = this.tradPixelsToIndex(e.getY());
        boolean ret = this.chessGameControler.move(new Coord(this.xInit, this.yInit), new Coord(xFinal, yFinal));
        // Si le deplacement n'a pas pu etre effectue, on realise deux actions supplementaires pour rafraichir l'affichage
    	if(!ret) {
            this.cleanPlateau();
        	this.setPieces();
    	}
	}

}
