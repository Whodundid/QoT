package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.gameEngine.effects.sounds.SoundEngine;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.settings.QoTSettings;

public class Volume_CMD extends TerminalCommand {
	
	public Volume_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "volume"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("vol"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays or changes the game volume"; }
	@Override public String getUsage() { return "ex: vol 50"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln(EColors.yellow, "Volume: " + QoTSettings.musicVolume.get());
			return;
		}
		
		int vol = ENumUtil.parseInt(args, 0, -1);
		if (vol < 0) {
			termIn.errorUsage("Expected an integer value!", getUsage());
			return;
		}
		QoTSettings.musicVolume.set(ENumUtil.clamp(vol, 0, 100));
		if (QoTSettings.musicVolume.get() == 0) SoundEngine.stopAll();
		else SoundEngine.getAllPlaying().forEach(s -> s.setVolume(QoTSettings.musicVolume.get() * 0.001));
		termIn.writeln(EColors.lgreen, "Set volume to: " + QoTSettings.musicVolume.get());
	}
	
}
