package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
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
    private boolean willOpenTerminal = false;
    
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
            DeveloperDesktop.openTerminal(commandName, args);
        }
        else {
            // find a registered command under the given name
            TerminalCommand command = TerminalCommandHandler.getRegisteredCommand(commandName);
            if (command == null) return;
            
            command.runCommand_i(DUMMY_TERMINAL, args, false);            
        }
    }
    
    @Override
    public void openShortcutRCM() {
        createBaseShortcutRCM();
        
        shortcutRCM.addOption("Run Command", WindowTextures.file_up, this::onDoubleClick);
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
    
}
