package envisionEngine.eWindow.windowUtil.input;

public interface MouseInputAcceptor {
	
	public default void handleMouseInput(int action, int mXIn, int mYIn, int button, int change) {
		switch (action) {
		case 0: mouseReleased(mXIn, mYIn, button); break;
		case 1: mousePressed(mXIn, mYIn, button); break;
		case 2: mouseScrolled(change); break;
		default: throw new IllegalArgumentException("Invalid keyboard action type! " + action);
		}
	}
	
	public default void parseMousePosition(int mX, int mY) {}
	public default void mousePressed(int mX, int mY, int button) {}
	public default void mouseReleased(int mX, int mY, int button) {}
	public default void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	public default void mouseScrolled(int change) {}
	
}
