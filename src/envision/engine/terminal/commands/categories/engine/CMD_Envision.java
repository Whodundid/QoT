package envision.engine.terminal.commands.categories.engine;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionEnvironmnetSettings;
import envision_lang._launch.EnvisionProgram;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Envision extends TerminalCommand {
	
	public CMD_Envision() {
		setCategory("System");
		allowAnyModifier = true;
	}

	@Override public String getName() { return "envision"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Mappings to the Envision Scripting Language"; }
	@Override public String getUsage() { return "ex: envision 'file'"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln(EnvisionLang.getVersionString(), EColors.seafoam);
			termIn.info("To run an Envision script, add an Envision program directory\n",
						"along with any of its launch arguments to the end of this command.");
			termIn.info(getUsage());
			return;
		}
		
//		if (args.isEmpty()) {
//			termIn.writeln("Starting Envision live mode...", EColors.seafoam);
//		}
		
		String fileName = args.getFirst();
		EnvisionProgram program = new EnvisionProgram(fileName);
		
		//arguments to be passed to the Envision Language
		EList<String> toParse = EList.newList();
		
		EList<String> modifiers = getParsedModifiers();
		if (modifiers.size() > 0) {
			for (int i = 0; i < modifiers.size(); i++) {
				toParse.add(modifiers.get(i));
			}
			var launchArgs = EnvisionEnvironmnetSettings.of(toParse);
			program.setLaunchArgs(launchArgs);
		}
		
		try {
			long start = System.currentTimeMillis();
			//QoT.getEnvision().runProgram(program);
			termIn.writeln(EColors.lgreen, "END ", EColors.yellow, (System.currentTimeMillis() - start), "ms");
		}
		catch (Throwable e) {
			e.printStackTrace();
			error(termIn, e);
		}
	}
	
}
