package envision.engine.terminal.window;

import envision.Envision;
import envision.engine.EngineSettings;
import envision.engine.assets.TaskBarTextures;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowSlider;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.misc.ScreenLocation;

public class TerminalOptionsWindow extends WindowParent {

    WindowScrollList settings;
    WindowButton drawLineNumbers, backColor, maxLines;
    WindowSlider opacitySlider;
    
    public TerminalOptionsWindow() {
        aliases.add("termoptions", "toptions");
        windowIcon = TaskBarTextures.settings;
    }
    
    @Override
    public boolean isDebugWindow() {
        return true;
    }
    
    @Override
    public void initWindow() {
        setGuiSize(490, 340);
        setMinDims(490, 340);
        setResizeable(true);
        setObjectName("Terminal Settings");
    }
    
    @Override
    public void initChildren() {
        defaultHeader(this);
        
        settings = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
        settings.setBackgroundColor(0xff303030);
        
        //Visual label
        WindowLabel visual = new WindowLabel(settings, startX + 8, startY + 10, "Visual", EColors.orange);
        
        settings.addObjectToList(false, visual);
        
        //buttons
        drawLineNumbers = new WindowButton(settings, startX + 12, visual.endY + 20, 130, 30, EngineSettings.termLineNumbers);
        backColor = new WindowButton(settings, startX + 13, drawLineNumbers.endY + 15, 20, 20) {
            @Override
            public void drawObject(float dt, int mXIn, int mYIn) {
                super.drawObject(dt, mXIn, mYIn);
                RenderingManager.drawHRect(backColor.startX - 1, backColor.startY - 1, backColor.endX + 1, backColor.endY + 1, 1, EColors.black);
                RenderingManager.drawHRect(backColor.startX, backColor.startY, backColor.endX, backColor.endY, 1, EColors.lgray);
                RenderingManager.drawHRect(backColor.startX + 1, backColor.startY + 1, backColor.endX - 1, backColor.endY - 1, 1, EColors.black);
            }
        };
        
        final var fh = FontRenderer.FONT_HEIGHT * 0.4;
        var opacityLbl = new WindowLabel(settings, startX + 12, backColor.endY + 35, "Terminal background opacity", EColors.lgray);
        
        opacitySlider = new WindowSlider(settings, startX + 12, opacityLbl.endY + 5, 250, 30, 0, 255, false);
        opacitySlider.setUseIntegers(true);
        opacitySlider.setSliderValue(EngineSettings.termOpacity.get());
        
        backColor.setDrawBackground(true);
        backColor.setBackgroundColor(EngineSettings.termBackground.get());
        backColor.setTextures(null, null);
        
        IActionObject.setActionReceiver(this, drawLineNumbers, backColor, opacitySlider);
        
        //labels
        WindowLabel numberLabel = new WindowLabel(settings, drawLineNumbers.endX + 20, drawLineNumbers.midY - fh, "Draw line numbers", EColors.lgray);
        WindowLabel background = new WindowLabel(settings, backColor.endX + 20, backColor.midY - fh, "Terminal background color", EColors.lgray);
        
        IWindowObject.setHoverText("Displays line numbers in terminals", numberLabel, drawLineNumbers);
        IWindowObject.setHoverText("Sets the background color in terminals", background, backColor);
        
        //add to list
        settings.addObjectToList(false, drawLineNumbers, backColor, opacitySlider);
        settings.addObjectToList(false, numberLabel, background, opacityLbl);
        
        settings.fitItemsInList();
        
        addObject(settings);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        //System.out.println(this.getDimensions());
    }
    
    @Override
    public void sendArgs(Object... args) {
        
    }
    
    @Override
    public void resize(double xIn, double yIn, ScreenLocation areaIn) {
        try {
            if (xIn != 0 || yIn != 0) {
                double vPos = settings.getVScrollBar().getScrollPos();
                double hPos = settings.getHScrollBar().getScrollPos();
                super.resize(xIn, yIn, areaIn);
                settings.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
                settings.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        if (object == drawLineNumbers) lineNumbers();
        if (object == backColor) changeColor();
        if (object == opacitySlider) changeOpacity();
        
        if (args.length > 0 && object instanceof ColorPickerSimple) {
            try {
                int val = (int) args[0];
                EngineSettings.termBackground.set(val);
                backColor.setBackgroundColor(val);
                Envision.saveEngineConfig();
                Envision.getDeveloperDesktop().reloadAllWindowInstances(ETerminalWindow.class);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void lineNumbers() {
        drawLineNumbers.toggleTrueFalseDisplay(EngineSettings.termLineNumbers, Envision.getEngineConfig());
        
        Envision.getDeveloperDesktop().reloadAllWindowInstances(ETerminalWindow.class);
    }
    
    private void changeColor() {
        Envision.getDeveloperDesktop().displayWindow(new ColorPickerSimple(this, EngineSettings.termBackground.get()));
    }
    
    private void changeOpacity() {
        EngineSettings.termOpacity.set((int) opacitySlider.getSliderValue());
        Envision.saveEngineConfig();
        
        final var background = EngineSettings.termBackground.get();
        final var opacity = EngineSettings.termOpacity.get();
        
        var terms = Envision.getDeveloperDesktop().getAllWindowInstances(ETerminalWindow.class);;
        for (var t : terms) {
            var c = EColors.changeOpacity(background, opacity);
            t.history.setBackgroundColor(c);
            t.inputField.setBackgroundColor(c);
        }
    }
    
}
