package model;

public class Cavalier extends AbstractPiece {

	public Cavalier(Couleur couleur, Coord coord) {
		super(couleur, coord);
		this.name = "Cavalier";
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		int diffXInitFinal = Math.abs(this.getX()-xFinal);
		int diffYInitFinal =Math.abs(this.getY()-yFinal);
		// On verifie que le deplacement se fait de 2 cases puis de 1 case
		if(((diffXInitFinal-2)==0 && (diffYInitFinal-1) == 0) || ((diffXInitFinal-1)==0 && (diffYInitFinal-2) == 0)){
			return true;
		}
		return false;
	}

}
