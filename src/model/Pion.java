package model;

public class Pion extends AbstractPiece implements Pions {

	public Pion(Couleur couleur, Coord coord) {
		super(couleur, coord);
		this.name = "Pion";
	}

	@Override
	public boolean isMoveDiagOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || this.isSameAsCurrent(xFinal, yFinal)){
			return false;
		}
		if(Math.abs(xFinal-this.getX()) != 1 || Math.abs(yFinal-this.getY()) != 1 || (xFinal == this.getX())) {
			return false;
		}
		switch(this.getCouleur()) {
			case BLANC:
				if(yFinal - this.getY() == 1) {
					return false;
				}
				break;
			case NOIR:
				if(yFinal - this.getY() == -1) {
					return false;
				}
				break;
			default:
					return false;
		}
		return true;
	}

	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		if(this.outOfBoard(xFinal, yFinal) || xFinal != this.getX()){
			return false;
		}
		// Gestion du cas particulier ou on peut se deplacer de 2 cases avant la gestion du cas normal
		switch(this.getCouleur()) {
			case BLANC:
				if((this.getY() == 6) && (yFinal - this.getY() == -2))
				{
					return true;
				}
				if(yFinal - this.getY() != -1) {
					return false;
				}
				break;
			case NOIR:
				if((this.getY() == 1) && (yFinal - this.getY() == 2))
				{
					return true;
				}
				if(yFinal - this.getY() != 1) {
					return false;
				}
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean moveDiag(int xFinal, int yFinal) {
		if(this.isMoveDiagOk(xFinal, yFinal)) {
			this.coord.x = xFinal;
			this.coord.y = yFinal;
			return true;
		}
		return false;
	}

}
