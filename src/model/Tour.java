package model;

public class Tour extends AbstractPiece {

	public Tour(Couleur couleur_de_piece, Coord coord) {
		super(couleur_de_piece, coord);
		this.name = "Tour";
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		// On verifie que la tour se deplace en ligne ou en colonne.
		if(this.getX() != xFinal && this.getY() != yFinal ) {
			return false;
		}
		return true;
	}

}
