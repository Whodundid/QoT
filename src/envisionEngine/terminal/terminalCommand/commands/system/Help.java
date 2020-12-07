package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.IListableCommand;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import main.Game;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public class Help extends TerminalCommand implements IListableCommand {
	
	public Help() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "help"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("h", "commands", "cmds", "cmd"); }
	@Override public String getHelpInfo(boolean runVisually) { return "List all commands with aliases and can display info on a specific command."; }
	@Override public String getUsage() { return "ex: help deb"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = Game.getTerminalHandler().getSortedCommandNames();
		super.basicTabComplete(termIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 0) {
			list(termIn, args, runVisually);
			termIn.writeln();
			termIn.writeln("To see help information on a specific command, type help followed by the command.", EColors.yellow);
			termIn.writeln("To run a command with more information, add -i after the command. ex: list -i", EColors.yellow);
			
		}
		else if (args.size() == 1) {
			
			String commandName = args.get(0);
			if (Game.getTerminalHandler().getCommandNames().contains(commandName)) {
				TerminalCommand command = Game.getTerminalHandler().getCommand(commandName);
				if (command.showInHelp()) {
					termIn.writeln(command.getHelpInfo(runVisually), 0xffff00);
					if (command.getUsage() != null && !command.getUsage().isEmpty()) {
						termIn.writeln(command.getUsage(), 0xffff00);
					}
				}
				else { termIn.error("Unrecognized command name"); }
			}
			else { termIn.error("Unrecognized command name"); }
		}
		else { termIn.error("Too many arguments!"); }
	}
	
	@Override
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Listing all terminal commands\n", EColors.lgreen);
		for (StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> box : Game.getTerminalHandler().getSortedCommands()) {
			boolean norm = false;
			StorageBoxHolder<String, EArrayList<TerminalCommand>> catHolder = box.getB();
			
			if (box.getA() == CommandType.NORMAL) { termIn.writeln("Built-In", EColors.cyan); norm = true; }
			if (box.getA() == CommandType.APP) { termIn.writeln("\n" + "EMC App Config Settings:", EColors.cyan); }
			if (box.getA() == CommandType.APP_COMMAND) { termIn.writeln("\n" + "EMC App Terminal Commands:", EColors.cyan); norm = true; }
			
			for (StorageBox<String, EArrayList<TerminalCommand>> catCommands : catHolder) {
				EArrayList<TerminalCommand> commands = catCommands.getB();
				boolean notEmpty = commands.isNotEmpty();
				
				if (norm && notEmpty) {
					termIn.writeln("  " + catCommands.getA(), EColors.orange);
				}
				
				for (TerminalCommand command : catCommands.getB()) {
					if (command.getAliases() == null) {
						termIn.writeln((norm ? "    " : "  ") + command.getName(), 0xb2b2b2);
					}
					else {
						String a = EColors.mc_green + "";
						for (int i = 0; i < command.getAliases().size(); i++) {
							String commandAlias = command.getAliases().get(i);
							if (i == command.getAliases().size() - 1) { a += commandAlias; }
							else { a += (commandAlias + ", "); }
						}
						termIn.writeln((norm ? "    " : "  ") + command.getName() + ": " + a, 0xb2b2b2);
					}
				}
				
				//if (norm) { termIn.writeln(); }
			}
		}
	}
	
}
