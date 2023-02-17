package envision.engine.scripting.scriptBuilder.events.region.quest;

import envision.engine.scripting.scriptBuilder.events.region.RegionEvent;
import envision.game.quests.Quest;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class QuestFailedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestFailedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}