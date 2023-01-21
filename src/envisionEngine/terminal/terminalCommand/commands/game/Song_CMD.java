package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.gameEngine.effects.sounds.Audio;
import envisionEngine.gameEngine.effects.sounds.SoundEngine;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.assets.sounds.Songs;
import qot.settings.QoTSettings;

public class Song_CMD extends TerminalCommand {
	
	public Song_CMD() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "song"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("play"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Plays the selected song."; }
	@Override public String getUsage() { return "ex: song "; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		EArrayList<String> range = new EArrayList<>();
		for (int i = 0; i < Songs.songList.size(); i++) {
			range.add(String.valueOf(i));
		}
		
		termIn.buildTabCompletions(range);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			displaySongList(termIn);
			return;
		}
		
		if (args.length() != 1) termIn.error(getUsage());
		
		int song = ENumUtil.parseInt(args, 0, -1);
		SoundEngine.stopAll();
		if (song >= 0) playSong(termIn, song);
		else playSong(termIn, args.get(0));
	}
	
	private void displaySongList(ETerminal termIn) {
		int i = 0;
		for (Audio s : Songs.songList) {
			termIn.writeln(String.format("%2d", i++), " : ", s.getName());
		}
	}
	
	private void playSong(ETerminal termIn, int songNum) {
		try {
			Audio theSong = Songs.songList.get(songNum);
			SoundEngine.play(theSong);
			theSong.setVolume(QoTSettings.musicVolume.get() * 0.001);
		}
		catch (Throwable t) {
			error(termIn, t);
		}
	}
	
	private void playSong(ETerminal termIn, String songName) {
		try {
			for (Audio s : Songs.songList) {
				if (EUtil.isEqual(s.getName().toLowerCase(), songName.toLowerCase())) {
					termIn.writeln("Playing song '" + s.getName() + "'", EColors.lgreen);
					SoundEngine.play(s);
					s.setVolume(QoTSettings.musicVolume.get() * 0.001);
					return;
				}
			}
			termIn.error("No song found under that name!");
		}
		catch (Throwable t) {
			error(termIn, t);
		}
	}
	
}
