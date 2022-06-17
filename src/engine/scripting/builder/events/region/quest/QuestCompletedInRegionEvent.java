package engine.scripting.builder.events.region.quest;

import engine.scripting.builder.events.region.RegionEvent;
import game.quests.Quest;
import world.GameWorld;
import world.Region;

public class QuestCompletedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestCompletedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}