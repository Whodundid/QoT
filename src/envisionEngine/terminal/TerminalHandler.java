package envision.terminal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.terminalCommand.commands.fileSystem.Cat;
import envision.terminal.terminalCommand.commands.fileSystem.Cd;
import envision.terminal.terminalCommand.commands.fileSystem.Cp;
import envision.terminal.terminalCommand.commands.fileSystem.Edit;
import envision.terminal.terminalCommand.commands.fileSystem.Head;
import envision.terminal.terminalCommand.commands.fileSystem.Ls;
import envision.terminal.terminalCommand.commands.fileSystem.Lsblk;
import envision.terminal.terminalCommand.commands.fileSystem.MkDir;
import envision.terminal.terminalCommand.commands.fileSystem.Mv;
import envision.terminal.terminalCommand.commands.fileSystem.Open;
import envision.terminal.terminalCommand.commands.fileSystem.Pwd;
import envision.terminal.terminalCommand.commands.fileSystem.Rm;
import envision.terminal.terminalCommand.commands.fileSystem.RmDir;
import envision.terminal.terminalCommand.commands.fileSystem.Tail;
import envision.terminal.terminalCommand.commands.game.Config_CMD;
import envision.terminal.terminalCommand.commands.game.CreateDungeon_CMD;
import envision.terminal.terminalCommand.commands.game.FPS_CMD;
import envision.terminal.terminalCommand.commands.game.God_CMD;
import envision.terminal.terminalCommand.commands.game.Kill_CMD;
import envision.terminal.terminalCommand.commands.game.ListEntities_CMD;
import envision.terminal.terminalCommand.commands.game.LoadWorld_CMD;
import envision.terminal.terminalCommand.commands.game.NoClip_CMD;
import envision.terminal.terminalCommand.commands.game.PauseGame_CMD;
import envision.terminal.terminalCommand.commands.game.ReloadWorld_CMD;
import envision.terminal.terminalCommand.commands.game.SaveWorld_CMD;
import envision.terminal.terminalCommand.commands.game.SetWorldUnderground_CMD;
import envision.terminal.terminalCommand.commands.game.Song_CMD;
import envision.terminal.terminalCommand.commands.game.SpawnEntity_CMD;
import envision.terminal.terminalCommand.commands.game.TPS_CMD;
import envision.terminal.terminalCommand.commands.game.UnloadWorld_CMD;
import envision.terminal.terminalCommand.commands.game.Volume_CMD;
import envision.terminal.terminalCommand.commands.game.World_CMD;
import envision.terminal.terminalCommand.commands.game.WorldsDir_CMD;
import envision.terminal.terminalCommand.commands.system.CalcCommand;
import envision.terminal.terminalCommand.commands.system.ClearObjects;
import envision.terminal.terminalCommand.commands.system.ClearTerminal;
import envision.terminal.terminalCommand.commands.system.ClearTerminalHistory;
import envision.terminal.terminalCommand.commands.system.CurScreen;
import envision.terminal.terminalCommand.commands.system.DebugControl;
import envision.terminal.terminalCommand.commands.system.Envision_CMD;
import envision.terminal.terminalCommand.commands.system.ForLoop;
import envision.terminal.terminalCommand.commands.system.Help;
import envision.terminal.terminalCommand.commands.system.HexToDec;
import envision.terminal.terminalCommand.commands.system.ID_CMD;
import envision.terminal.terminalCommand.commands.system.JavaTrace;
import envision.terminal.terminalCommand.commands.system.ListCMD;
import envision.terminal.terminalCommand.commands.system.OpenScreen;
import envision.terminal.terminalCommand.commands.system.OpenWindow;
import envision.terminal.terminalCommand.commands.system.ReloadTextures;
import envision.terminal.terminalCommand.commands.system.ReregisterCommands;
import envision.terminal.terminalCommand.commands.system.RuntimeCMD;
import envision.terminal.terminalCommand.commands.system.Shutdown;
import envision.terminal.terminalCommand.commands.system.SystemCMD;
import envision.terminal.terminalCommand.commands.system.Version;
import envision.terminal.terminalCommand.commands.system.WhoAmI;
import envision.terminal.terminalCommand.commands.windows.Close;
import envision.terminal.terminalCommand.commands.windows.DisplayWindow;
import envision.terminal.terminalCommand.commands.windows.MinimizeWindow;
import envision.terminal.terminalCommand.commands.windows.PinWindow;
import envision.terminal.terminalCommand.commands.windows.ToFrontWindow;
import envision.terminal.terminalUtil.ClassFinder;
import envision.terminal.window.ETerminal;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.reflection.EModifier;
import eutil.strings.EStringUtil;
import game.QoT;

//Author: Hunter Bragg

public class TerminalHandler {

	public static final String version = "1.0";
	private static TerminalHandler instance;
	protected BoxList<String, TerminalCommand> commands;
	protected EArrayList<TerminalCommand> commandList;
	protected EArrayList<TerminalCommand> customCommandList;
	public static boolean drawSpace = true;
	public static EArrayList<String> cmdHistory = new EArrayList();
	
	public static TerminalHandler getInstance() {
		return instance == null ? instance = new TerminalHandler() : instance;
	}
	
	private TerminalHandler() {
		commands = new BoxList();
		commandList = new EArrayList();
		customCommandList = new EArrayList();
	}
	
	public void initCommands() {
		registerBaseCommands(false);
	}
	
	/** Dynamically finds terminal commands within the given classpath directory.
	 * 
	 *  NOTE: THIS ONLY WORKS WHEN RUNNING OUT OF ECLIPSE!!!
	 */
	private EArrayList<TerminalCommand> findCommands() {
		String commandDir = "engine.terminal.terminalCommand";
		
		EArrayList<Class<TerminalCommand>> getCommands = ClassFinder.findClassesOfType(commandDir, TerminalCommand.class);
		EArrayList<TerminalCommand> commands = new EArrayList();
		
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
	private void registerBaseCommands(ETerminal termIn, boolean runVisually) {
		//findCommands().filter(c -> c.shouldRegister()).forEach(c -> registerCommand(c, termIn, runVisually));
		
		//define commands to register here
		
		//file system
		registerCommand(new Ls(), termIn, runVisually);
		registerCommand(new Cd(), termIn, runVisually);
		registerCommand(new Pwd(), termIn, runVisually);
		registerCommand(new Rm(), termIn, runVisually);
		registerCommand(new RmDir(), termIn, runVisually);
		registerCommand(new MkDir(), termIn, runVisually);
		registerCommand(new Mv(), termIn, runVisually);
		registerCommand(new Cp(), termIn, runVisually);
		registerCommand(new Lsblk(), termIn, runVisually);
		registerCommand(new Cat(), termIn, runVisually);
		registerCommand(new Head(), termIn, runVisually);
		registerCommand(new Tail(), termIn, runVisually);
		registerCommand(new Edit(), termIn, runVisually);
		registerCommand(new Open(), termIn, runVisually);
		
		//game
		registerCommand(new CreateDungeon_CMD(), termIn, runVisually);
		registerCommand(new FPS_CMD(), termIn, runVisually);
		registerCommand(new LoadWorld_CMD(), termIn, runVisually);
		registerCommand(new NoClip_CMD(), termIn, runVisually);
		registerCommand(new PauseGame_CMD(), termIn, runVisually);
		registerCommand(new SaveWorld_CMD(), termIn, runVisually);
		registerCommand(new SetWorldUnderground_CMD(), termIn, runVisually);
		registerCommand(new Song_CMD(), termIn, runVisually);
		registerCommand(new SpawnEntity_CMD(), termIn, runVisually);
		registerCommand(new TPS_CMD(), termIn, runVisually);
		registerCommand(new UnloadWorld_CMD(), termIn, runVisually);
		registerCommand(new Volume_CMD(), termIn, runVisually);
		registerCommand(new World_CMD(), termIn, runVisually);
		registerCommand(new WorldsDir_CMD(), termIn, runVisually);
		registerCommand(new ListEntities_CMD(), termIn, runVisually);
		registerCommand(new Kill_CMD(), termIn, runVisually);
		registerCommand(new ReloadWorld_CMD(), termIn, runVisually);
		registerCommand(new Config_CMD(), termIn, runVisually);
		registerCommand(new God_CMD(), termIn, runVisually);
		
		//system
		registerCommand(new CalcCommand(), termIn, runVisually);
		registerCommand(new ClearObjects(), termIn, runVisually);
		registerCommand(new ClearTerminal(), termIn, runVisually);
		registerCommand(new ClearTerminalHistory(), termIn, runVisually);
		registerCommand(new CurScreen(), termIn, runVisually);
		registerCommand(new DebugControl(), termIn, runVisually);
		registerCommand(new Envision_CMD(), termIn, runVisually);
		registerCommand(new ForLoop(), termIn, runVisually);
		registerCommand(new Help(), termIn, runVisually);
		registerCommand(new HexToDec(), termIn, runVisually);
		registerCommand(new ID_CMD(), termIn, runVisually);
		registerCommand(new JavaTrace(), termIn, runVisually);
		registerCommand(new ListCMD(), termIn, runVisually);
		registerCommand(new OpenScreen(), termIn, runVisually);
		registerCommand(new OpenWindow(), termIn, runVisually);
		registerCommand(new ReloadTextures(), termIn, runVisually);
		registerCommand(new ReregisterCommands(), termIn, runVisually);
		registerCommand(new RuntimeCMD(), termIn, runVisually);
		registerCommand(new Shutdown(), termIn, runVisually);
		registerCommand(new SystemCMD(), termIn, runVisually);
		registerCommand(new Version(), termIn, runVisually);
		registerCommand(new WhoAmI(), termIn, runVisually);
		
		//windows
		registerCommand(new Close(), termIn, runVisually);
		registerCommand(new MinimizeWindow(), termIn, runVisually);
		registerCommand(new PinWindow(), termIn, runVisually);
		registerCommand(new DisplayWindow(), termIn, runVisually);
		registerCommand(new ToFrontWindow(), termIn, runVisually);
	}
	
	public void registerCommand(TerminalCommand command, boolean runVisually) { registerCommand(command, null, runVisually); }
	public void registerCommand(TerminalCommand command, ETerminal termIn, boolean runVisually) {
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
	
	public void executeCommand(ETerminal termIn, String cmd) { executeCommand(termIn, cmd, false, true); }
	public void executeCommand(ETerminal termIn, String cmd, boolean tab) { executeCommand(termIn, cmd, tab, true); }
	public void executeCommand(ETerminal termIn, String cmd, boolean tab, boolean addSpace) {
		boolean emptyEnd = cmd.endsWith(" ");
		
		cmd = cmd.trim();
		String[] commandParts = cmd.split(" ");
		EArrayList<String> commandArguments = new EArrayList();
		String baseCommand = "";
		
		if (commandParts.length > 0) {
			baseCommand = commandParts[0].toLowerCase();
			for (int i = 1; i < commandParts.length; i++) {
				commandArguments.add(commandParts[i]);
			}
			if (emptyEnd) commandArguments.add("");
			
			if (commands.getBoxWithA(baseCommand) != null) {
				TerminalCommand command = commands.getBoxWithA(baseCommand).getB();
				
				if (command == null) {
					termIn.error("Unrecognized command.");
					if (!termIn.isChatTerminal()) termIn.writeln();
					return;
				}
				
				boolean runVisually = false;
				Iterator<String> i = commandArguments.iterator();
				while (i.hasNext()) {
					String arg = i.next();
					if (arg.equals("-i")) {
						runVisually = true;
						i.remove();
						break;
					}
				}
				
				if (tab) {
					if (command.showInHelp()) command.handleTabComplete(termIn, commandArguments);
				}
				else {
					command.preRun(termIn, commandArguments, runVisually);
				
					if (addSpace && (drawSpace && !command.getName().equals("clear"))) {
						if (!termIn.isChatTerminal()) termIn.writeln();
						drawSpace = true;
					}
				}
				
				return;
			}
		}
		termIn.writeln("Unrecognized command." + (!termIn.isChatTerminal() ? "\n" : ""), 0xffff5555);
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) { reregisterAllCommands(null, runVisually); }
	public synchronized void reregisterAllCommands(ETerminal termIn, boolean runVisually) {
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
		
		customCommandList.forEach(c -> registerCommand(c, termIn, runVisually));
	}
	
	public TerminalCommand getCommand(String commandName) {
		Box2<String, TerminalCommand> box = commands.getBoxWithA(commandName);
		if (box != null) {
			return commands.getBoxWithA(commandName).getB();
		}
		return null;
	}
	
	public static EArrayList<String> getSortedCommandNames() {
		EArrayList<String> cmds = new EArrayList();
		BoxList<String, EArrayList<TerminalCommand>> sortedAll = getSortedCommands();
		
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
	
	public static BoxList<String, EArrayList<TerminalCommand>> getSortedCommands() {
		//the sorted list to be returned
		BoxList<String, EArrayList<TerminalCommand>> sortedCommands = new BoxList<>();
		
		//---------------------------
		// sort commands by category
		//---------------------------
		
		//keep track of commands without a specified category
		EArrayList<TerminalCommand> noneCat = new EArrayList<>();
		
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
	
	private static class Sorter implements Comparator {

		@Override
		public int compare(Object a, Object b) {
			
			if (a instanceof TerminalCommand && b instanceof TerminalCommand) {
				String name1 = ((TerminalCommand) a).getName();
				String name2 = ((TerminalCommand) b).getName();
				
				return name1.compareToIgnoreCase(name2);
			}
			
			return 0;
		}
		
	}
	
	public EArrayList<TerminalCommand> getCommandList() { return commandList; }
	public List<String> getCommandNames() { return commands.getAVals(); }
	public EArrayList<String> getHistory() { return cmdHistory; }
	public TerminalHandler clearHistory() { cmdHistory.clear(); return this; }
	
	/**
	 * Searches for the first terminal instance on the top renderer and returns it.
	 * If there is no terminal instance found, a new one is created and returned.
	 * 
	 * @return The active terminal instance on the top renderer
	 */
	public static ETerminal getActiveTerminal() {
		ETerminal term = QoT.getTopRenderer().getTerminalInstance();
		if (term == null) term = new ETerminal();
		return term;
	}
	
}
