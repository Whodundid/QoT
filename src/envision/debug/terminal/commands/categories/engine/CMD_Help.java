package envision.debug.terminal.commands.categories.engine;

import envision.debug.terminal.TerminalCommandHandler;
import envision.debug.terminal.commands.IListableCommand;
import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import qot.QoT;

//Author: Hunter Bragg

public class CMD_Help extends TerminalCommand implements IListableCommand {
	
	public CMD_Help() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "help"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("h", "commands", "cmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "List all commands with aliases and can display info on a specific command."; }
	@Override public String getUsage() { return "ex: help deb"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		EList<String> options = TerminalCommandHandler.getSortedCommandNames();
		super.basicTabComplete(termIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			list(termIn, args, runVisually);
			termIn.writeln();
			termIn.writeln("To see help information on a specific command, type help followed by the command.", EColors.yellow);
			termIn.writeln("To run a command with more information, add -i after the command. ex: list -i", EColors.yellow);
			return;
		}
		
		if (args.size() > 1) {
			termIn.error("Too many arguments!");
			return;
		}
		
		String commandName = args.get(0);
		
		var cmds = QoT.getTerminalHandler().getCommandNames();
		if (!cmds.contains(commandName)) {
			termIn.error("Unrecognized command name");
			return;
		}
		
		TerminalCommand command = QoT.getTerminalHandler().getCommand(commandName);
		if (!command.showInHelp()) {
			termIn.error("Unrecognized command name");
			return;
		}
		
		termIn.writeln(command.getHelpInfo(runVisually), 0xffffff00);
		if (command.getUsage() != null && !command.getUsage().isEmpty()) {
			termIn.writeln(command.getUsage(), 0xffffff00);
		}
	}
	
	@Override
	public void list(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		termIn.writeln("Listing all terminal commands\n", EColors.lgreen);
		
		for (var box : TerminalCommandHandler.getSortedCommands()) {
			//print category name
			termIn.writeln("  " +  box.getA(), EColors.orange);
			
			//print each command under the current category
			for (TerminalCommand command : box.getB()) {
				var aliasList = command.getAliases();
				
				//if the command doesn't have any aliases, just print the command name
				if (aliasList == null || aliasList.isEmpty()) {
					termIn.writeln("    " + command.getName(), 0xffb2b2b2);
					continue;
				}
				
				//print the command name followed by all of the command's aliases
				EStringBuilder a = new EStringBuilder();
				a.a(EColors.mc_green);
				
				for (int i = 0; i < command.getAliases().size(); i++) {
					String commandAlias = command.getAliases().get(i);
					if (i == command.getAliases().size() - 1) a.a(commandAlias);
					else a.a(commandAlias, ", ");
				}
				
				termIn.writeln(EColors.lgray, "    ", command.getName(), ": ", a);
			}
		}
	}
	
}
