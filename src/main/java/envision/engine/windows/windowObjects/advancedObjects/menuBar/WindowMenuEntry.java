package envision.engine.windows.windowObjects.advancedObjects.menuBar;

import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowTypes.ActionObject;
import eutil.math.ENumUtil;

public class WindowMenuEntry extends ActionObject {
    
    //========
    // Fields
    //========
    
    private WindowButton entryButton;
    private WindowImageBox entrySpriteDisplay;
    
    private String entryTitle;
    private Sprite entrySprite;
    
    public static final String DEFAULT_ENTRY_TITLE = "New Entry";
    public static final int DEFAULT_ENTRY_PIXEL_HEIGHT = 40;
    
    //==============
    // Constructors
    //==============
    
    public WindowMenuEntry() { this(DEFAULT_ENTRY_TITLE, null, null); }
    public WindowMenuEntry(String titleIn) { this(titleIn, null, null); }
    public WindowMenuEntry(String titleIn, Runnable pressActionIn) { this(titleIn, null, pressActionIn); }
    public WindowMenuEntry(String titleIn, Sprite entrySpriteIn) { this(titleIn, entrySpriteIn, null); }
    public WindowMenuEntry(String titleIn, Sprite entrySpriteIn, Runnable pressActionIn) {
        this.entryTitle = titleIn;
        this.setObjectName(titleIn);
        this.entrySprite = entrySpriteIn;
        this.onPressAction = pressActionIn;
        this.setRunActionOnPress(true);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        var pd = getParent().getDimensions();
        
        entrySpriteDisplay = new WindowImageBox(this, startX, startY, height, height);
        var d = entrySpriteDisplay.getDimensions();
        
        double width = strWidth(entryTitle);
        width = ENumUtil.clamp(width, pd.width - d.width, pd.width);
        
        entryButton = new WindowButton(this, d.endX, startY, width, height, entryTitle);
        entryButton.setAction(onPressAction);
        
        
        addObject(entrySpriteDisplay, entryButton);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        //System.out.println(entrySpriteDisplay.getDimensions());
        
    }
    
    //=========
    // Getters
    //=========
    
    public String getEntryTitle() { return entryTitle; }
    
    //=========
    // Setters
    //=========
    
    public void setEntryTitle(String titleIn) { entryTitle = titleIn; }
    
}
