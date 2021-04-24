package debug.terminal;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.terminalCommand.commands.fileSystem.Cat;
import debug.terminal.terminalCommand.commands.fileSystem.Cd;
import debug.terminal.terminalCommand.commands.fileSystem.Cp;
import debug.terminal.terminalCommand.commands.fileSystem.Edit;
import debug.terminal.terminalCommand.commands.fileSystem.Head;
import debug.terminal.terminalCommand.commands.fileSystem.Ls;
import debug.terminal.terminalCommand.commands.fileSystem.Lsblk;
import debug.terminal.terminalCommand.commands.fileSystem.MkDir;
import debug.terminal.terminalCommand.commands.fileSystem.Mv;
import debug.terminal.terminalCommand.commands.fileSystem.Open;
import debug.terminal.terminalCommand.commands.fileSystem.Pwd;
import debug.terminal.terminalCommand.commands.fileSystem.Rm;
import debug.terminal.terminalCommand.commands.fileSystem.RmDir;
import debug.terminal.terminalCommand.commands.fileSystem.Tail;
import debug.terminal.terminalCommand.commands.game.CD_Saves;
import debug.terminal.terminalCommand.commands.game.LoadWorld;
import debug.terminal.terminalCommand.commands.game.NoClip;
import debug.terminal.terminalCommand.commands.game.UnloadWorld;
import debug.terminal.terminalCommand.commands.system.CalcCommand;
import debug.terminal.terminalCommand.commands.system.ClearObjects;
import debug.terminal.terminalCommand.commands.system.ClearTerminal;
import debug.terminal.terminalCommand.commands.system.ClearTerminalHistory;
import debug.terminal.terminalCommand.commands.system.DebugControl;
import debug.terminal.terminalCommand.commands.system.ForLoop;
import debug.terminal.terminalCommand.commands.system.Help;
import debug.terminal.terminalCommand.commands.system.ID_CMD;
import debug.terminal.terminalCommand.commands.system.ListCMD;
import debug.terminal.terminalCommand.commands.system.OpenWindow;
import debug.terminal.terminalCommand.commands.system.ReregisterCommands;
import debug.terminal.terminalCommand.commands.system.RuntimeCMD;
import debug.terminal.terminalCommand.commands.system.Shutdown;
import debug.terminal.terminalCommand.commands.system.SystemCMD;
import debug.terminal.terminalCommand.commands.system.Version;
import debug.terminal.terminalCommand.commands.system.WhoAmI;
import debug.terminal.terminalCommand.commands.windows.Close;
import debug.terminal.terminalCommand.commands.windows.MinimizeWindow;
import debug.terminal.terminalCommand.commands.windows.PinWindow;
import debug.terminal.terminalCommand.commands.windows.ShowWindow;
import debug.terminal.terminalCommand.commands.windows.ToFrontWindow;
import debug.terminal.window.ETerminal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import storageUtil.EArrayList;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public class TerminalHandler {

	public static final String version = "1.0";
	private static TerminalHandler instance;
	protected StorageBoxHolder<String, TerminalCommand> commands;
	protected EArrayList<TerminalCommand> commandList;
	protected EArrayList<TerminalCommand> customCommandList;
	public static boolean drawSpace = true;
	public static EArrayList<String> cmdHistory = new EArrayList();
	
	public static TerminalHandler getInstance() {
		return instance == null ? instance = new TerminalHandler() : instance;
	}
	
	private TerminalHandler() {
		commands = new StorageBoxHolder();
		commandList = new EArrayList();
		customCommandList = new EArrayList();
	}
	
	public void initCommands() {
		registerBaseCommands(false);
	}
	
	private void registerBaseCommands(boolean runVisually) { registerBaseCommands(null, runVisually); }
	private void registerBaseCommands(ETerminal termIn, boolean runVisually) {
		
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
		
		//system
		registerCommand(new ID_CMD(), termIn, runVisually);
		registerCommand(new Shutdown(), termIn, runVisually);
		registerCommand(new CalcCommand(), termIn, runVisually);
		registerCommand(new ClearObjects(), termIn, runVisually);
		registerCommand(new ClearTerminal(), termIn, runVisually);
		registerCommand(new ClearTerminalHistory(), termIn, runVisually);
		registerCommand(new Close(), termIn, runVisually);
		registerCommand(new DebugControl(), termIn, runVisually);
		registerCommand(new Help(), termIn, runVisually);
		registerCommand(new ListCMD(), termIn, runVisually);
		registerCommand(new OpenWindow(), termIn, runVisually);
		registerCommand(new ReregisterCommands(), termIn, runVisually);
		registerCommand(new Version(), termIn, runVisually);
		registerCommand(new WhoAmI(), termIn, runVisually);
		registerCommand(new RuntimeCMD(), termIn, runVisually);
		registerCommand(new SystemCMD(), termIn, runVisually);
		registerCommand(new ForLoop(), termIn, runVisually);
		
		//windows
		registerCommand(new PinWindow(), termIn, runVisually);
		registerCommand(new ToFrontWindow(), termIn, runVisually);
		registerCommand(new ShowWindow(), termIn, runVisually);
		registerCommand(new MinimizeWindow(), termIn, runVisually);
		
		//game
		registerCommand(new CD_Saves(), termIn, runVisually);
		registerCommand(new NoClip(), termIn, runVisually);
		registerCommand(new LoadWorld(), termIn, runVisually);
		registerCommand(new UnloadWorld(), termIn, runVisually);
	}
	
	public void registerCommand(TerminalCommand command, boolean runVisually) { registerCommand(command, null, runVisually); }
	public void registerCommand(TerminalCommand command, ETerminal termIn, boolean runVisually) {
		commandList.add(command);
		commands.put(command.getName(), command);
		if (termIn != null & runVisually) { termIn.writeln("Registering command call: " + command.getName(), 0xffffff00); }
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				if (termIn != null & runVisually) { termIn.writeln("Registering command alias: " + command.getAliases().get(i), 0xff55ff55); }
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
			if (emptyEnd) { commandArguments.add(""); }
			
			if (commands.getBoxWithA(baseCommand) != null) {
				TerminalCommand command = commands.getBoxWithA(baseCommand).getB();
				
				if (command == null) {
					termIn.error("Unrecognized command.");
					if (!termIn.isChatTerminal()) {	termIn.writeln(); }
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
					if (command.showInHelp()) { command.handleTabComplete(termIn, commandArguments); }
				}
				else {
					command.runCommand(termIn, commandArguments, runVisually);
				
					if (addSpace && (drawSpace && !command.getName().equals("clear"))) {
						if (!termIn.isChatTerminal()) {	termIn.writeln(); }
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
		
		Iterator<StorageBox<String, TerminalCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getA();
			if (termIn != null && runVisually) { termIn.writeln("Unregistering command alias: " + commandName, 0xffb2b2b2); }
			b.remove();
		}
		
		registerBaseCommands(termIn, runVisually);
		
		customCommandList.forEach(c -> registerCommand(c, termIn, runVisually));
	}
	
	public TerminalCommand getCommand(String commandName) {
		StorageBox<String, TerminalCommand> box = commands.getBoxWithA(commandName);
		if (box != null) {
			return commands.getBoxWithA(commandName).getB();
		}
		return null;
	}
	
	public static EArrayList<String> getSortedCommandNames() {
		EArrayList<String> cmds = new EArrayList();
		StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> sortedAll = getSortedCommands();
		
		for (StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> box : sortedAll) {
			StorageBoxHolder<String, EArrayList<TerminalCommand>> catCommands = box.getB();
			for (StorageBox<String, EArrayList<TerminalCommand>> byCat : catCommands) {
				for (TerminalCommand command : byCat.getB()) {
					if (command.showInHelp()) {
						cmds.add(command.getName());
					}
				}
			}
		}
		
		return cmds;
	}
	
	public static StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> getSortedCommands() {
		StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> sortedCommands = new StorageBoxHolder();
		EArrayList<TerminalCommand> unsorted = TerminalHandler.getInstance().getCommandList();
		
		//filter out commands that should not be shown in help
		unsorted = unsorted.stream().filter(c -> c.showInHelp()).collect(EArrayList.toEArrayList());
		
		//get command categories
		EArrayList<String> categories = new EArrayList();
		for (TerminalCommand c : unsorted) {
			if (c != null) { categories.addIfNotContains(c.getCategory()); }
		}
		Collections.sort(categories);
		
		//collect for each category
		StorageBoxHolder<String, EArrayList<TerminalCommand>> commandsByCategory = new StorageBoxHolder();
		categories.forEach(c -> commandsByCategory.add(c, null));
		EArrayList<TerminalCommand> toProcess = new EArrayList(unsorted);
		
		for (String category : categories) {
			EArrayList<TerminalCommand> byCat = new EArrayList();
			
			Iterator<TerminalCommand> it = toProcess.iterator();
			while (it.hasNext()) {
				TerminalCommand c = it.next();
				if (c.getCategory().equals(category)) {
					byCat.add(c);
					it.remove();
				}
			}
			
			Collections.sort(byCat, new Sorter());
			commandsByCategory.getBoxWithA(category).setB(byCat);
		}
		
		//get command types
		EArrayList<CommandType> types = new EArrayList();
		unsorted.forEach(c -> types.addIfNotContains(c.getType()));
		
		EArrayList<TerminalCommand> typeToProcess = new EArrayList();
		
		//isolate the 'none' category
		if (!commandsByCategory.removeBoxesContainingA("none").isEmpty()) {
			StorageBox<String, EArrayList<TerminalCommand>> noneCat = commandsByCategory.removeBoxesContainingA("none").get(0);
			typeToProcess.addAll(noneCat.getB());
			
			//filter out commands that have a category but are not normal
			for (StorageBox<String, EArrayList<TerminalCommand>> byCat : commandsByCategory) {
				Iterator<TerminalCommand> it = byCat.getB().iterator();
				while (it.hasNext()) {
					TerminalCommand c = it.next();
					if (c.getType() != CommandType.NORMAL) {
						StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> box = sortedCommands.getBoxWithA(c.getType());
						if (box != null) {
							StorageBoxHolder<String, EArrayList<TerminalCommand>> cats = box.getB();
							StorageBox<String, EArrayList<TerminalCommand>> catBox = cats.getBoxWithA(c.getCategory());
							if (catBox != null) {
								catBox.getB().add(c);
							}
							else {
								cats.add(new StorageBox(c.getCategory(), new EArrayList(c)));
							}
						}
						else {
							sortedCommands.add(c.getType(), new StorageBoxHolder(c.getCategory(), new EArrayList(c)));
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
			EArrayList<TerminalCommand> byType = new EArrayList();
			
			Iterator<TerminalCommand> it = typeToProcess.iterator();
			while (it.hasNext()) {
				TerminalCommand t = it.next();
				if (t.getType() == type) {
					byType.add(t);
					it.remove();
				}
			}
			
			if (sortedCommands.getBoxWithA(type) == null) {
				sortedCommands.add(type, new StorageBoxHolder("nocat", byType));
			}
		}
		
		//add any remaining commands in the 'none' category to the normal command type
		sortedCommands.getBoxWithA(CommandType.NORMAL).getB().add(new StorageBox("No Category", typeToProcess));
		
		//ensure correct command type order
		EArrayList<StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>>> commands = sortedCommands.getBoxes();
		sortedCommands.clear();
		Collections.sort(commands, new Comparator<StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>>>() {

			@Override
			public int compare(StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> o1, StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> o2) {
				return o1.getA().compareTo(o2.getA());
			}
			
		});
		sortedCommands.addAll(commands);
		
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
}
