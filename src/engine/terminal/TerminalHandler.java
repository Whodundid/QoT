package engine.terminal;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.util.ClassFinder;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.reflection.EModifier;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
	
	/** Dynamically finds terminal commands within the given classpath directory. */
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
		//only register commands which specifically are marked with 'shouldRegister'
		findCommands().filter(c -> c.shouldRegister()).forEach(c -> registerCommand(c, termIn, runVisually));
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
		BoxList<CommandType, BoxList<String, EArrayList<TerminalCommand>>> sortedAll = getSortedCommands();
		
		for (Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>> box : sortedAll) {
			BoxList<String, EArrayList<TerminalCommand>> catCommands = box.getB();
			for (Box2<String, EArrayList<TerminalCommand>> byCat : catCommands) {
				for (TerminalCommand command : byCat.getB()) {
					if (command.showInHelp()) {
						cmds.add(command.getName());
					}
				}
			}
		}
		
		return cmds;
	}
	
	public static BoxList<CommandType, BoxList<String, EArrayList<TerminalCommand>>> getSortedCommands() {
		BoxList<CommandType, BoxList<String, EArrayList<TerminalCommand>>> sortedCommands = new BoxList();
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
		BoxList<String, EArrayList<TerminalCommand>> commandsByCategory = new BoxList();
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
			Box2<String, EArrayList<TerminalCommand>> noneCat = commandsByCategory.removeBoxesContainingA("none").get(0);
			typeToProcess.addAll(noneCat.getB());
			
			//filter out commands that have a category but are not normal
			for (Box2<String, EArrayList<TerminalCommand>> byCat : commandsByCategory) {
				Iterator<TerminalCommand> it = byCat.getB().iterator();
				while (it.hasNext()) {
					TerminalCommand c = it.next();
					if (c.getType() != CommandType.NORMAL) {
						Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>> box = sortedCommands.getBoxWithA(c.getType());
						if (box != null) {
							BoxList<String, EArrayList<TerminalCommand>> cats = box.getB();
							Box2<String, EArrayList<TerminalCommand>> catBox = cats.getBoxWithA(c.getCategory());
							if (catBox != null) {
								catBox.getB().add(c);
							}
							else {
								cats.add(new Box2(c.getCategory(), new EArrayList(c)));
							}
						}
						else {
							sortedCommands.add(c.getType(), new BoxList(c.getCategory(), new EArrayList(c)));
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
				sortedCommands.add(type, new BoxList("nocat", byType));
			}
		}
		
		//add any remaining commands in the 'none' category to the normal command type
		sortedCommands.getBoxWithA(CommandType.NORMAL).getB().add(new Box2("No Category", typeToProcess));
		
		//ensure correct command type order
		EArrayList<Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>>> commands = sortedCommands.getBoxes();
		sortedCommands.clear();
		Collections.sort(commands, new Comparator<Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>>>() {

			@Override
			public int compare(Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>> o1, Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>> o2) {
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
