package envision.terminal.terminalCommand.commands.system;

import envision.terminal.TerminalHandler;
import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.IListableCommand;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import game.QoT;

//Author: Hunter Bragg

public class Help extends TerminalCommand implements IListableCommand {
	
	public Help() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "help"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("h", "commands", "cmds", "cmd"); }
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
		else {
			if (args.size() > 1) { termIn.error("Too many arguments!"); return; }
			String commandName = args.get(0);
			
			var cmds = QoT.getTerminalHandler().getCommandNames();
			if (!cmds.contains(commandName)) { termIn.error("Unrecognized command name"); return; }
			TerminalCommand command = QoT.getTerminalHandler().getCommand(commandName);
			
			if (!command.showInHelp()) { termIn.error("Unrecognized command name"); return; }
			termIn.writeln(command.getHelpInfo(runVisually), 0xffffff00);
			if (command.getUsage() != null && !command.getUsage().isEmpty()) {
				termIn.writeln(command.getUsage(), 0xffffff00);
			}
		}
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
							if (i == command.getAliases().size() - 1) a += commandAlias;
							else a += (commandAlias + ", ");
						}
						termIn.writeln((norm ? "    " : "  ") + command.getName() + ": " + a, 0xffb2b2b2);
					}
				}
				
				//if (norm) { termIn.writeln(); }
			}
		}
	}
	
}
