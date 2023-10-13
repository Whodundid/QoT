package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.strings.EStringUtil;

//Author: Hunter Bragg

public class CMD_Echo extends TerminalCommand {
	
	public CMD_Echo() {
		setCategory("System");
	}

	@Override public String getName() { return "echo"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Write to the terminal"; }
	@Override public String getUsage() { return "ex: echo 'hello world'"; }
	
	@Override
	public void runCommand() {
		expectAtLeast(1);
		
		String toPrint = EStringUtil.combineAll(args(), " ");
		
        // trim out ' and " characters
        if (EStringUtil.startsAndEndsWithAny(toPrint, "'", "\"")) {
            toPrint = toPrint.substring(1, toPrint.length() - 1);
        }
		
		writeln(toPrint);
	}
	
}
