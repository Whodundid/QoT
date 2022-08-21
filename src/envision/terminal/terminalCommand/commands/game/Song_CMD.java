package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

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
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.error(getUsage());
		
		
	}
}
