package debug.terminal.terminalCommand.commands.game;

import assets.entities.Player;
import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.storage.EArrayList;
import gameScreens.gameplay.GamePlayScreen;
import java.io.File;
import main.QoT;
import world.GameWorld;

public class LoadWorld extends TerminalCommand {
	
	public LoadWorld() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "loadworld"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("lw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Loads the specified world"; }
	@Override public String getUsage() { return "ex: lw (world name)"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Error: Map name empty!"); }
		else {
			String worldName = args.get(0);
			worldName = (worldName.endsWith(".twld")) ? worldName : worldName + ".twld";
			File f = new File(QoT.settings.getEditorWorldsDir(), worldName);
			
			boolean switchTo = true;
			
			if (args.length() > 1) {
				try {
					String flag = args.get(1);
					if ("-ns".equals(flag)) { switchTo = false; }
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			
			if (f.exists()) {
				termIn.writeln("Loading world...", EColors.green);
				
				Player p = QoT.setPlayer(new Player("Test"));
				
				if (switchTo) {
					QoT.displayScreen(new GamePlayScreen());
				}
				
				GameWorld w = QoT.loadWorld(new GameWorld(f));
				w.addEntity(p);
			}
			else {
				termIn.error("World '" + worldName + "' does not exist!");
			}
		}
	}
	
}
