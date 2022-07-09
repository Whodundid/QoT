package engine.terminal.terminalCommand.commands.game;

import engine.input.Keyboard;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.sys.FileOpener;
import main.settings.QoT_Settings;

import java.io.File;

public class WorldsDir_CMD extends TerminalCommand {
	
	public WorldsDir_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		setAcceptedModifiers("-c", "-o");
		numArgs = 0;
	}

	@Override public String getName() { return "worldsdir"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("wdir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "CDs to the directory where worlds are stored."; }
	@Override public String getUsage() { return "ex: wdir"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) { termIn.error("This command may only take one additional argument! (-c, -o)"); }
		else {
			try {
				boolean copy = false;
				boolean open = false;
				
				if (hasModifier("-c")) copy = true;
				if (hasModifier("-o")) open = true;
				
				termIn.setDir(QoT_Settings.getEditorWorldsDir());
				String path = termIn.getDir().getCanonicalPath();
				String colorPath = "" + EColors.mc_aqua + path;
				termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
				
				if (copy) Keyboard.setClipboard(path);
				if (open) FileOpener.openFile(new File(path));
			}
			catch (Exception e) {
				e.printStackTrace();
				error(termIn, e);
			}
		}
	}
	
}