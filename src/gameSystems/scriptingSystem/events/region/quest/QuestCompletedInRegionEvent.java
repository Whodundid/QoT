package gameSystems.scriptingSystem.events.region.quest;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.questSystem.Quest;
import gameSystems.scriptingSystem.events.region.RegionEvent;

public class QuestCompletedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestCompletedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}