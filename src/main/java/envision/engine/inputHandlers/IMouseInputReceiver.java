package envision.engine.inputHandlers;

import eutil.datatypes.util.EList;

public interface IMouseInputReceiver {
	
    default void onMouseEnteredWindow(int mXIn, int mYIn) {}
    default void onMouseExitedWindow(int mXIn, int mYIn) {}
    
	void onMouseInput(int action, int mXIn, int mYIn, int button, int change);
	void onDroppedFiles(EList<String> droppedFileNames);
	
}
