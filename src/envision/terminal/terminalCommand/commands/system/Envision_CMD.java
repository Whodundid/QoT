package envision.terminal.terminalCommand.commands.system;

import java.io.File;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import envision_lang.EnvisionLang;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

public class Envision_CMD extends TerminalCommand {
	
	public Envision_CMD() {
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "envision"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Mappings to the Envision Scripting Language"; }
	@Override public String getUsage() { return "ex: envision 'file'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln(EnvisionLang.getVersionString(), EColors.seafoam);
			termIn.info("To run an Envision script, add the file name to the end of this command.");
			termIn.info(getUsage());
		}
		else if (args.hasOne()) {
			String fileName = args.getFirst();
			//fileName = (fileName.endsWith(".nvis")) ? fileName : fileName + ".nvis";
			File f = new File(fileName);
			
			try {
				QoT.getEnvision().runProgram(f);
			}
			catch (Exception e) {
				e.printStackTrace();
				error(termIn, e);
			}
		}
	}
	
}
