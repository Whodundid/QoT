package envisionEngine.terminal.terminalCommand.commands.game;

import assets.entities.player.Player;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import gameScreens.gameplay.GamePlayScreen;
import gameSystems.mapSystem.GameWorld;
import java.io.File;
import main.Game;
import renderUtil.EColors;
import storageUtil.EArrayList;

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
			File f = new File(Game.settings.getEditorWorldsDir(), worldName);
			
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
				
				Player p = Game.setPlayer(new Player("Test"));
				
				if (switchTo) {
					Game.displayScreen(new GamePlayScreen());
				}
				
				GameWorld w = Game.loadWorld(new GameWorld(f));
				w.addEntity(p);
			}
			else {
				termIn.error("World '" + worldName + "' does not exist!");
			}
		}
	}
	
}
