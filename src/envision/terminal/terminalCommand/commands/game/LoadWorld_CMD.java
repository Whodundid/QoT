package envision.terminal.terminalCommand.commands.game;

import java.io.File;

import envision.game.world.gameWorld.GameWorld;
import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.commands.fileSystem.FileCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;
import game.entities.player.QoT_Player;
import game.screens.gameplay.GamePlayScreen;
import game.settings.QoTSettings;

public class LoadWorld_CMD extends FileCommand {
	
	public LoadWorld_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "loadworld"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("lw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Loads the specified world"; }
	@Override public String getUsage() { return "ex: lw (world name)"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		fileTabComplete(termIn, QoTSettings.getEditorWorldsDir(), args);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Error: Map name empty!"); }
		else {
			String worldName = args.get(0);
			GameWorld world = buildWorld(worldName);
			
			boolean switchTo = true;
			
			if (args.length() > 1) {
				try {
					String flag = args.get(1);
					if ("-ns".equals(flag)) switchTo = false;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (world.getWorldFile().exists()) {
				termIn.writeln("Loading world...", EColors.green);
				
				QoT_Player p = QoT.setPlayer(new QoT_Player("Test"));
				
				if (switchTo) {
					QoT.displayScreen(new GamePlayScreen());
				}
				
				GameWorld w = QoT.loadWorld(world);
				w.addEntity(p);
			}
			else {
				termIn.error("World '" + worldName + "' does not exist!");
			}
		}
	}
	
	public static GameWorld buildWorld(String worldName) {
		worldName = (worldName.endsWith(".twld")) ? worldName : worldName + ".twld";
		File f = new File(QoTSettings.getEditorWorldsDir(), worldName);
		return new GameWorld(f);
	}
	
}
