package envision.windowLib;

import game.QoT;

public class WindowSize {
	
	//--------
	// Fields
	//--------
	
	private final int width;
	private final int height;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowSize(QoT qoT) {
		width = QoT.getWidth();
		height = QoT.getHeight();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return width + ", " + height; }
	
	//---------
	// Getters
	//---------
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }

}
