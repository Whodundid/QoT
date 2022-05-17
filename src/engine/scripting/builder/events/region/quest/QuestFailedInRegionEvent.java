package engine.scripting.builder.events.region.quest;

import engine.scripting.builder.events.region.RegionEvent;
import game.quests.Quest;
import world.GameWorld;
import world.Region;

public class QuestFailedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestFailedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}