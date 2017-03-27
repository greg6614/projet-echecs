package model;

public interface Pieces {
	/**
	 * 
	 * @return colonne
	 */
	int getX();
	
	/**
	 * 
	 * @return ligne
	 */
	int getY();
	
	/**
	 * 
	 * @return couleur
	 */
	Couleur getCouleur();
	
	/**
	 * Verification que la position finale de la piece est correcte
	 * @param xFinal
	 * @param yFinal
	 * @return true si le deplacement est legal en fonction des algos 
	 * de deplacement specifique de chaque piece.
	 */
	boolean isMoveOk(int xFinal, int yFinal);
	
	/**
	 * 
	 * @return true si la piece est effectivement capturee.
	 * Positionne x et y a -1.
	 */
	boolean capture();


	/**
	 * Deplace la piece concernee
	 * @param xFinal
	 * @param yFinal
	 * @return true si le deplacement a ete effectue, false sinon
	 */
	boolean move(int xFinal, int yFinal);

	/**
	 *
	 * @param xFinal
	 * @param yFinal
	 * @return true si les valeurs de xFinal et de yFinal ne sont pas dans le plateau de jeu
	 */
	boolean outOfBoard(int xFinal, int yFinal);

	/**
	 *
	 * @param xFinal
	 * @param yFinal
	 * @return true si les coordonnees xFinal et yFinal sont les memes que les coordonnes courantes de la pieces
	 */
	boolean isSameAsCurrent(int xFinal, int yFinal);
	
}
