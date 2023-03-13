package envision.engine.windows;

import envision.Envision;

public class WindowSize {
	
	//--------
	// Fields
	//--------
	
	private final int width;
	private final int height;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowSize() {
		width = Envision.getWidth();
		height = Envision.getHeight();
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
