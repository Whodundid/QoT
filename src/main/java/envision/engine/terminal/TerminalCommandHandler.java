package envision.engine.terminal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.commands.categories.engine.CMD_Config;
import envision.engine.terminal.commands.categories.engine.CMD_CurScreenInfo;
import envision.engine.terminal.commands.categories.engine.CMD_DebugControl;
import envision.engine.terminal.commands.categories.engine.CMD_DisplayScreen;
import envision.engine.terminal.commands.categories.engine.CMD_Envision;
import envision.engine.terminal.commands.categories.engine.CMD_FPS;
import envision.engine.terminal.commands.categories.engine.CMD_Help;
import envision.engine.terminal.commands.categories.engine.CMD_ReloadTextures;
import envision.engine.terminal.commands.categories.engine.CMD_RenderSettings;
import envision.engine.terminal.commands.categories.engine.CMD_SetVolume;
import envision.engine.terminal.commands.categories.engine.CMD_Shutdown;
import envision.engine.terminal.commands.categories.engine.CMD_TPS;
import envision.engine.terminal.commands.categories.engine.CMD_Version;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Cat;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Cd;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Cp;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Edit;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Files;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Head;
import envision.engine.terminal.commands.categories.fileSystem.CMD_L;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Ls;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Lsblk;
import envision.engine.terminal.commands.categories.fileSystem.CMD_MkDir;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Mv;
import envision.engine.terminal.commands.categories.fileSystem.CMD_OpenFile;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Pwd;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Rm;
import envision.engine.terminal.commands.categories.fileSystem.CMD_Tail;
import envision.engine.terminal.commands.categories.game.CMD_CreateDungeon;
import envision.engine.terminal.commands.categories.game.CMD_GiveItem;
import envision.engine.terminal.commands.categories.game.CMD_God;
import envision.engine.terminal.commands.categories.game.CMD_Kill;
import envision.engine.terminal.commands.categories.game.CMD_ListEntities;
import envision.engine.terminal.commands.categories.game.CMD_LoadWorld;
import envision.engine.terminal.commands.categories.game.CMD_NoClip;
import envision.engine.terminal.commands.categories.game.CMD_PauseGame;
import envision.engine.terminal.commands.categories.game.CMD_PlaySong;
import envision.engine.terminal.commands.categories.game.CMD_ReloadWorld;
import envision.engine.terminal.commands.categories.game.CMD_SaveWorld;
import envision.engine.terminal.commands.categories.game.CMD_SetWorldUnderground;
import envision.engine.terminal.commands.categories.game.CMD_SetXP;
import envision.engine.terminal.commands.categories.game.CMD_SpawnEntity;
import envision.engine.terminal.commands.categories.game.CMD_Speed;
import envision.engine.terminal.commands.categories.game.CMD_TransposeOld;
import envision.engine.terminal.commands.categories.game.CMD_UnloadWorld;
import envision.engine.terminal.commands.categories.game.CMD_WorldInfo;
import envision.engine.terminal.commands.categories.game.CMD_WorldsDir;
import envision.engine.terminal.commands.categories.system.CMD_Alias;
import envision.engine.terminal.commands.categories.system.CMD_Calculator;
import envision.engine.terminal.commands.categories.system.CMD_ClearTerminal;
import envision.engine.terminal.commands.categories.system.CMD_ClearTerminalHistory;
import envision.engine.terminal.commands.categories.system.CMD_Echo;
import envision.engine.terminal.commands.categories.system.CMD_ForLoop;
import envision.engine.terminal.commands.categories.system.CMD_HexToDec;
import envision.engine.terminal.commands.categories.system.CMD_JavaTrace;
import envision.engine.terminal.commands.categories.system.CMD_List;
import envision.engine.terminal.commands.categories.system.CMD_ReregisterCommands;
import envision.engine.terminal.commands.categories.system.CMD_Runtime;
import envision.engine.terminal.commands.categories.system.CMD_SU;
import envision.engine.terminal.commands.categories.system.CMD_System;
import envision.engine.terminal.commands.categories.system.CMD_Unalias;
import envision.engine.terminal.commands.categories.system.CMD_WhoAmI;
import envision.engine.terminal.commands.categories.windows.CMD_ClearObjects;
import envision.engine.terminal.commands.categories.windows.CMD_CloseWindow;
import envision.engine.terminal.commands.categories.windows.CMD_MinimizeWindow;
import envision.engine.terminal.commands.categories.windows.CMD_MoveWindowToFront;
import envision.engine.terminal.commands.categories.windows.CMD_OpenWindow;
import envision.engine.terminal.commands.categories.windows.CMD_PinWindow;
import envision.engine.terminal.commands.categories.windows.CMD_ShowWindow;
import envision.engine.terminal.commands.categories.windows.CMD_TermID;
import envision.engine.terminal.terminalUtil.ClassFinder;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.reflection.EModifier;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

//Author: Hunter Bragg

public class TerminalCommandHandler {

	public static final String version = "1.0";
	private static TerminalCommandHandler instance;
	protected BoxList<String, TerminalCommand> commands;
	public Map<String, String> commandAliases = new HashMap<>();
	protected EList<TerminalCommand> commandList;
	public static boolean drawSpace = true;
	public static EList<String> cmdHistory = new EArrayList<>();
	
	public static TerminalCommandHandler getInstance() {
		return instance == null ? instance = new TerminalCommandHandler() : instance;
	}
	
	private TerminalCommandHandler() {
		commands = new BoxList<>();
		commandList = new EArrayList<>();
	}
	
	public void initCommands() {
		registerBaseCommands(false);
	}
	
	/** Dynamically finds terminal commands within the given classpath directory.
	 * 
	 *  NOTE: THIS ONLY WORKS WHEN RUNNING OUT OF ECLIPSE!!!
	 */
	private EList<TerminalCommand> findCommands() {
		String commandDir = "engine.terminal.terminalCommand";
		
		EList<Class<TerminalCommand>> getCommands = ClassFinder.findClassesOfType(commandDir, TerminalCommand.class);
		EList<TerminalCommand> commands = new EArrayList();
		
		for (Class<TerminalCommand> c : getCommands) {
			EModifier mods = EModifier.of(c.getModifiers());
			if (!mods.isAbstract() && mods.isPublic()) {
				try { commands.add(c.getConstructor().newInstance()); }
				catch (Exception e) { e.printStackTrace(); break; }
			}
		}
		
		return commands;
	}
	
	private void registerBaseCommands(boolean runVisually) { registerBaseCommands(null, runVisually); }
	private void registerBaseCommands(ETerminalWindow termIn, boolean runVisually) {
		//findCommands().filter(c -> c.shouldRegister()).forEach(c -> registerCommand(c, termIn, runVisually));
		
		//define commands to register here
		
		registerCommand(new CMD_TransposeOld(), termIn, runVisually);
		
		//file system
		registerCommand(new CMD_Files(), termIn, runVisually);
		registerCommand(new CMD_L(), termIn, runVisually);
		registerCommand(new CMD_Ls(), termIn, runVisually);
		registerCommand(new CMD_Cd(), termIn, runVisually);
		registerCommand(new CMD_Pwd(), termIn, runVisually);
		registerCommand(new CMD_Rm(), termIn, runVisually);
		registerCommand(new CMD_MkDir(), termIn, runVisually);
		registerCommand(new CMD_Mv(), termIn, runVisually);
		registerCommand(new CMD_Cp(), termIn, runVisually);
		registerCommand(new CMD_Lsblk(), termIn, runVisually);
		registerCommand(new CMD_Cat(), termIn, runVisually);
		registerCommand(new CMD_Head(), termIn, runVisually);
		registerCommand(new CMD_Tail(), termIn, runVisually);
		registerCommand(new CMD_Edit(), termIn, runVisually);
		registerCommand(new CMD_OpenFile(), termIn, runVisually);
		
		//game
		registerCommand(new CMD_CreateDungeon(), termIn, runVisually);
		registerCommand(new CMD_FPS(), termIn, runVisually);
		registerCommand(new CMD_LoadWorld(), termIn, runVisually);
		registerCommand(new CMD_NoClip(), termIn, runVisually);
		registerCommand(new CMD_PauseGame(), termIn, runVisually);
		registerCommand(new CMD_SaveWorld(), termIn, runVisually);
		registerCommand(new CMD_SetWorldUnderground(), termIn, runVisually);
		registerCommand(new CMD_PlaySong(), termIn, runVisually);
		registerCommand(new CMD_SpawnEntity(), termIn, runVisually);
		registerCommand(new CMD_TPS(), termIn, runVisually);
		registerCommand(new CMD_UnloadWorld(), termIn, runVisually);
		registerCommand(new CMD_SetVolume(), termIn, runVisually);
		registerCommand(new CMD_WorldInfo(), termIn, runVisually);
		registerCommand(new CMD_WorldsDir(), termIn, runVisually);
		registerCommand(new CMD_ListEntities(), termIn, runVisually);
		registerCommand(new CMD_Kill(), termIn, runVisually);
		registerCommand(new CMD_ReloadWorld(), termIn, runVisually);
		registerCommand(new CMD_Config(), termIn, runVisually);
		registerCommand(new CMD_God(), termIn, runVisually);
		registerCommand(new CMD_Speed(), termIn, runVisually);
		registerCommand(new CMD_SetXP(), termIn, runVisually);
		registerCommand(new CMD_GiveItem(), termIn, runVisually);
		
		//system
		registerCommand(new CMD_Calculator(), termIn, runVisually);
		registerCommand(new CMD_ClearObjects(), termIn, runVisually);
		registerCommand(new CMD_ClearTerminal(), termIn, runVisually);
		registerCommand(new CMD_ClearTerminalHistory(), termIn, runVisually);
		registerCommand(new CMD_CurScreenInfo(), termIn, runVisually);
		registerCommand(new CMD_DebugControl(), termIn, runVisually);
		registerCommand(new CMD_Envision(), termIn, runVisually);
		registerCommand(new CMD_ForLoop(), termIn, runVisually);
		registerCommand(new CMD_Help(), termIn, runVisually);
		registerCommand(new CMD_HexToDec(), termIn, runVisually);
		registerCommand(new CMD_TermID(), termIn, runVisually);
		registerCommand(new CMD_JavaTrace(), termIn, runVisually);
		registerCommand(new CMD_List(), termIn, runVisually);
		registerCommand(new CMD_DisplayScreen(), termIn, runVisually);
		registerCommand(new CMD_OpenWindow(), termIn, runVisually);
		registerCommand(new CMD_ReloadTextures(), termIn, runVisually);
		registerCommand(new CMD_ReregisterCommands(), termIn, runVisually);
		registerCommand(new CMD_Runtime(), termIn, runVisually);
		registerCommand(new CMD_Shutdown(), termIn, runVisually);
		registerCommand(new CMD_System(), termIn, runVisually);
		registerCommand(new CMD_Version(), termIn, runVisually);
		registerCommand(new CMD_WhoAmI(), termIn, runVisually);
		registerCommand(new CMD_RenderSettings(), termIn, runVisually);
		registerCommand(new CMD_SU(), termIn, runVisually);
		registerCommand(new CMD_Alias(), termIn, runVisually);
		registerCommand(new CMD_Unalias(), termIn, runVisually);
		registerCommand(new CMD_Echo(), termIn, runVisually);
		
		//windows
		registerCommand(new CMD_CloseWindow(), termIn, runVisually);
		registerCommand(new CMD_MinimizeWindow(), termIn, runVisually);
		registerCommand(new CMD_PinWindow(), termIn, runVisually);
		registerCommand(new CMD_ShowWindow(), termIn, runVisually);
		registerCommand(new CMD_MoveWindowToFront(), termIn, runVisually);
	}
	
	public void registerCommand(TerminalCommand command, boolean runVisually) { registerCommand(command, null, runVisually); }
	public void registerCommand(TerminalCommand command, ETerminalWindow termIn, boolean runVisually) {
		//only register commands which specifically are marked with 'shouldRegister'
		if (command == null || !command.shouldRegister()) return;
		
		//add the command to the command map
		commandList.add(command);
		commands.put(command.getName(), command);
		if (termIn != null & runVisually) termIn.writeln("Registering command call: " + command.getName(), 0xffffff00);
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				if (termIn != null & runVisually) termIn.writeln("Registering command alias: " + command.getAliases().get(i), 0xff55ff55);
			}
		}
	}
	
	public void executeCommand(ETerminalWindow termIn, String cmd) { executeCommand(termIn, cmd, false); }
	public void executeCommand(ETerminalWindow termIn, String cmd, boolean tab) {
		cmd = cmd.trim();
		
		// separate multiple commands in one line from one another
		EList<String> splitCommands = parseIndividualCommands(cmd);
		
		// run each command
		for (String singleCommand : splitCommands) {
		    String[] commandParts = singleCommand.split(" ");
	        EList<String> commandArguments = EList.newList();
	        String baseCommand = "";
	        
	        if (commandParts.length == 0) {
	            termIn.writeln("Unrecognized command.\n", 0xffff5555);
	            return;
	        }
	        
	        // parse out arguments from input string
	        baseCommand = commandParts[0].toLowerCase();
	        for (int i = 1; i < commandParts.length; i++) {
	            commandArguments.add(commandParts[i]);
	        }
	        
	        runCommand(termIn, baseCommand, commandArguments, tab);
		}
	}
	
	private void runCommand(ETerminalWindow termIn, String baseCommand, EList<String> commandArguments, boolean tab) {
        // try to find a registered command that matches
        final var foundCommand = commands.getBoxWithA(baseCommand);
        
        if (foundCommand == null) {
            // try to find a command alias instead
            final String foundAlias = commandAliases.get(baseCommand);
            
            // check if this was an alias and execute its replacement if it was
            if (foundAlias != null) {
                var aliasCommand = new EStringBuilder(foundAlias);
                aliasCommand.a(" ");
                aliasCommand.a(EStringUtil.combineAll(commandArguments));
                executeCommand(termIn, aliasCommand.toString(), tab);
                return;
            }
            
            termIn.writeln("Unrecognized command.\n", 0xffff5555);
            return;
        }
        
        // extract the associated command
        TerminalCommand command = commands.getBoxWithA(baseCommand).getB();
        
        if (command == null) {
            termIn.error("Unrecognized command.");
            termIn.writeln();
            return;
        }
        
        boolean runVisually = false;
        var it = commandArguments.iterator();
        while (it.hasNext()) {
            String arg = it.next();
            if (arg.equals("-i")) {
                runVisually = true;
                it.remove();
                break;
            }
        }
        
        if (tab) {
            if (command.showInHelp()) command.handleTabComplete(termIn, commandArguments);
        }
        else {
            command.preRun(termIn, commandArguments, runVisually);
        
            if (drawSpace && !command.getName().equals("clear")) {
                termIn.writeln();
                drawSpace = true;
            }
        }
	}
	
    /**
     * Parses out individual commands that are separated by a ';' but only when
     * the ';' is not in a string
     * 
     * @param input the full input line
     * 
     * @return parsed individual commands from the input line
     */
	private EList<String> parseIndividualCommands(String input) {
	    EList<String> individualCommands = EList.newList();
	    var curCommand = new EStringBuilder();
	    
	    char prevChar = '\0';
	    boolean inStr = false;
	    
	    final int len = input.length();
	    for (int i = 0; i < len; i++) {
	        char c = input.charAt(i);
	        
	        switch (c) {
	        case '\'':
	        case '"':
	            if (prevChar != '\\') inStr = !inStr;
	            break;
	        case ';':
	            if (!inStr) {
	                individualCommands.add(curCommand.toString());
	                curCommand.clear();
	                prevChar = c;
	                continue;
	            }
	            break;
	        default:
	            break;
	        }
	        
	        curCommand.a(c);
	        prevChar = c;
	    }
	    
	    // get the last command off of the buffer
	    if (!curCommand.isEmpty()) {
	        individualCommands.add(curCommand.toString());
	    }
	        
	    return individualCommands;
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) { reregisterAllCommands(null, runVisually); }
	public synchronized void reregisterAllCommands(ETerminalWindow termIn, boolean runVisually) {
		Iterator<TerminalCommand> a = commandList.iterator();
		while (a.hasNext()) {
			String commandName = a.next().getName();
			if (termIn != null && runVisually) { termIn.writeln("Unregistering command: " + commandName, 0xffb2b2b2); }
			a.remove();
		}
		
		Iterator<Box2<String, TerminalCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getA();
			if (termIn != null && runVisually) { termIn.writeln("Unregistering command alias: " + commandName, 0xffb2b2b2); }
			b.remove();
		}
		
		registerBaseCommands(termIn, runVisually);
	}
	
	public TerminalCommand getCommand(String commandName) {
		Box2<String, TerminalCommand> box = commands.getBoxWithA(commandName);
		if (box != null) {
			return commands.getBoxWithA(commandName).getB();
		}
		return null;
	}
	
	public static TerminalCommand tabCompleteCommand(String input) {
	    if (input == null || input.isBlank()) return null;
	    
	    for (var cmd : instance.getCommandList()) {
	        String name = cmd.getName();
	        var aliases = cmd.getAliases();
	        
	        if (input.equals(name)) return cmd;
	        if (EStringUtil.startsWithAny(input, aliases)) return cmd;
	    }
	    
	    return null;
	}
    
    /**
     * Finds and returns a list of all possible command names or aliases that
     * start with the given input.
     * 
     * @param input The starting part of a potential command
     * 
     * @return a list of possible command names that start with the given input
     */
    public static EList<String> buildTabCompleteCommandOptions(String input) {
        EList<String> returnList = EList.newList();
        
        // return the empty list if there isn't any input to check against
        if (input == null || input.isBlank()) return returnList;
        
        // check command names
        for (var cmd : instance.getCommandList()) {
            String name = cmd.getName();
            var aliases = cmd.getAliases();
            
            // check the command name itself
            if (name.startsWith(input)) {
                returnList.add(name);
            }
            
            // then check its aliases
            for (String alias : aliases) {
                if (alias.startsWith(input)) {
                    returnList.add(alias);
                }
            }
        }
        
        // now check command aliases
        for (String alias : instance.getCommandAliases().keySet()) {
            if (alias.startsWith(input)) {
                returnList.add(alias);
            }
        }
        
        return returnList;
    }
    
	public static EList<String> getSortedCommandNames() {
		EList<String> cmds = new EArrayList();
		BoxList<String, EList<TerminalCommand>> sortedAll = getSortedCommands();
		
		for (var box : sortedAll) {
			var catCommands = box.getB();
			for (TerminalCommand command : catCommands) {
				if (command.showInHelp()) {
					cmds.add(command.getName());
				}
			}
		}
		
		return cmds;
	}
	
	public static BoxList<String, EList<TerminalCommand>> getSortedCommands() {
		//the sorted list to be returned
		BoxList<String, EList<TerminalCommand>> sortedCommands = new BoxList<>();
		
		//---------------------------
		// sort commands by category
		//---------------------------
		
		//keep track of commands without a specified category
		EList<TerminalCommand> noneCat = new EArrayList<>();
		
		for (var command : instance.getCommandList()) {
			//get commands declared category
			String category = command.getCategory();
			
			//check if the command actually has a set category, if not, use 'none' by default
			category = (category != null) ? category : "none";
			
			//if category is none, track separately
			if ("none".equals(category)) {
				noneCat.add(command);
				continue;
			}
			
			//get the command list for the current type
			var typeCommands = sortedCommands.get(category);
			
			//check if there is already a command list for the type
			if (typeCommands != null) {
				typeCommands.add(command);
			}
			//otherwise, create a new list, add the current command, and
			//then add the type/list to the BoxList
			else {
				typeCommands = new EArrayList<>();
				typeCommands.add(command);
				sortedCommands.add(category, typeCommands);
			}
		}
		
		//add 'none' category to end of categories
		if (noneCat.isNotEmpty()) sortedCommands.add("No Category", noneCat);
		
		//--------------------------------
		// sort categories alphabetically
		//--------------------------------
		
		//sort the intermediate BoxList by category name alphabetically
		sortedCommands.sort((a, b) -> EStringUtil.compare(a.getA(), b.getA()));
		
		//alphabetically sort each category's command list 
		for (var category : sortedCommands) {
			var catCommands = category.getB();
			catCommands.sort((a, b) -> EStringUtil.compare(a.getName(), b.getName()));
		}
		
		return sortedCommands;
	}
	
	public void addCommandAlias(String aliasIn, String valueIn) {
	    if (aliasIn == null || aliasIn.isBlank()) return;
	    commandAliases.put(aliasIn, valueIn);
	}
	
	public void removeCommandAlias(String aliasIn) {
	    commandAliases.remove(aliasIn);
	}
	
	public EList<TerminalCommand> getCommandList() { return commandList; }
	public Map<String, String> getCommandAliases() { return commandAliases; }
	public EList<String> getCommandNames() { return commands.getAVals(); }
	public EList<String> getCommandAliasNames() { return EList.of(commandAliases.keySet()); }
	public EList<String> getHistory() { return cmdHistory; }
	public TerminalCommandHandler clearHistory() { cmdHistory.clear(); return this; }
	
	/**
	 * Searches for the first terminal instance on the top renderer and returns it.
	 * If there is no terminal instance found, a new one is created and returned.
	 * 
	 * @return The active terminal instance on the top renderer
	 */
	public static ETerminalWindow getActiveTerminal() {
		ETerminalWindow term = Envision.getTopScreen().getTerminalInstance();
		if (term == null) term = new ETerminalWindow();
		return term;
	}
	
}
