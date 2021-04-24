package scripting.builder.events.region.quest;

import quests.Quest;
import scripting.builder.events.region.RegionEvent;
import world.GameWorld;
import world.Region;

public class QuestStartedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestStartedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}