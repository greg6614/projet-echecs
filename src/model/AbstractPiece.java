package model;

public abstract class AbstractPiece implements Pieces {
	
	private Couleur couleur;
	protected Coord coord;
	protected String name;
	
	public AbstractPiece(Couleur couleur, Coord coord) {
		this.couleur = couleur;
		this.coord = coord;
	}

	@Override
	public int getX() {
		return this.coord.x;
	}

	@Override
	public int getY() {
		return this.coord.y;
	}

	@Override
	public Couleur getCouleur() {
		return this.couleur;
	}

	@Override
	public abstract boolean isMoveOk(int xFinal, int yFinal);

	@Override
	public boolean capture() {
		this.coord.x = -1;
		this.coord.y = -1;
		return true;
	}
	
	@Override
	public String toString() {
		return "name="
				+ this.name
				+ ". Coord: x="
				+ Integer.toString(this.coord.x)
				+ "; y="
				+ Integer.toString(this.coord.y)
				+ ".\n";
		
	}

	@Override
	public boolean move(int xFinal, int yFinal) {
		if(this.isMoveOk(xFinal, yFinal)) {
			this.coord.x = xFinal;
			this.coord.y = yFinal;
			return true;
		}
		return false;
	}

	@Override
	public boolean outOfBoard(int xFinal, int yFinal) {
		if((xFinal < 0 || xFinal > 7) || (yFinal < 0 || yFinal > 7)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSameAsCurrent(int xFinal, int yFinal) {
		if((this.getX()-xFinal == 0) && (this.getY()-yFinal == 0)) {
			return true;
		}
		return false;
	}
}
