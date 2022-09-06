package envision.game.scripts.scriptBuilder.events.region.quest;

import envision.game.quests.Quest;
import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class QuestCompletedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestCompletedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}