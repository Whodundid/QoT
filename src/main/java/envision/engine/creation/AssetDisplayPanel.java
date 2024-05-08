package envision.engine.creation;

import envision.engine.assets.WindowTextures;
import envision.engine.registry.AbstractResourceRegistry;
import envision.engine.registry.IGameResource;
import envision.engine.registry.types.Sprite;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

/**
 * Displays the game assets that are actively loaded.
 * 
 * @author Hunter
 */
public class AssetDisplayPanel extends WindowObject {
    
    //========
    // Fields
    //========
    
    private final EList<IGameResource> loadedResources = EList.newList();
    private AbstractResourceRegistry<?> resourceRegistry;
    
    private WindowButton refreshButton;
    private WindowScrollList scrollArea;
    
    //==============
    // Constructors
    //==============
    
    public AssetDisplayPanel(AbstractResourceRegistry<?> resourceRegistry) {
        this.resourceRegistry = resourceRegistry;
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        int bw = 28;
        refreshButton = new WindowButton(this, endX - 4 - bw, startY + 3, bw, bw);
        WindowButton.setTextures(WindowTextures.refresh, WindowTextures.refresh_sel, refreshButton);
        refreshButton.setAction(this::refresh);
        
        double w = (endX - 2) - (startX + 2);
        double h = (endY - 2) - (startY + 33);
        scrollArea = new WindowScrollList(this, startX + 2, startY + 33, w, h);
        
        addObject(refreshButton);
        addObject(scrollArea);
        
        loadAssets();
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        // background
        drawRect(EColors.black);
        drawRect(EColors.dgray, 2);
        
        // top header
        drawRect(startX + 2, startY + 2, endX - 2, startY + 34, EColors.lsteel);
        drawRect(startX, startY + 32, endX, startY + 33, EColors.black);
        drawString(resourceRegistry.getRegistryName(), startX + 10, startY + 40 / 2 - FontRenderer.HALF_FH, EColors.lgray);
        
        super.drawObject_i(dt, mXIn, mYIn);
    }

    //=========
    // Methods
    //=========
    
    public void refresh() {
        scrollArea.clearList();
        resourceRegistry.loadResources();
        loadAssets();
    }
    
    public void loadAssets() {
        EList<ResourceDisplay> resourceDisplays = EList.newList();
        
        for (String resourceName : resourceRegistry.getAllResourceNames()) {
            IGameResource resource = resourceRegistry.getResource(resourceName);
            resourceDisplays.add(new ResourceDisplay(resourceName, resource));
        }
        
        int gap = 10;
        int displayWidth = 64;
        int displayHeight = 64;
        int maxX = (int) (width / (gap * 2 + (displayWidth + gap)));
        
        final int size = resourceDisplays.size();
        final var dims = scrollArea.getListDimensions();
        
        for (int i = 0; i < size; i++) {
            int x = i % maxX;
            int y = i / maxX;
            
            double sx = dims.startX + gap + (x * displayWidth) + (x * gap);
            double sy = dims.startY + gap + (y * displayHeight) + (y * gap);
            
            ResourceDisplay d = resourceDisplays.get(i);
            d.setDimensions(sx, displayWidth, sy, displayHeight);
            scrollArea.addObjectToList(d);
        }
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    public static class ResourceDisplay extends WindowObject {
        private String resourceName;
        private final IGameResource resource;
        private final Sprite resourceSprite;
        
        public ResourceDisplay(String name, IGameResource resourceIn) {
            resourceName = name;
            resource = resourceIn;
            resourceSprite = getResourceSprite(resource);
        }
        
        @Override
        public void drawObject_i(float dt, int mXIn, int mYIn) {
            super.drawObject_i(dt, mXIn, mYIn);
            
            drawSprite(resourceSprite);
            drawStringC(resourceName, midX, endY + 8);
        }
    }
    
    private static Sprite getResourceSprite(IGameResource resource) {
        switch (resource.getResourceType()) {
        case FILE: return new Sprite(WindowTextures.file_txt);
        case SOUND: 
        case TEXTURE: 
        default: return new Sprite(WindowTextures.file_pic);
        }
    }
    
}
