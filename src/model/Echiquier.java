package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Echiquier implements BoardGames {
	
	private String message;
	private Jeu jeuBlanc;
	private Jeu jeuNoir;
	private boolean switchTour;
	private Map<Couleur,Jeu> jeux = new HashMap<Couleur, Jeu>();
	
	public Echiquier() {
		this.setMessage("Bienvenue");
		this.jeuBlanc = new Jeu(Couleur.BLANC);
		jeux.put(Couleur.BLANC, jeuBlanc);
		this.jeuNoir = new Jeu(Couleur.NOIR);
		jeux.put(Couleur.NOIR, jeuNoir);
		this.switchTour = false;
	}

	@Override
	public boolean isEnd() {
		return true;
	}
	
	/**
	 * Setter de l'attribut prive message
	 * @param message
	 */
	private void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public Couleur getColorCurrentPlayer() {
		if(this.switchTour) {
			return Couleur.NOIR;
		}
		return Couleur.BLANC;
	}

	@Override
	public Couleur getPieceColor(int x, int y) {
		if(this.jeuBlanc.isPieceHere(x, y)) {
			return this.jeuBlanc.getPieceColor(x, y);
		}
		if(this.jeuNoir.isPieceHere(x, y)) {
			return this.jeuNoir.getPieceColor(x, y);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Jeu blanc:\n" + this.jeuBlanc.toString() + "\n" +
				"Jeu noir:\n" + this.jeuNoir.toString();
	}
	
	/**
	 * Change de joueur
	 */
	public void switchJoueur() {
		this.switchTour = !this.switchTour;
	}
	
	/**
	 *
	 * @param x
	 * @param y
	 * @return true si la couleur de la piece est la meme que la couleur du joueur
	 */
	private boolean isSameColorAsPlayer(int x, int y) {
		return(this.getColorCurrentPlayer() == this.getPieceColor(x, y));
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return true si le roi de la couleur du joueur courant est sur les coordonnees x et y, false sinon
	 */
	private boolean isCurrentColorKing(int x, int y) {
		return this.jeux.get(this.getColorCurrentPlayer()).getKingCoord().equals(new Coord(x, y));
	}
	
	/**
	 * Fait l'hypothese du roque du roi de la couleur courante
	 */
	private void setCastling() {
		this.jeux.get(this.getColorCurrentPlayer()).setCastling();
	}
	
	/**
	 * teste si :
	 *  - la piece existe sur les coordonnees xInit et yInit
	 *  - les coordonnees initiales 
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true si le deplacement est faisable
	 */
	public boolean isMoveOk(int xInit,int yInit,int xFinal,int yFinal){
		if(!this.isPieceHere(xInit, yInit)) {
			this.setMessage("KO: Pièce non présente a l'emplacement");
			return false;
		}

		if((xInit == xFinal && yInit == yFinal) || this.outOfBoard(xFinal, yFinal)) {
			this.setMessage("KO: Coordonnées finales incorrectes");
			return false;
		}
		if(!this.isSameColorAsPlayer(xInit, yInit)) {
			this.setMessage("KO: Pièce de la mauvaise couleur");
			return false;
		}
		Jeu jeuCourant = this.jeux.get(this.getColorCurrentPlayer());
		if(jeuCourant.getPieceType(xInit, yInit).equals("Pion")) {
			return this.isMovePionOk(xInit, yInit, xFinal, yFinal);
		}
		if(!jeuCourant.isMoveOk(xInit, yInit, xFinal, yFinal)) {
			this.setMessage("KO: Position finale ne correspond pas à l'algo de déplacement légal de la pièce");
			return false;
		}
		if(this.isPieceInter(jeuCourant, xInit, yInit, xFinal, yFinal))
		{
			this.setMessage("KO: Il y a au moins une pièce intermédiaire");
			return false;
		}
		if(this.isPieceHere(xFinal, yFinal)) {
			if(this.isSameColorAsPlayer(xFinal, yFinal)) {
				if(this.isCurrentColorKing(xInit, yInit)) {
					this.setCastling();
					this.setMessage("OK: Hypothese de roque du roi");
					return true;
				}
				this.setMessage("KO: Pièce à la position finale de même couleur");
				return false;
			}
			this.setMessage("OK: déplacement + capture");
			this.setPossibleCapture();
			return true;
		}
		this.setMessage("OK: déplacement simple");
		return true;
	}

	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true si le pion peut se deplacer en ligne ou en diagonale, false sinon
	 */
	private boolean isMovePionOk(int xInit, int yInit, int xFinal, int yFinal) {
		Jeu jeuCourant = this.jeux.get(this.getColorCurrentPlayer());
		if(!jeuCourant.getPieceType(xInit, yInit).equals("Pion")) {
			return false;
		}
		if(this.isPieceInter(jeuCourant, xInit, yInit, xFinal, yFinal))
		{
			this.setMessage("KO: Il y a au moins une pièce intermédiaire");
			return false;
		}
		if(this.isPieceHere(xFinal, yFinal)) {
			if(this.isSameColorAsPlayer(xFinal, yFinal)) {
				this.setMessage("KO: Pièce à la position finale de même couleur");
				return false;
			}
			if(jeuCourant.isMoveDiagOk(xInit, yInit, xFinal, yFinal)) {
				this.setMessage("OK: déplacement + capture");
				this.setPossibleCapture();
				return true;
			}
			this.setMessage("KO: mauvais déplacement avec une pièce à la position finale");
			return false;
		}
		if(!jeuCourant.isMoveOk(xInit, yInit, xFinal, yFinal)) {
			this.setMessage("KO: Position finale ne correspond pas à l'algo de déplacement légal de la pièce");
			return false;
		}
		this.setMessage("OK: déplacement simple");
		return true;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @return true si les valeurs de x et de y ne sont pas dans le plateau de jeu
	 */
	private boolean outOfBoard(int x, int y) {
		if((x < 0 || x > 7) || (y < 0 || y > 7)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return true si une piece est presente aux coordonnees x et y, false sinon
	 */
	private boolean isPieceHere(int x, int y) {
		if(this.jeuBlanc.isPieceHere(x, y) || this.jeuNoir.isPieceHere(x, y)) {
			return true;
		}
		return false;
	}

	/**
	 * Fait appel a la fonction isPieceInter de la piece qu'on souhaite deplacer
	 * @param jeu
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true s'il y a une piece intermediaire sur le chemin, false sinon ou si la piece n'est pas concerne par ce test
	 */
	private boolean isPieceInter(Jeu jeu, int xInit, int yInit, int xFinal, int yFinal){
		switch(jeu.getPieceType(xInit, yInit)){
		case "Tour":
			return isPieceInterTour(xInit, yInit, xFinal, yFinal);
		case "Fou":
			return isPieceInterFou(xInit, yInit, xFinal, yFinal);
		case "Reine":
			return isPieceInterReine(xInit, yInit, xFinal, yFinal);
		case "Pion":
			return isPieceInterPion(xInit, yInit, xFinal, yFinal);
		default:
			return false;
		}
	}
	
	/**
	 * Modifie l'attribut capturePossible de la classe Jeu du joueur courant
	 */
	private void setPossibleCapture() {
		this.jeux.get(this.getColorCurrentPlayer()).setPossibleCapture();
	}
	
	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true s'il y a une piece intermediaire sur le chemin d'une piece de type Tour
	 */
	private boolean isPieceInterTour(int xInit, int yInit, int xFinal, int yFinal){
		int j,k;
		if(xInit == xFinal) {
			j=Math.abs(yInit-yFinal);
		} else {
			j=Math.abs(xInit-xFinal);
		}
		k=1;
		while(k<j){
			if(xInit == xFinal)
			{
				if(yInit>yFinal) {
					if(this.isPieceHere(xInit, yInit-k)) {
						return true;
					}
				} else {
					if(this.isPieceHere(xInit, yInit+k)) {
						return true;
					}
				}
			}
			else {
				if(xInit>xFinal) {
					if(this.isPieceHere(xInit-k, yInit)) {
						return true;
					}
				} else {
					if(this.isPieceHere(xInit+k, yInit)) {
						return true;
					}
				}
			}
			k++;
		}
		return false;
	}
	
	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true s'il y a une piece intermediaire sur le chemin d'une piece de type Fou
	 */
	private boolean isPieceInterFou(int xInit, int yInit, int xFinal, int yFinal){
		int i = Math.abs(xInit - xFinal);
		int j = 1;
		while(j<i) {
			if((xInit>xFinal) && (yInit>yFinal)) {
				if(this.isPieceHere(xInit-j, yInit-j)) {
					return true;
				}
			}
			if((xInit<xFinal) && (yInit>yFinal)) {
				if(this.isPieceHere(xInit+j, yInit-j)) {
					return true;
				}
			}
			if((xInit>xFinal) && (yInit<yFinal)) {
				if(this.isPieceHere(xInit-j, yInit+j)) {
					return true;
				}
			}
			if((xInit<xFinal) && (yInit<yFinal)) {
				if(this.isPieceHere(xInit+j, yInit+j)) {
					return true;
				}
			}
			j++;
		}
		return false;
	}
	
	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true s'il y a une piece intermediaire sur le chemin d'une piece de type Reine
	 */
	private boolean isPieceInterReine(int xInit, int yInit, int xFinal, int yFinal){
		if((xInit == xFinal)||(yInit == yFinal) ){
			return isPieceInterTour(xInit, yInit, xFinal, yFinal);
		}
		else{
			return isPieceInterFou(xInit, yInit, xFinal, yFinal);
		}
	}
	
	/**
	 *
	 * @param xInit
	 * @param yInit
	 * @param xFinal
	 * @param yFinal
	 * @return true s'il y a une piece intermediaire sur le chemin d'une piece de type Pion
	 */
	private boolean isPieceInterPion(int xInit, int yInit, int xFinal, int yFinal){
		if(Math.abs(yFinal-yInit) == 2){
			return this.isPieceHere(xInit,(yInit+yFinal)/2);
		}
		return false;
	}
	
	@Override
	public boolean move(int xInit, int yInit, int xFinal, int yFinal) {
		if(!this.isMoveOk(xInit, yInit, xFinal, yFinal)) {
			return false;
		}
		switch(this.getColorCurrentPlayer()) {
			case BLANC:
				if(this.jeuBlanc.getPossibleCapture()) {
					this.jeuNoir.capture(xFinal, yFinal);
					this.jeuBlanc.resetPossibleCapture();
					if(this.jeuBlanc.getPieceType(xInit, yInit).equals("Pion")) {
						return this.jeuBlanc.moveDiag(xInit, yInit, xFinal, yFinal);
					}
				}
				return this.jeuBlanc.move(xInit, yInit, xFinal, yFinal);
			case NOIR:
				if(this.jeuNoir.getPossibleCapture()) {
					this.jeuBlanc.capture(xFinal, yFinal);
					this.jeuNoir.resetPossibleCapture();
					if(this.jeuNoir.getPieceType(xInit, yInit).equals("Pion")) {
						return this.jeuNoir.moveDiag(xInit, yInit, xFinal, yFinal);
					}
				}
				return this.jeuNoir.move(xInit, yInit, xFinal, yFinal);
			default:
				return false;
		}
	}
	
	/**
	 * @return la liste des pieces de l'echiquier
	 */
	public List<PieceIHM> getPiecesIHM() {
		List<PieceIHM> list = new LinkedList<PieceIHM>();
		list.addAll(this.jeuBlanc.getPiecesIHM());
		list.addAll(this.jeuNoir.getPiecesIHM());
		return list;
	}
	

}
