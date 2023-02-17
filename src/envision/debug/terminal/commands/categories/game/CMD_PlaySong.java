package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.game.objects.effects.sounds.Audio;
import envision.game.objects.effects.sounds.SoundEngine;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.assets.sounds.Songs;
import qot.settings.QoTSettings;

public class CMD_PlaySong extends TerminalCommand {
	
	public CMD_PlaySong() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "song"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("play"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Plays the selected song."; }
	@Override public String getUsage() { return "ex: song "; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		EArrayList<String> range = new EArrayList<>();
		for (int i = 0; i < Songs.songList.size(); i++) {
			range.add(String.valueOf(i));
		}
		
		termIn.buildTabCompletions(range);
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
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
	
	private void displaySongList(ETerminalWindow termIn) {
		int i = 0;
		for (Audio s : Songs.songList) {
			termIn.writeln(String.format("%2d", i++), " : ", s.getName());
		}
	}
	
	private void playSong(ETerminalWindow termIn, int songNum) {
		try {
			Audio theSong = Songs.songList.get(songNum);
			SoundEngine.play(theSong);
			theSong.setVolume(QoTSettings.musicVolume.get() * 0.001);
		}
		catch (Throwable t) {
			error(termIn, t);
		}
	}
	
	private void playSong(ETerminalWindow termIn, String songName) {
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
