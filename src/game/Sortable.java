package game;

/**
 * A specialized interface used for indicating that the given object
 * is capable of being sorted for drawing order purposes.
 * 
 * @author Hunter Bragg
 */
public interface Sortable {

	/**
	 * Each Sortable object should return its endY coordinate
	 * in order to determine which object should effectively
	 * be drawn first.
	 * 
	 * @return This object's endY coordinate
	 */
	public double getYPos();
	
}
