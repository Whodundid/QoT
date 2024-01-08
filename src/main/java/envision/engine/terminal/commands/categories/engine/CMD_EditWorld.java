package envision.engine.terminal.commands.categories.engine;

import java.io.File;

import envision.Envision;
import envision.engine.terminal.commands.categories.fileSystem.AbstractFileCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.settings.QoTSettings;

public class CMD_EditWorld extends AbstractFileCommand {
	
	public CMD_EditWorld() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "editworld"; }
	@Override public EList<String> getAliases() { return EList.of("ew"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Loads the specified world into the map editor"; }
	@Override public String getUsage() { return "ex: ew (world name)"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		fileTabComplete(termIn, QoTSettings.getEditorWorldsDir(), args);
	}
	
	@Override
	public void runCommand() {
	    expectExactly(1, "Error: Map name empty!");
		
		String worldName = firstArg();
		File f = new File(QoTSettings.getEditorWorldsDir(), worldName);
		
		if (!f.exists()) {
		    error("World '" + worldName + "' does not exist!");
		    return;
		}
		
		writeln("Editing world...", EColors.green);
		Envision.displayScreen(new MapEditorScreen(f));
	}
	
}
