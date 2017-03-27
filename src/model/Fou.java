package model;

public class Fou extends AbstractPiece {

	public Fou(Couleur couleur, Coord coord) {
		super(couleur, coord);
		this.name = "Fou";
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		
		int diffXInitFinal = Math.abs(this.getX()-xFinal);
		int diffYInitFinal =Math.abs(this.getY()-yFinal);
		// On verifie que le deplacement se fait en diagonale.
		if(diffXInitFinal==diffYInitFinal){
			return true;
		}
		return false;
	}

}
