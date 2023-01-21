package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.sys.TracingPrintStream;

//Author: Hunter Bragg

public class JavaTrace extends TerminalCommand {
	
	public JavaTrace() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "javatrace"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles print stream tracing in sysout"; }
	@Override public String getUsage() { return "ex: javatrace"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		tabCompleteTF(termIn, args);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (TracingPrintStream.isTracing()) {
			TracingPrintStream.disableTrace();
			termIn.writeln("Disabled tracing", EColors.lgray);
		}
		else {
			TracingPrintStream.enableTrace();
			termIn.writeln("Enabled tracing", EColors.lgreen);
		}
	}
	
}
