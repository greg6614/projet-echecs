package model;

import java.util.LinkedList;
import java.util.List;

import tools.ChessPiecesFactory;

public class Jeu {
	
	private List<Pieces> listPieces;
	private Couleur couleur;
	private boolean hypotheseRoque;
	private boolean possibleCapture;
	/**
	 * Utilise la fonction statique de ChessPiecesFactory pour creer le jeu
	 * @param couleur
	 */
	public Jeu(Couleur couleur) {
		this.listPieces = ChessPiecesFactory.newPieces(couleur);
		this.couleur = couleur;
		this.hypotheseRoque = false;
		this.possibleCapture = false;
	}

	@Override
	public String toString() {
		return this.listPieces.toString();
	}
	
	/**
	 * 
	 * @return la couleur du jeu
	 */
	public Couleur getCouleur() {
		return this.couleur;
	}
	
	/**
	 * 
	 * @return les coordonnees du roi du jeu
	 */
	public Coord getKingCoord(){
		
		for(Pieces p : this.listPieces) {
			if(p.getClass().getSimpleName().equals("Roi"))
			{
				Coord coord = new Coord(p.getX(), p.getY());
				return coord;
			}
		}
		return null;
	}
	
	/**
	 * Recherche de la piece suivant x et y
	 * @param x
	 * @param y
	 * @return la piece correspondante si elle est trouvee
	 */
	private Pieces findPiece(int x, int y) {
		for(Pieces p : this.listPieces) {
			if(p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		return null;
	}

	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true si le pion peut se deplacer sur la case voulue, false sinon
	 */
	public boolean isMoveDiagOk(int xInit, int yInit, int xFinal, int yFinal) {
		Pion pion = (Pion) this.findPiece(xInit, yInit);
		return pion.isMoveDiagOk(xFinal, yFinal);
	}
	

	/**
	* @return une vue de la liste des pièces en cours
	* ne donnant que des accès en lecture sur des PieceIHM
	* (type piece + couleur + liste de coordonnées)
	*/
	public List<PieceIHM> getPiecesIHM(){
		PieceIHM newPieceIHM = null;
		List<PieceIHM> list = new LinkedList<PieceIHM>();
		for (Pieces piece : this.listPieces) {
			boolean existe = false;
			// si le type de piece existe déjà dans la liste de PieceIHM
			// ajout des coordonnées de la pièce dans la liste de Coord de ce type
			// si elle est toujours en jeu (x et y != -1)
			for ( PieceIHM pieceIHM : list){
				if ((pieceIHM.getTypePiece()).equals(piece.getClass().getSimpleName())){
					existe = true;
					if (piece.getX() != -1){
						pieceIHM.add(new Coord(piece.getX(), piece.getY()));
					}
				}
			}
			// sinon, création d'une nouvelle PieceIHM si la pièce est toujours en jeu
			if (! existe) {
				if (piece.getX() != -1){
					newPieceIHM = new PieceIHM(piece.getClass().getSimpleName(),
					piece.getCouleur());
					newPieceIHM.add(new Coord(piece.getX(), piece.getY()));
					list.add(newPieceIHM);
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return la couleur de la piece si elle existe aux positions x et y, null sinon
	 */
	public Couleur getPieceColor(int x,int y) {
	
		Pieces p = this.findPiece(x, y);
		if(p == null){
			return null;
		}
		return p.getCouleur();
	}
	

	/**
	 * 
	 * @param x
	 * @param y
	 * @return le type de la piece si elle a ete trouvee, renvoie "piece non trouvee" sinon
	 */
	public String getPieceType(int x,int y){
		Pieces p = this.findPiece(x, y);
		if(p == null) {
			return "piece non trouvee";
		}
		return p.getClass().getSimpleName();

	}
	
	/**
	 * 
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return la fonction isMoveOk de la piece concernee
	 */
	public boolean isMoveOk(int xInit,int yInit,int xFinal,int yFinal){
		Pieces p = this.findPiece(xInit, yInit);
		return p.isMoveOk(xFinal, yFinal);
	}
	
	/**
	 * 
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return false si le mouvement n'est pas bon, fait appel a la fonction move de la piece concernee sinon
	 */
	public boolean move(int xInit,int yInit,int xFinal,int yFinal){
		if(this.isMoveOk(xInit,yInit,xFinal,yFinal)) {
			Pieces p = this.findPiece(xInit, yInit);
			return p.move(xFinal,yFinal);
		}
		return false;
	}

	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return false si le mouvement en diagonale n'est pas bon, fait appel a la fonction moveDiag de la piece concernee sinon
	 */
	public boolean moveDiag(int xInit, int yInit, int xFinal, int yFinal) {
		if(this.isMoveDiagOk(xInit, yInit, xFinal, yFinal)) {
			Pion p = (Pion) this.findPiece(xInit, yInit);
			return p.moveDiag(xFinal, yFinal);
		}
		return true;
	}
	
	/**
	 * 
	 * @param xFinal
	 * @param yFinal
	 * @return true si la position finale est au bord du plateau, false sinon
	 */
	public boolean isPawnPromotion(int xFinal, int yFinal) {
		if((xFinal < 0 || xFinal > 7) || (yFinal != 0 && yFinal != 7)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return true si une piece existe aux positions x et y, false sinon
	 */
	public boolean isPieceHere(int x, int y) {
		Pieces p = this.findPiece(x, y);
		if(p == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param xFinal
	 * @param yFinal
	 * @param type
	 * @return true si la promotion est valide, false sinon
	 */
	public boolean pawnPromotion(int xFinal, int yFinal, String type) {
		if(!this.isPawnPromotion(xFinal, yFinal)) {
			return false;
		}
		if(!type.equals("Reine") && !type.equals("Cavalier") && !type.equals("Tour") && !type.equals("Fou")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Met a jour le boolen hypotheseRoque
	 */
	public void setCastling() {
		this.hypotheseRoque = true;
	}
	
	/**
	 * 
	 * @param xCatch
	 * @param yCatch
	 * @return false si la piece n'est pas aux position xCatch et yCatch, fait appel a la fonction capture de la piece sinon
	 */
	public boolean capture(int xCatch, int yCatch) {
		Pieces p = this.findPiece(xCatch, yCatch);
		if(p == null) {
			return false;
		}
		return p.capture();
	}

	/**
	 *
	 * @return la valeur de l'attribut possibleCapture
	 */
	public boolean getPossibleCapture() {
		return this.possibleCapture;
	}
	
	/**
	 * Met a jour le booleen capturePossible
	 */
	public void setPossibleCapture() {
		this.possibleCapture = true;
	}

	/**
	 * Remet a zero le booleen
	 */
	public void resetPossibleCapture() {
		this.possibleCapture = false;
	}
	
	/**
	 * 
	 */
	public void undoMove() {
		
	}
	
	/**
	 * 
	 */
	public void undoCapture() {
		
	}
}
