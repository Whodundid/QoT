package envision.engine.terminal.commands.categories.game;

import java.io.File;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.file.FileOpener;
import qot.settings.QoTSettings;

public class CMD_WorldsDir extends TerminalCommand {
	
	public CMD_WorldsDir() {
		setCategory("Game");
		setAcceptedModifiers("-c", "-o");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "worldsdir"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("wdir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "CDs to the directory where worlds are stored."; }
	@Override public String getUsage() { return "ex: wdir"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
	    
	    try {
            boolean copy = false;
            boolean open = false;
            
            if (hasModifier("-c")) copy = true;
            if (hasModifier("-o")) open = true;
            
            setDir(QoTSettings.getEditorWorldsDir());
            String path = dir().getName();
            String colorPath = "" + EColors.mc_aqua + path;
            writeLink("Current Dir: " + colorPath, path, dir(), false, EColors.yellow);
            
            if (copy) Keyboard.setClipboard(path);
            if (open) FileOpener.openFile(new File(path));
        }
        catch (Exception e) {
            e.printStackTrace();
            error(e);
        }
	}
	
}
