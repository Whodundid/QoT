package envision.engine.internal.kernel.developerDesktop.windows.textEditor;

import envision.engine.internal.windows.windowObjects.action.WindowButton;
import envision.engine.internal.windows.windowObjects.advanced.textArea.WindowTextArea2;
import envision.engine.internal.windows.windowObjects.basic.WindowLabel;
import envision.engine.internal.windows.windowTypes.WindowParent;

public class TextEditorNew extends WindowParent {
    
    private WindowTextArea2 textArea;
    
    private WindowButton toggleBoldButton;
    private WindowButton toggleItalicButton;
    private WindowButton toggleUnderlineButton;
    private WindowButton applyFontColorButton;
    private WindowButton changeFontColorButton;
    
    private WindowLabel currentFileLabel;
    private WindowLabel cursorPosLabel;
    
    private EditorMode editorMode;
    
    public static enum EditorMode {
        TEXT,
        CODE
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void determineEditorMode() {
        
    }    
    //=========
    // Setters
    //=========
    
    public void setEditorMode(EditorMode mode) {
        
    }    
    //=========
    // Getters
    //=========
    
    public EditorMode getEditorMode() {
        return editorMode;
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("New Window");
        setSize(400, 400);
        setMinDims(200, 200);
        setResizeable(true);
        setMaximizable(true);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        defaultHeader();
        
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
    }


    
}
