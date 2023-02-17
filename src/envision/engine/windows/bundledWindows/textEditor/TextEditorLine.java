package envision.engine.windows.bundledWindows.textEditor;

import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class TextEditorLine implements CharSequence {

	//========
	// Fields
	//========
	
	private String contents;
	private EList<TextLink> linkData = new EArrayList<>();
	
	//==============
	// Constructors
	//==============
	
	public TextEditorLine() {}
	public TextEditorLine(String contentIn) {
		contents = contentIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public int length() {
		return 0;
	}

	@Override
	public char charAt(int index) {
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return null;
	}
	
	//=========
	// Getters
	//=========
	
	/** Returns the actual string content of this line. */
	public String getContents() { return contents; }
	
	//=========
	// Setters
	//=========
	
	
}
