package engine.terminal.terminalCommand.commands.game;

import assets.sounds.Songs;
import engine.soundEngine.SoundEngine;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class Song_CMD extends TerminalCommand {
	
	public Song_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "play_song"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("song"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Plays the selected song."; }
	@Override public String getUsage() { return "ex: song "; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.errorUsage(ERROR_NO_ARGS, getUsage()); return; }
		if (args.length() > 1) { termIn.errorUsage(ERROR_TOO_MANY, getUsage()); return; }
		
		int val = Integer.parseInt(args.get(0));
		if (val < 0 || val >= Songs.songs.size()) {
			termIn.error("index out of bounds! [0," + Songs.songs.size() + "]");
			return;
		}
		
		var song = Songs.songs.get(val);
		SoundEngine.stopAll();
		SoundEngine.loopIfNotPlaying(song);
	}
}
