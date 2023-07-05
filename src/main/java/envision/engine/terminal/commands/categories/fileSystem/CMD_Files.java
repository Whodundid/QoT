package envision.engine.terminal.commands.categories.fileSystem;

import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_Files extends AbstractFileCommand {
	
	public CMD_Files() {
		expectedArgLength = 0;
	}

	@Override public String getName() { return "files"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("explorer"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Opens a new file explorer window"; }
	@Override public String getUsage() { return "ex: files"; }
	
	@Override
	public void runCommand() {
		displayWindow(new FileExplorerWindow(dir()));
	}
	
}
