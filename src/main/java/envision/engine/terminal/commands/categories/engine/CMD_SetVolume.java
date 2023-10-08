package envision.engine.terminal.commands.categories.engine;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.effects.sounds.SoundEngine;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.settings.QoTSettings;

public class CMD_SetVolume extends TerminalCommand {
	
	public CMD_SetVolume() {
		setCategory("Engine");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "volume"; }
	@Override public EList<String> getAliases() { return EList.of("vol"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays or changes the game volume"; }
	@Override public String getUsage() { return "ex: vol 50"; }
	
	@Override
	public Object runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln(EColors.yellow, "Volume: " + QoTSettings.musicVolume.get());
			return null;
		}
		
		int vol = ENumUtil.parseInt(args, 0, -1);
		if (vol < 0) {
			termIn.errorUsage("Expected an integer value!", getUsage());
			return null;
		}
		
		QoTSettings.musicVolume.set(ENumUtil.clamp(vol, 0, 100));
		
		if (QoTSettings.musicVolume.get() == 0) SoundEngine.stopAll();
		else SoundEngine.getAllPlaying().forEach(s -> s.setVolume(QoTSettings.musicVolume.get() * 0.001));
		
		termIn.writeln(EColors.lgreen, "Set volume to: " + QoTSettings.musicVolume.get());
		return null;
	}
	
}
