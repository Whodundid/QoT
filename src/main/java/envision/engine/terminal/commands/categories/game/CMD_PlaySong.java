package envision.engine.terminal.commands.categories.game;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.effects.sounds.Audio;
import envision.game.effects.sounds.SoundEngine;
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
		EList<String> range = EList.newList();
		for (int i = 0; i < Songs.songList.size(); i++) {
			range.add(String.valueOf(i));
		}
		
		termIn.buildTabCompletions(range);
	}
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(1);
	    
	    if (noArgs()) {
			displaySongList();
			return;
		}
		
		int song = ENumUtil.parseInt(args(), 0, -1);
		SoundEngine.stopAll();
		if (song >= 0) playSong(song);
		else playSong(firstArg());
	}
	
	private void displaySongList() {
		int i = 0;
		for (Audio s : Songs.songList) {
			writeln(String.format("%2d", i++), " : ", s.getName());
		}
	}
	
	private void playSong(int songNum) {
	    Audio theSong = Songs.songList.get(songNum);
        SoundEngine.play(theSong);
        theSong.setVolume(QoTSettings.musicVolume.get() * 0.001);
	}
	
	private void playSong(String songName) {
	    for (Audio s : Songs.songList) {
            if (EUtil.isEqual(s.getName().toLowerCase(), songName.toLowerCase())) {
                writeln("Playing song '" + s.getName() + "'", EColors.lgreen);
                SoundEngine.play(s);
                s.setVolume(QoTSettings.musicVolume.get() * 0.001);
                return;
            }
        }
        error("No song found under that name!");
	}
	
}
