package envision.engine.windows.bundledWindows;

import envision.debug.DebugSettings;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowSlider;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.colors.EColors;
import qot.assets.textures.taskbar.TaskBarTextures;

public class GameOptionsWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    private WindowSlider lightSlider;
    private WindowButton toggleBatchRendering;
    private WindowButton toggleCursedRendering;
    
    //==============
    // Constructors
    //==============
    
    public GameOptionsWindow() {
        windowIcon = TaskBarTextures.settings;
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setGuiSize(456, 527);
        setResizeable(true);
        setMinDims(148, 100);
        setMaximizable(true);
        setObjectName("Rendering Settings");
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        defaultHeader();
        
        var lightText = new WindowLabel(this, startX + 10, startY + 10, "Underground Brightness");
        lightSlider = new WindowSlider(this, startX + 10, lightText.endY + 10, width - 20, 40, 0, 100, false);
        
        toggleBatchRendering = new WindowButton(this, startX + 10, lightSlider.endY + 20, 300, 30, "Toggle Batch");
        toggleCursedRendering = new WindowButton(this, startX + 10, toggleBatchRendering.endY + 20, 300, 30,
                                                 "Toggle 3D");
        
        toggleBatchRendering.setAction(this::toggleBatch);
        toggleCursedRendering.setAction(this::toggleCursedRendering);
        
        addObject(lightText, lightSlider);
        addObject(toggleBatchRendering);
        addObject(toggleCursedRendering);
    }
    
    @Override
    public void drawObject(long dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        if (toggleBatchRendering != null) {
            String text = (BatchManager.isEnabled()) ? "True" : "False";
            int textColor = (BatchManager.isEnabled()) ? EColors.green.intVal : EColors.lred.intVal;
            drawString(text, toggleBatchRendering.endX + 10, toggleBatchRendering.midY - FontRenderer.FONT_HEIGHT * 0.5,
                       textColor);
        }
        
        if (toggleCursedRendering != null) {
            String text = (DebugSettings.draw3DCursed) ? "True" : "False";
            int textColor = (DebugSettings.draw3DCursed) ? EColors.green.intVal : EColors.lred.intVal;
            drawString(text, toggleCursedRendering.endX + 10, toggleCursedRendering.midY - FontRenderer.FONT_HEIGHT * 0.5,
                       textColor);
        }
        
        super.drawObject(dt, mXIn, mYIn);
    }
    
    protected void onLightChange() {
        
    }
    
    protected void toggleBatch() {
        if (BatchManager.isEnabled()) BatchManager.disable();
        else BatchManager.enable();
    }
    
    protected void toggleCursedRendering() {
        DebugSettings.draw3DCursed = !DebugSettings.draw3DCursed;
    }
    
}
