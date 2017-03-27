package model;

public class Roi extends AbstractPiece {

	public Roi(Couleur couleur, Coord coord) {
		super(couleur, coord);
		this.name="Roi";
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		
		int diffXInitFinal = Math.abs(this.getX()-xFinal);
		int diffYInitFinal =Math.abs(this.getY()-yFinal);
		if(diffXInitFinal<=1&&diffYInitFinal<=1){
			return true;
		}
		return false;
	}

}
