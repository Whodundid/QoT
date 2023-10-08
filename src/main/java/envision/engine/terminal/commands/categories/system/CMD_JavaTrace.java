package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.sys.TracingPrintStream;

//Author: Hunter Bragg

public class CMD_JavaTrace extends TerminalCommand {
	
	public CMD_JavaTrace() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "javatrace"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles print stream tracing in sysout"; }
	@Override public String getUsage() { return "ex: javatrace"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		tabCompleteTF(termIn, args);
	}
	
	@Override
	public void runCommand() {
		if (TracingPrintStream.isTracing()) {
			TracingPrintStream.disableTrace();
			writeln("Disabled tracing", EColors.lgray);
		}
		else {
			TracingPrintStream.enableTrace();
			writeln("Enabled tracing", EColors.lgreen);
		}
	}
	
}
