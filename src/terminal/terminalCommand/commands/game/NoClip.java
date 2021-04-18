package terminal.terminalCommand.commands.game;

import main.Game;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;

public class NoClip extends TerminalCommand {
	
	public NoClip() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "noclip"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("nc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Allows player no-clipping"; }
	@Override public String getUsage() { return "ex: nc"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (Game.thePlayer != null) {
			Game.thePlayer.setNoClipAllowed(!Game.thePlayer.isNoClipping());
			termIn.writeln(((Game.thePlayer.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
		}
		else {
			termIn.error("There isn't a player!");
		}
	}
	
}
