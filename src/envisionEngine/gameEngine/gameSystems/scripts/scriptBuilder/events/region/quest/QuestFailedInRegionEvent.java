package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.quest;

import envision.gameEngine.gameSystems.quests.Quest;
import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

public class QuestFailedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestFailedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}