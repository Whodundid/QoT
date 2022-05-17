package engine.terminal.terminalCommand.commands.game;

import engine.QoT;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class Song_CMD extends TerminalCommand {
	
	public Song_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "play_song"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("song"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Plays the selected song."; }
	@Override public String getUsage() { return "ex: song "; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.error(getUsage());
		
		String name = StringUtil.combineAll(args);
		
	}
}
