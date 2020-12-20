package gameSystems.scriptingSystem.gameScripting.events.region.quest;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.questSystem.Quest;
import gameSystems.scriptingSystem.gameScripting.events.region.RegionEvent;

public class QuestStartedInRegionEvent extends RegionEvent {
	
	private Quest quest;
	
	public QuestStartedInRegionEvent(GameWorld theWorld, Region theRegion, Quest theQuest) {
		super(theWorld, theRegion);
		quest = theQuest;
	}
	
	public Quest getQuest() { return quest; }
	
}