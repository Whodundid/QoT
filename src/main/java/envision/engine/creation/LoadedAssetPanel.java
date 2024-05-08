package envision.engine.creation;

import envision.engine.registry.AbstractResourceRegistry;
import envision.engine.registry.ResourceRegistry;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.engine.windows.windowTypes.WindowObject;
import eutil.datatypes.util.EList;

public class LoadedAssetPanel extends WindowObject {
    
    //========
    // Fields
    //========
    
    private CreatorWindow parentWindow;
    private final EList<AbstractResourceRegistry> resourceRegistries = EList.newList();
    private TabbedContainer tabPanel;
    
    //==============
    // Constructors
    //==============
    
    public LoadedAssetPanel(CreatorWindow parentWindowIn) {
        parentWindow = parentWindowIn;
        
        resourceRegistries.add(ResourceRegistry.getTextureRegistry());
        
        double w = parentWindow.width - 2;
        double h = parentWindow.height / 2.7;
        double x = parentWindow.startX + 1;
        double y = parentWindow.endY - h - 1;
        
        this.init(parentWindowIn, x, y, w, h);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        tabPanel = new TabbedContainer(this, startX + 1, startY + 1, width - 2, height - 2);
        
        createTabs();
        
        addObject(tabPanel);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        super.drawObject_i(dt, mXIn, mYIn);
        
        
    }

    //=========
    // Methods
    //=========
    
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private void createTabs() {
        for (var registry : resourceRegistries) {
            String name = registry.getRegistryName();
            name = name.replace("Registry", "").trim();
            if (!name.endsWith("s")) name += "s";
            var dims = tabPanel.getTabDims();
            AssetDisplayPanel panel = new AssetDisplayPanel(registry);
            panel.setDimensions(dims);
            tabPanel.addTab(name, panel);
        }
    }
    
}
