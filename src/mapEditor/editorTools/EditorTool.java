package mapEditor.editorTools;

import mapEditor.MapEditorScreen;

public abstract class EditorTool {
	
	protected MapEditorScreen editor;
	
	//--------------
	// Constructors
	//--------------
	
	protected EditorTool(MapEditorScreen in) {
		editor = in;
	}
	
	//---------
	// Methods
	//---------
	
	public void onEvent(boolean pressed, int xIn, int yIn, int button) {
		if (pressed) { onPress(xIn, yIn, button); }
		else { onRelease(xIn, yIn, button); }
	}
	
	//------------------
	// Abstract Methods
	//------------------
	
	public abstract void onPress(int xIn, int yIn, int button);
	public abstract void onRelease(int xIn, int yIn, int button);
	
}
