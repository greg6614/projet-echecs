package model;

public interface Pions {
	/**
	 *
	 * @param xFinal
	 * @param yFinal
	 * @return true si le deplacement en diagonale du pion est possible, false sinon
	 */
	public boolean isMoveDiagOk(int xFinal, int yFinal);

	/**
	 *
	 * @param xFinal
	 * @param yFinal
	 * @return true si le deplacement en diagonale a ete effectue, false sinon
	 */
	public boolean moveDiag(int xFinal, int yFinal);
}
