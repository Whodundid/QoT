package engine.terminal.terminalCommand.commands.system;

import engine.terminal.TerminalHandler;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.IListableCommand;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;
import main.QoT;

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
		EArrayList<String> options = TerminalHandler.getSortedCommandNames();
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
			if (QoT.getTerminalHandler().getCommandNames().contains(commandName)) {
				TerminalCommand command = QoT.getTerminalHandler().getCommand(commandName);
				if (command.showInHelp()) {
					termIn.writeln(command.getHelpInfo(runVisually), 0xffffff00);
					if (command.getUsage() != null && !command.getUsage().isEmpty()) {
						termIn.writeln(command.getUsage(), 0xffffff00);
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
		for (Box2<CommandType, BoxList<String, EArrayList<TerminalCommand>>> box : TerminalHandler.getSortedCommands()) {
			boolean norm = false;
			BoxList<String, EArrayList<TerminalCommand>> catHolder = box.getB();
			
			if (box.getA() == CommandType.NORMAL) { termIn.writeln("Built-In", EColors.cyan); norm = true; }
			if (box.getA() == CommandType.APP) { termIn.writeln("\n" + "EMC App Config Settings:", EColors.cyan); }
			if (box.getA() == CommandType.APP_COMMAND) { termIn.writeln("\n" + "EMC App Terminal Commands:", EColors.cyan); norm = true; }
			
			for (Box2<String, EArrayList<TerminalCommand>> catCommands : catHolder) {
				EArrayList<TerminalCommand> commands = catCommands.getB();
				boolean notEmpty = commands.isNotEmpty();
				
				if (norm && notEmpty) {
					termIn.writeln("  " + catCommands.getA(), EColors.orange);
				}
				
				for (TerminalCommand command : catCommands.getB()) {
					if (command.getAliases() == null) {
						termIn.writeln((norm ? "    " : "  ") + command.getName(), 0xffb2b2b2);
					}
					else {
						String a = EColors.mc_green + "";
						for (int i = 0; i < command.getAliases().size(); i++) {
							String commandAlias = command.getAliases().get(i);
							if (i == command.getAliases().size() - 1) { a += commandAlias; }
							else { a += (commandAlias + ", "); }
						}
						termIn.writeln((norm ? "    " : "  ") + command.getName() + ": " + a, 0xffb2b2b2);
					}
				}
				
				//if (norm) { termIn.writeln(); }
			}
		}
	}
	
}
