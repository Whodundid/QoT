package envision.terminal.terminalCommand.commands.game;

import envision.gameEngine.effects.sounds.Audio;
import envision.gameEngine.effects.sounds.SoundEngine;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import game.assets.sounds.Songs;
import game.settings.QoTSettings;

public class Song_CMD extends TerminalCommand {
	
	public Song_CMD() {
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "song"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("play"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Plays the selected song."; }
	@Override public String getUsage() { return "ex: song "; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> range = new EArrayList<>();
		for (int i = 0; i < Songs.songList.size(); i++) {
			range.add(String.valueOf(i));
		}
		
		termIn.buildTabCompletions(range);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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
