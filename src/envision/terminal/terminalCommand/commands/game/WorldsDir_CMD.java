package envision.terminal.terminalCommand.commands.game;

import java.io.File;

import envision.inputHandlers.Keyboard;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.file.FileOpener;
import game.settings.QoTSettings;

public class WorldsDir_CMD extends TerminalCommand {
	
	public WorldsDir_CMD() {
		setCategory("Game");
		setAcceptedModifiers("-c", "-o");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "worldsdir"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("wdir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "CDs to the directory where worlds are stored."; }
	@Override public String getUsage() { return "ex: wdir"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) {
			termIn.error("This command may only take one additional argument! (-c, -o)");
		}
		else {
			try {
				boolean copy = false;
				boolean open = false;
				
				if (hasModifier("-c")) copy = true;
				if (hasModifier("-o")) open = true;
				
				termIn.setDir(QoTSettings.getEditorWorldsDir());
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
