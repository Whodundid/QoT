package envision.engine.internal.windows.windowObjects.basic;

import envision.engine.internal.windows.windowTypes.WindowObject;
import envision.engine.internal.windows.windowUtil.layouts.IWindowLayout;
import eutil.colors.EColors;

public class WindowPanel extends WindowObject {
    
    //========
    // Fields
    //========
    
    public int backgroundColor = EColors.dgray.intVal;
    
    public boolean drawBorder = false;
    public int borderColor = EColors.black.intVal;
    public int borderWidth = 1;
    
    //==============
    // Constructors
    //==============
    
    public WindowPanel() { this((IWindowLayout) null); }
    public WindowPanel(String name) { this(name, null); }
    public WindowPanel(IWindowLayout layout) { this(null, layout); }
    public WindowPanel(String name, IWindowLayout layout) {
        this.setObjectName(name);
        this.setLayout(layout);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawRect(backgroundColor);
        //scissor();
        super.drawObject_i(dt, mXIn, mYIn);
        //endScissor();
        if (drawBorder) {
            drawHRect(borderColor, borderWidth);
        }
        //drawHRect(EColors.red);
    }
    
    //=========
    // Getters
    //=========
    
    public int getBackground() { return backgroundColor; }
    
    //=========
    // Setters
    //=========
    
    public void setBackground(EColors color) { backgroundColor = color.intVal; }
    public void setBackground(int color) { backgroundColor = color; }
    
}
