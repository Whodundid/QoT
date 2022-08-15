package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import world.GameWorld;
import world.dungeonBuilder.DungeonBuilder;
import world.dungeonBuilder.DungeonBuilderSettings;
import world.dungeonBuilder.DungeonSize;

public class CreateDungeon_CMD extends TerminalCommand {
	
	public CreateDungeon_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "createdung"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("crdung"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Creates a new random dungeon map"; }
	@Override public String getUsage() { return "ex: crdung small 'name'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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
