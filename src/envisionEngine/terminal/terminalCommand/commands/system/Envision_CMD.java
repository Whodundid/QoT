package envision.terminal.terminalCommand.commands.system;

import java.util.List;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionLaunchSettings;
import envision_lang._launch.EnvisionProgram;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

public class Envision_CMD extends TerminalCommand {
	
	public Envision_CMD() {
		setCategory("System");
		allowAnyModifier = true;
	}

	@Override public String getName() { return "envision"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Mappings to the Envision Scripting Language"; }
	@Override public String getUsage() { return "ex: envision 'file'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln(EnvisionLang.getVersionString(), EColors.seafoam);
			termIn.info("To run an Envision script, add an Envision program directory\n",
						"along with any of its launch arguments to the end of this command.");
			termIn.info(getUsage());
			return;
		}
		
		String fileName = args.getFirst();
		EnvisionProgram program = new EnvisionProgram(fileName);
		
		//arguments to be passed to the Envision Language
		List<String> toParse = new EArrayList<>();
		
		List<String> modifiers = getParsedModifiers();
		if (modifiers.size() > 0) {
			for (int i = 0; i < modifiers.size(); i++) {
				toParse.add(modifiers.get(i));
			}
			var launchArgs = EnvisionLaunchSettings.of(toParse);
			program.setLaunchArgs(launchArgs);
		}
		
		try {
			long start = System.currentTimeMillis();
			QoT.getEnvision().runProgram(program);
			termIn.writeln(EColors.lgreen, "END ", EColors.yellow, (System.currentTimeMillis() - start), "ms");
		}
		catch (Throwable e) {
			e.printStackTrace();
			error(termIn, e);
		}
	}
	
}
