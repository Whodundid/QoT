package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.ListableCommand;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;

//Author: Hunter Bragg

public class CMD_Help extends ListableCommand {
	
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
	public void runCommand() {
	    expectNoMoreThan(1);
	    
		if (noArgs()) {
			displayList(term());
			writeln();
			writeln("To see help information on a specific command, type help followed by the command.", EColors.yellow);
			writeln("To run a command with more information, add -i after the command. ex: list -i", EColors.yellow);
			return;
		}
		
		String commandName = firstArg();
		
		var cmds = Envision.getTerminalHandler().getCommandNames();
		if (!cmds.contains(commandName)) {
			error("Unrecognized command name");
			return;
		}
		
		TerminalCommand command = Envision.getTerminalHandler().getCommand(commandName);
		if (!command.showInHelp()) {
			error("Unrecognized command name");
			return;
		}
		
		writeln(command.getHelpInfo(runVisually()), 0xffffff00);
		if (command.getUsage() != null && !command.getUsage().isEmpty()) {
			writeln(command.getUsage(), 0xffffff00);
		}
	}
	
	public void showHelp(ETerminalWindow termIn) {
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
	
	@Override
	public boolean hasCustomizedListDisplay() {
        return true;
    }
	
	@Override
    public void displayList(ETerminalWindow termIn) {
        showHelp(termIn);
    }
	
	@Override
	public EList<String> getList() {
		return TerminalCommandHandler.getSortedCommandNames();
	}
	
}
