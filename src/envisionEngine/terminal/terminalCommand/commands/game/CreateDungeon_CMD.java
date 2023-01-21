package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.gameEngine.world.dungeonBuilder.DungeonBuilder;
import envisionEngine.gameEngine.world.dungeonBuilder.DungeonBuilderSettings;
import envisionEngine.gameEngine.world.dungeonBuilder.DungeonSize;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CreateDungeon_CMD extends TerminalCommand {
	
	public CreateDungeon_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "createdung"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("crdung"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Creates a new random dungeon map"; }
	@Override public String getUsage() { return "ex: crdung small 'name'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		DungeonBuilder builder = new DungeonBuilder();
		
		if (args.isEmpty()) {
			GameWorld dung = builder.buildRandomDungeon();
			dung.setWorldName("rand_dung");
			dung.saveWorldToFile();
		}
		else {
			String size = (args.size() >= 1) ? args.get(0) : "small";
			DungeonSize dungSize = DungeonSize.fromString(size);
			
			DungeonBuilderSettings settings = new DungeonBuilderSettings();
			settings.size = dungSize;
			
			GameWorld dung = builder.buildRandomDungeon(settings);
			if (dung != null) {
				String dungName = (args.size() >= 2) ? args.get(1) : "rand_dung";
				
				dung.setWorldName(dungName);
				dung.saveWorldToFile();
			}
			else termIn.error("Created map returned null!");
		}
	}
	
}
