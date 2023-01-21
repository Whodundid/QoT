package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.quest;

import envisionEngine.gameEngine.gameSystems.quests.Quest;
import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class QuestCompletedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestCompletedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}