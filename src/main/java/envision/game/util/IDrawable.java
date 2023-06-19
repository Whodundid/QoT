package envision.game.util;

public interface IDrawable {
	
	/**
	 * Usually associated with the y coordinate value that
	 * will determine the draw order of game objects.
	 */
	double getSortPoint();
	
	/**
	 * Draws the object at the given position with the given dimensions.
	 * @param midX TODO
	 * @param midY TODO
	 */
	//void draw(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver);
	
}