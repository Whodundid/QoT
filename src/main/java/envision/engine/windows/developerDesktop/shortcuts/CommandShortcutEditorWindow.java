package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.inputHandlers.Mouse;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.strings.EStringUtil;

public class CommandShortcutEditorWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    protected DesktopShortcut_Command shortcut;
    
    protected WindowLabel infoLabel;
    protected WindowTextField commandEntry;
    protected WindowButton confirmButton, cancelButton;
    
    protected String enteredText;
    protected boolean makeNew;
    
    //==============
    // Constructors
    //==============
    
    public CommandShortcutEditorWindow() {
        this.shortcut = new DesktopShortcut_Command("New Command");
        this.shortcut.setPosition(Mouse.getMx(), Mouse.getMy());
    }
    
    public CommandShortcutEditorWindow(DesktopShortcut_Command shortcutIn) {
        this(shortcutIn, false);
    }
    
    public CommandShortcutEditorWindow(DesktopShortcut_Command shortcutIn, boolean makeNewIn) {
        this.shortcut = shortcutIn;
        this.makeNew = makeNewIn;
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("Edit Command");
        setSize(550, 150);
        setMinDims(550, 150);
        setMaxHeight(550);
        setResizeable(true);
        setMaximizable(false);
        setMinimizable(false);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void onAdded() {
        getTopParent().setFocusLockObject(this);
    }
    
    @Override
    public void initChildren() {
        defaultHeader().setTitleColor(EColors.pink);
        
        double gap = 10.0;
        double sx = startX + gap;
        double sy = startY + gap;
        double w = width - gap * 2.0;
        
        infoLabel = new WindowLabel(this, sx, sy, "Enter a command to be executed");
        commandEntry = new WindowTextField(this, sx, infoLabel.endY + 10, w, 30);
        
        double bwidth = ENumUtil.clamp(width / 4, 120, 400);
        double bheight = 40.0;
        double bgap = ENumUtil.clamp(width / 8, 5, 150) * 0.5;
        double bsy = endY - gap - bheight;
        
        confirmButton = new WindowButton(this, midX - bgap - bwidth, bsy, bwidth, bheight, "Confirm");
        cancelButton = new WindowButton(this, midX + bgap, bsy, bwidth, bheight, "Cancel");
        
        infoLabel.setColor(EColors.yellow);
        commandEntry.setTextWhenEmpty("command to run...");
        confirmButton.setStringColor(EColors.lgreen);
        cancelButton.setStringColor(EColors.lred);
        
        //commandEntry.setHoverText("The command to run when this shortcut is opened");
        confirmButton.setHoverText("Saves this command to the shortcut");
        cancelButton.setHoverText("Stop editing this shortcut");
        
        commandEntry.setAction(this::saveCommand);
        confirmButton.setAction(this::saveCommand);
        cancelButton.setAction(this::close);
        
        if (shortcut != null && EStringUtil.isPopulated(shortcut.getCommandName())) {
            commandEntry.setText(shortcut.getCommandName());
        }
        
        setDefaultFocusObject(commandEntry);
        
        addObject(infoLabel);
        addObject(commandEntry);
        addObject(confirmButton, cancelButton);
    }
    
    @Override
    public void preReInit() {
        enteredText = commandEntry.getText();
    }
    
    @Override
    public void postReInit() {
        commandEntry.setText(enteredText);
    }
    
    @Override
    public void drawObject(long dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
    }
    
    protected void saveCommand() {
        if (EStringUtil.isNotPopulated(commandEntry.getText())) return;
        
        System.out.println(shortcut);
        String text = commandEntry.getText();
        shortcut.parseCommand(text);
        
        if (makeNew) {
            DeveloperDesktop.addShortcut(shortcut);
        }
        else {
            DeveloperDesktop.saveConfig();
            DeveloperDesktop.reloadConfig();            
        }
        close();
    }
    
}
