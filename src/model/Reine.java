package model;

public class Reine extends AbstractPiece{

	public Reine(Couleur couleur, Coord coord) {
		super(couleur, coord);
		this.name="Reine";
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		int diffXInitFinal = Math.abs(this.getX()-xFinal);
		int diffYInitFinal =Math.abs(this.getY()-yFinal);
		if(diffXInitFinal==diffYInitFinal||!(this.getX() != xFinal && this.getY() != yFinal)){
			return true;
		}
		return false;
	}

}
