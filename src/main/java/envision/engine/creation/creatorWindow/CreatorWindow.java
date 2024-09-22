package envision.engine.creation.creatorWindow;

import envision.engine.assets.WindowTextures;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.colors.EColors;

public class CreatorWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    private LoadedAssetPanel assetPanel;
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("Creator");
        setSize(400, 400);
        setMinDims(200, 200);
        setResizeable(true);
        setMaximizable(true);
        this.windowIcon = WindowTextures.settings;
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        defaultHeader();
        
        assetPanel = new LoadedAssetPanel(this);
        
        addObject(assetPanel);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        drawRect(EColors.pdgray, 1);
        
        super.drawObject_i(dt, mXIn, mYIn);
    }
    
    
}
