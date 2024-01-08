package envision.debug.testStuff;

import envision.engine.resourceLoaders.SpriteSheet;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.advancedObjects.menuBar.WindowMenuBar;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea2;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import eutil.datatypes.util.EList;

public class SpriteSheetBuilder extends WindowParent {
    
    //========
    // Fields
    //========
    
    private SpriteSheet currentSheet;
    
    /** Used to create a new sprite sheet. */
    private WindowButton newBtn;
    /** Button used to open/edit other already existing sprite sheets. */
    private WindowButton openBtn;
    /** Saves the current state of the sprite sheet to its JSON equivalent and updates the JSON editor. */
    private WindowButton saveBtn;
    /** Button used to either rebuild or reload the JSON file for the current sprite sheet. */
    private WindowButton reloadBtn;
    /** 1st tab is the sprite sheet image and editor, 2nd tab is the JSON built from the sheet. */
    private TabbedContainer tabContainer;
    /** Used to both display and edit the JSON of the spritesheet. */
    private WindowTextArea2 jsonEditor;
    /** Used to set/modify the name of this sprite sheet. */
    private WindowTextField sheetNameEntry;
    /** Text fields that can be used to set a constant sprite width/height. */
    private WindowTextField spriteWidthEntry, spriteHeightEntry;
    /** Displays the current sprite width/height. */
    private WindowLabel spriteDimensionsLbl;
    /** Displays the dimensions of the currently selected area within the sprite sheet texture. */
    private WindowLabel currentlySelectedAreaLbl;
    
    private WindowMenuBar menuBar;
    
    private boolean isSaved = false;
    
    public final int defaultSpritePixelWidth = 64;
    public final int defaultSpritePixelHeight = 64;
    
    //==============
    // Constructors
    //==============
    
    public SpriteSheetBuilder() {
        this(null);
    }
    
    public SpriteSheetBuilder(SpriteSheet sheetIn) {
        currentSheet = sheetIn;
        
        this.setSize(600, 400);
        this.setResizeable(true);
        this.setMaximizable(true);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        defaultHeader();
        
        menuBar = new WindowMenuBar(this);
        var fileMenu = menuBar.addMenuCategory("File");
        
        fileMenu.addMenuEntry("New", this::createNewSheet);
        fileMenu.addMenuEntry("Save", this::saveSheet);
        
        addObject(menuBar);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject(dt, mXIn, mYIn);
    }
    
    @Override
    public boolean allowsSystemDragAndDrop() {
        return true;
    }
    
    @Override
    public void onSystemDragAndDrop(EList<String> droppedFileNames) {
        
    }
    
    @Override
    public void onGroupNotification(ObjectEvent e) {
        
    }
    
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        
    }
    
    @Override
    public void preReInit() {
        
    }
    
    @Override
    public void postReInit() {
        
    }
    
    //=========
    // Methods
    //=========
    
    public void saveSheet() {
        
    }
    
    public void loadSheet() {
        
    }
    
    public void unloadSheet() {
        
    }
    
    public void createNewSheet() {
        
    }
    
    //=========
    // Getters
    //=========
    
    public SpriteSheet getSheet() { return currentSheet; }
    
    //=========
    // Setters
    //=========
    
}
