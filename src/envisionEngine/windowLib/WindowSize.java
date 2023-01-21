package envisionEngine.windowLib;

import qot.QoT;

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
	
	public WindowSize(int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
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
