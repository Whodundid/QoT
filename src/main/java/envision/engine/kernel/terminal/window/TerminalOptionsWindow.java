package envision.engine.kernel.terminal.window;

import envision.Envision;
import envision.engine.EngineSettings;
import envision.engine.assets.TaskBarTextures;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.action.WindowButton;
import envision.engine.windows.windowObjects.action.WindowCheckBox;
import envision.engine.windows.windowObjects.action.WindowSlider;
import envision.engine.windows.windowObjects.advanced.WindowScrollList;
import envision.engine.windows.windowObjects.advanced.colorPicker.ColorPickerSimple;
import envision.engine.windows.windowObjects.basic.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.misc.ScreenLocation;

public class TerminalOptionsWindow extends WindowParent {

    WindowScrollList settings;
    WindowButton drawLineNumbers, backColor, maxLines;
    WindowSlider opacitySlider;
    WindowCheckBox addNewLineBetweenCommands;
    WindowSlider scrollRateSlider;
    
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
        drawLineNumbers.setAction(this::lineNumbers);
        
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
        opacitySlider.setSliderValue(EngineSettings.termOpacity);
        opacitySlider.setAction(this::changeOpacity);
        
        addNewLineBetweenCommands = new WindowCheckBox(settings, startX + 12, opacitySlider.endY + 15, 20, 20);
        var addNewLineLabel = new WindowLabel(settings, addNewLineBetweenCommands.endX + 12, addNewLineBetweenCommands.midY - fh, "Add new line between commands", EColors.lgray);
        addNewLineBetweenCommands.setAction(this::toggleAddNewLines);
        addNewLineBetweenCommands.setChecked(EngineSettings.termCmdNewLines);
        
        var scrollRateLbl = new WindowLabel(settings, startX + 12, addNewLineBetweenCommands.endY + 35, "Terminal Scroll Rate", EColors.lgray);
        scrollRateSlider = new WindowSlider(settings, startX + 12, scrollRateLbl.endY + 5, 250, 30, 10, 255, false);
        scrollRateSlider.setUseIntegers(true);
        scrollRateSlider.setSliderValue(EngineSettings.termScrollRate);
        scrollRateSlider.setAction(this::changeScrollRate);
        
        backColor.setDrawBackground(true);
        backColor.setBackgroundColor(EngineSettings.termBackground);
        backColor.setTextures(null, null);
        
        IActionObject.setActionReceiver(this, backColor);
        
        //labels
        WindowLabel numberLabel = new WindowLabel(settings, drawLineNumbers.endX + 20, drawLineNumbers.midY - fh, "Draw line numbers", EColors.lgray);
        WindowLabel background = new WindowLabel(settings, backColor.endX + 20, backColor.midY - fh, "Terminal background color", EColors.lgray);
        
        IWindowObject.setHoverText("Displays line numbers in terminals", numberLabel, drawLineNumbers);
        IWindowObject.setHoverText("Sets the background color in terminals", background, backColor);
        //IWindowObject.setHoverText("Modifies the rate at which the scroll wheel will advance through the terminal history", scrollRateLbl, scrollRateSlider);
        IWindowObject.setHoverText("An empty line is added after any command is executed in terminals", addNewLineLabel);
        
        //add to list
        settings.addObjectToList(false, drawLineNumbers, backColor, opacitySlider, addNewLineBetweenCommands, scrollRateSlider);
        settings.addObjectToList(false, numberLabel, background, opacityLbl, addNewLineLabel, scrollRateLbl);
        
        settings.fitItemsInList();
        
        addObject(settings);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        super.drawObject(dt, mXIn, mYIn);
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
        Envision.getDeveloperDesktop().displayWindow(new ColorPickerSimple(this, EngineSettings.termBackground));
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
    
    private void toggleAddNewLines() {
        EngineSettings.termCmdNewLines.toggle();
        Envision.saveEngineConfig();
    }
    
    private void changeScrollRate() {
        EngineSettings.termScrollRate.set((int) scrollRateSlider.getSliderValue());
        Envision.saveEngineConfig();
        
        var terms = Envision.getDeveloperDesktop().getAllWindowInstances(ETerminalWindow.class);;
        for (var t : terms) {
            t.setTerminalScrollRate(scrollRateSlider.getSliderValue());
        }
    }
    
}
