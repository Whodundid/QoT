package envision.engine.terminal.commands.categories.game;

import envision.engine.terminal.commands.TerminalCommand;
import envision.game.world.GameWorld;
import envision.game.world.dungeonBuilder.DungeonBuilder;
import envision.game.world.dungeonBuilder.DungeonBuilderSettings;
import envision.game.world.dungeonBuilder.DungeonSize;
import eutil.datatypes.util.EList;

public class CMD_CreateDungeon extends TerminalCommand {
	
	public CMD_CreateDungeon() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "createdung"; }
	@Override public EList<String> getAliases() { return EList.of("crdung"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Creates a new random dungeon map"; }
	@Override public String getUsage() { return "ex: crdung small 'name'"; }
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(2);
	    
		DungeonBuilder builder = new DungeonBuilder();
		
		if (noArgs()) {
		    GameWorld dung = builder.buildRandomDungeon();
            dung.setWorldName("rand_dung");
            dung.saveWorldToFile();
            return;
		}
		
		String size = (oneArg()) ? arg(0) : "small";
        DungeonSize dungSize = DungeonSize.fromString(size);
        
        DungeonBuilderSettings settings = new DungeonBuilderSettings();
        settings.size = dungSize;
        
        GameWorld dung = builder.buildRandomDungeon(settings);
        if (dung != null) {
            String dungName = (twoArgs()) ? arg(1) : "rand_dung";
            
            dung.setWorldName(dungName);
            dung.saveWorldToFile();
        }
        else error("Created map returned null!");
	}
	
}
