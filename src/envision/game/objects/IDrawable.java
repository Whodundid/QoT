package envision.game.objects;

import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;

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
	void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY);
	
}
