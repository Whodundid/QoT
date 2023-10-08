package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.CommandShortcutEditorWindow;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import qot.assets.textures.window.WindowTextures;

public class DesktopShortcut_Command extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private static final ETerminalWindow DUMMY_TERMINAL = new ETerminalWindow();
    
    private String commandName;
    private final EList<String> args = EList.newList();
    
    /**
     * True if executing this shortcut will open a terminal window or not.
     * (runs in background otherwise)
     */
    private boolean willOpenTerminal = true;
    
    /**
     * If false, a new terminal window will be opened each time this command is
     * run. Otherwise, the outputs of running this command will be displayed in
     * the first terminal window instance found on the desktop.
     * <p>
     * NOTE: Does nothing if 'willOpenTerminal' is not also true.
     */
    private boolean canDisplayOnExistingTerminal = true;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Command(String shortcutName) {
        super(shortcutName, ShortcutType.COMMAND);
        this.setIcon(WindowTextures.terminal);
    }
    
    public DesktopShortcut_Command(int x, int y, String commandNameIn, EList<String> argsIn) {
        this(commandNameIn, x, y, commandNameIn, argsIn);
    }
    
    public DesktopShortcut_Command(String shortcutName, int x, int y, String commandNameIn, EList<String> argsIn) {
        super(shortcutName, x, y, ShortcutType.COMMAND);
        commandName = commandNameIn;
        args.addAll(argsIn);
        this.setIcon(WindowTextures.terminal);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        if (commandName == null) return;
        
        if (willOpenTerminal) {
            runCommandInTerminal();
        }
        else {
            // find a registered command under the given name
            TerminalCommand command = TerminalCommandHandler.getRegisteredCommand(commandName);
            if (command == null) return;
            
            command.runCommand_i(DUMMY_TERMINAL, args, false);            
        }
    }
    
    protected void runCommandInTerminal() {
        ETerminalWindow termToRunOn = null;
        
        // check if we can display on already existing terminals
        if (canDisplayOnExistingTerminal) {
            // get top most terminal (if there is one)
            termToRunOn = DeveloperDesktop.findFirstTerminalInstance();
        }
        
        if (termToRunOn == null) {
            termToRunOn = DeveloperDesktop.openTerminal();
        }
        
        termToRunOn.setDrawsNewLineBetweenCommands(false);
        termToRunOn.writeln(EColors.yellow, "Running DesktopShortcut Command: ",
                            EColors.yellow, "'",
                            EColors.green, shortcutName,
                            EColors.yellow, "'");
        if (args.isEmpty()) termToRunOn.runCommand(commandName);
        else termToRunOn.runCommand(commandName, args);
        termToRunOn.writeln();
        termToRunOn.setDrawsNewLineBetweenCommands(true);
    }
    
    @Override
    public void openShortcutRCM() {
        createBaseShortcutRCM();
        
        shortcutRCM.addOption("Run Command", WindowTextures.file_up, this::onDoubleClick);
        shortcutRCM.addOption("Edit Command", this::editCommand);
        shortcutRCM.addOption("Copy", this::copyShortcut);
        shortcutRCM.addOption("Delete", WindowTextures.red_x, this::deleteShortcut);
        shortcutRCM.addOption("Rename", this::renameShortcut);
        shortcutRCM.addOption("Properties", this::openProperties);
        
        shortcutRCM.showOnCurrent();
    }
    
    @Override
    public void generateSaveString(EStringBuilder sb) {
        generateBaseSaveString(sb);
        sb.println("command: ", commandName);
        sb.println("args: ", args);
        sb.println("openTerminal: ", willOpenTerminal);
        sb.decrementTabCount();
    }
    
    public void parseCommand(String toParse) {
        commandName = toParse;
    }
    
    public void setCommand(TerminalCommand command) {
        commandName = command.getName();
    }
    
    public void setArguments(EList<String> arguments) {
        args.clearThenAddAll(arguments);
    }
    
    public void setCommand(TerminalCommand command, EList<String> arguments) {
        commandName = command.getName();
        args.clearThenAddAll(arguments);
    }
    
    public void setOpenTerminal(boolean val) { willOpenTerminal = val; }
    
    public String getCommandName() { return commandName; }
    public EList<String> getArguments() { return args; }
    public boolean opensTerminal() { return willOpenTerminal; }
    
    public void editCommand() {
        DeveloperDesktop.openWindow(new CommandShortcutEditorWindow(this));
    }
    
}
