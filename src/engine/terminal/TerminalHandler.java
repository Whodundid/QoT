package engine.terminal;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.terminalCommand.commands.fileSystem.Cat;
import engine.terminal.terminalCommand.commands.fileSystem.Cd;
import engine.terminal.terminalCommand.commands.fileSystem.Cp;
import engine.terminal.terminalCommand.commands.fileSystem.Edit;
import engine.terminal.terminalCommand.commands.fileSystem.Head;
import engine.terminal.terminalCommand.commands.fileSystem.Ls;
import engine.terminal.terminalCommand.commands.fileSystem.Lsblk;
import engine.terminal.terminalCommand.commands.fileSystem.MkDir;
import engine.terminal.terminalCommand.commands.fileSystem.Mv;
import engine.terminal.terminalCommand.commands.fileSystem.Open;
import engine.terminal.terminalCommand.commands.fileSystem.Pwd;
import engine.terminal.terminalCommand.commands.fileSystem.Rm;
import engine.terminal.terminalCommand.commands.fileSystem.RmDir;
import engine.terminal.terminalCommand.commands.fileSystem.Tail;
import engine.terminal.terminalCommand.commands.game.CreateDungeon_CMD;
import engine.terminal.terminalCommand.commands.game.FPS_CMD;
import engine.terminal.terminalCommand.commands.game.Kill_CMD;
import engine.terminal.terminalCommand.commands.game.ListEntities_CMD;
import engine.terminal.terminalCommand.commands.game.LoadWorld_CMD;
import engine.terminal.terminalCommand.commands.game.NoClip_CMD;
import engine.terminal.terminalCommand.commands.game.PauseGame_CMD;
import engine.terminal.terminalCommand.commands.game.SaveWorld_CMD;
import engine.terminal.terminalCommand.commands.game.SetWorldUnderground_CMD;
import engine.terminal.terminalCommand.commands.game.Song_CMD;
import engine.terminal.terminalCommand.commands.game.SpawnEntity_CMD;
import engine.terminal.terminalCommand.commands.game.TPS_CMD;
import engine.terminal.terminalCommand.commands.game.UnloadWorld_CMD;
import engine.terminal.terminalCommand.commands.game.World_CMD;
import engine.terminal.terminalCommand.commands.game.WorldsDir_CMD;
import engine.terminal.terminalCommand.commands.system.CalcCommand;
import engine.terminal.terminalCommand.commands.system.ClearObjects;
import engine.terminal.terminalCommand.commands.system.ClearTerminal;
import engine.terminal.terminalCommand.commands.system.ClearTerminalHistory;
import engine.terminal.terminalCommand.commands.system.CurScreen;
import engine.terminal.terminalCommand.commands.system.DebugControl;
import engine.terminal.terminalCommand.commands.system.Envision_CMD;
import engine.terminal.terminalCommand.commands.system.ForLoop;
import engine.terminal.terminalCommand.commands.system.Help;
import engine.terminal.terminalCommand.commands.system.HexToDec;
import engine.terminal.terminalCommand.commands.system.ID_CMD;
import engine.terminal.terminalCommand.commands.system.ListCMD;
import engine.terminal.terminalCommand.commands.system.OpenScreen;
import engine.terminal.terminalCommand.commands.system.OpenWindow;
import engine.terminal.terminalCommand.commands.system.ReloadTextures;
import engine.terminal.terminalCommand.commands.system.ReregisterCommands;
import engine.terminal.terminalCommand.commands.system.RuntimeCMD;
import engine.terminal.terminalCommand.commands.system.Shutdown;
import engine.terminal.terminalCommand.commands.system.SystemCMD;
import engine.terminal.terminalCommand.commands.system.Version;
import engine.terminal.terminalCommand.commands.system.WhoAmI;
import engine.terminal.terminalCommand.commands.windows.Close;
import engine.terminal.terminalCommand.commands.windows.MinimizeWindow;
import engine.terminal.terminalCommand.commands.windows.PinWindow;
import engine.terminal.terminalCommand.commands.windows.ShowWindow;
import engine.terminal.terminalCommand.commands.windows.ToFrontWindow;
import engine.terminal.terminalUtil.ClassFinder;
import engine.terminal.window.ETerminal;
import eutil.EUtil;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.reflection.EModifier;
import main.QoT;

//Author: Hunter Bragg

public class TerminalHandler {

	public static final String version = "1.0";
	private static TerminalHandler instance;
	protected ConcurrentHashMap<String, TerminalCommand> commands;
	protected EList<TerminalCommand> commandList;
	protected EList<TerminalCommand> customCommandList;
	public static boolean drawSpace = true;
	public static EList<String> cmdHistory = new EArrayList<>();
	
	/** The set of registered commands that can be listed using the 'ListCMD'. */
	private final EList<TerminalCommand> listableCommands = new EArrayList<>();
	
	public static TerminalHandler getInstance() {
		return instance == null ? instance = new TerminalHandler() : instance;
	}
	
	private TerminalHandler() {
		commands = new ConcurrentHashMap<>();
		commandList = new EArrayList<>();
		customCommandList = new EArrayList<>();
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
		registerCommand(new World_CMD(), termIn, runVisually);
		registerCommand(new WorldsDir_CMD(), termIn, runVisually);
		registerCommand(new ListEntities_CMD(), termIn, runVisually);
		registerCommand(new Kill_CMD(), termIn, runVisually);
		
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
		registerCommand(new ShowWindow(), termIn, runVisually);
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
		EList<String> commandArguments = new EArrayList<>();
		String baseCommand = "";
		
		if (commandParts.length > 0) {
			baseCommand = commandParts[0].toLowerCase();
			for (int i = 1; i < commandParts.length; i++) {
				commandArguments.add(commandParts[i]);
			}
			if (emptyEnd) commandArguments.add("");
			
			if (commands.get(baseCommand) != null) {
				TerminalCommand command = commands.get(baseCommand);
				
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
		
		var cmds = commands.entrySet().iterator();
		while (cmds.hasNext()) {
			String commandName = cmds.next().getKey();
			if (termIn != null && runVisually) {
				termIn.writeln("Unregistering command alias: " + commandName, 0xffb2b2b2);
			}
			cmds.remove();
		}
		
		registerBaseCommands(termIn, runVisually);
		
		customCommandList.forEach(c -> registerCommand(c, termIn, runVisually));
	}
	
	public TerminalCommand getCommand(String commandName) {
		return commands.getOrDefault(commandName, null);
	}
	
	public static EList<String> getSortedCommandNames() {
		EList<String> cmds = new EArrayList();
		BoxList<CommandType, BoxList<String, EList<TerminalCommand>>> sortedAll = getSortedCommands();
		
		for (var box : sortedAll) {
			var catCommands = box.getB();
			for (var byCat : catCommands) {
				for (var command : byCat.getB()) {
					if (command.showInHelp()) {
						cmds.add(command.getName());
					}
				}
			}
		}
		
		return cmds;
	}
	
	public static BoxList<CommandType, BoxList<String, EList<TerminalCommand>>> getSortedCommands() {
		BoxList<CommandType, BoxList<String, EList<TerminalCommand>>> sortedCommands = new BoxList<>();
		var unsorted = TerminalHandler.getInstance().getCommandList();
		
		//filter out commands that should not be shown in help
		unsorted = unsorted.filter(c -> c.showInHelp());
		
		//get command categories
		EList<String> categories = new EArrayList<>();
		for (var c : unsorted) {
			if (c != null) categories.addIfNotContains(c.getCategory());
		}
		Collections.sort(categories);
		
		//collect for each category
		BoxList<String, EList<TerminalCommand>> commandsByCategory = new BoxList<>();
		categories.forEach(c -> commandsByCategory.add(c, null));
		var toProcess = new EArrayList<>(unsorted);
		
		for (String category : categories) {
			EList<TerminalCommand> byCat = new EArrayList<>();
			
			var it = toProcess.iterator();
			while (it.hasNext()) {
				var c = it.next();
				if (c.getCategory().equals(category)) {
					byCat.add(c);
					it.remove();
				}
			}
			
			Collections.sort(byCat, CommandSorter);
			commandsByCategory.getBoxWithA(category).setB(byCat);
		}
		
		//get command types
		EList<CommandType> types = new EArrayList<>();
		EList<TerminalCommand> typeToProcess = new EArrayList<>();
		unsorted.forEach(c -> types.addIfNotContains(c.getType()));
		
		//isolate the 'none' category
		if (!commandsByCategory.removeBoxesContainingA("none").isEmpty()) {
			var noneCat = commandsByCategory.removeBoxesContainingA("none").get(0);
			typeToProcess.addAll(noneCat.getB());
			
			//filter out commands that have a category but are not normal
			for (var byCat : commandsByCategory) {
				var it = byCat.getB().iterator();
				while (it.hasNext()) {
					var c = it.next();
					if (c.getType() != CommandType.NORMAL) {
						var box = sortedCommands.getBoxWithA(c.getType());
						if (box != null) {
							var cats = box.getB();
							var catBox = cats.getBoxWithA(c.getCategory());
							if (catBox != null) {
								catBox.getB().add(c);
							}
							else {
								cats.add(new Box2(c.getCategory(), new EArrayList<>(c)));
							}
						}
						else {
							sortedCommands.add(c.getType(), new BoxList<>(c.getCategory(), new EArrayList<>(c)));
						}
						it.remove();
					}
				}
			}
		}
		
		
		//add all other command categories except for 'none'
		sortedCommands.add(CommandType.NORMAL, commandsByCategory);
		
		//parse 'none' category for different command types
		for (CommandType type : types) {
			if (type == CommandType.NORMAL) { continue; }
			EList<TerminalCommand> byType = new EArrayList();
			
			Iterator<TerminalCommand> it = typeToProcess.iterator();
			while (it.hasNext()) {
				TerminalCommand t = it.next();
				if (t.getType() == type) {
					byType.add(t);
					it.remove();
				}
			}
			
			if (sortedCommands.getBoxWithA(type) == null) {
				sortedCommands.add(type, new BoxList("nocat", byType));
			}
		}
		
		//add any remaining commands in the 'none' category to the normal command type
		sortedCommands.getBoxWithA(CommandType.NORMAL).getB().add(new Box2("No Category", typeToProcess));
		
		//ensure correct command type order
		EList<Box2<CommandType, BoxList<String, EList<TerminalCommand>>>> commands = sortedCommands.getBoxes();
		sortedCommands.clear();
		Collections.sort(commands, new Comparator<Box2<CommandType, BoxList<String, EList<TerminalCommand>>>>() {
			@Override
			public int compare(Box2<CommandType, BoxList<String, EList<TerminalCommand>>> o1, Box2<CommandType, BoxList<String, EList<TerminalCommand>>> o2) {
				return o1.getA().compareTo(o2.getA());
			}
		});
		sortedCommands.addAll(commands);
		
		return sortedCommands;
	}
	
	private static final Comparator<TerminalCommand> CommandSorter = (a, b) -> a.getName().compareToIgnoreCase(b.getName());
	
	public List<String> getCommandNames() { return EUtil.mapList(commands, e -> e.getKey()); }
	public EList<TerminalCommand> getListableCommands() { return listableCommands; }
	public EList<TerminalCommand> getCommandList() { return commandList; }
	public EList<String> getHistory() { return cmdHistory; }
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
