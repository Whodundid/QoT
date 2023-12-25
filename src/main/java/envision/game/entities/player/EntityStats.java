package envision.game.entities.player;

import envision.game.entities.Entity;
import envision.game.quests.Quest;
import eutil.datatypes.util.EList;

public class EntityStats {
	
    //========
    // Fields
    //========
    
	private Entity entity;
	private int enemiesKilled;
	private int friendshipPoints; // what is ??
	private EList<Quest> completedQuests = EList.newList();
	private EList<Quest> startedQuests = EList.newList();
	
	//==============
    // Constructors
    //==============
	
	public EntityStats(Entity entityIn) {
		entity = entityIn;
		enemiesKilled = 0;
		friendshipPoints = 0;
	}
	
	//=========
    // Methods
    //=========
	
	public EntityStats reset() {
		enemiesKilled = 0;
		friendshipPoints = 0;
		completedQuests.clear();
		startedQuests.clear();
		return this;
	}
	
	public EntityStats addKilled(int val) { enemiesKilled += val; return this; }
	public EntityStats addFriendship(int val) { friendshipPoints += val; return this; }
	public EntityStats completeQuest(Quest questIn) { completedQuests.addNullContains(questIn); return this; }
	public EntityStats startQuest(Quest questIn) { startedQuests.addNullContains(questIn); return this; }
	
	public boolean hasStartedQuest(Quest questIn) { return startedQuests.contains(questIn); }
	public boolean hasCompletedQuest(Quest questIn) { return completedQuests.contains(questIn); }
	
	//=========
    // Getters
    //=========
	
	public Entity getEntity() { return entity; }
	
	public int getEnemiesKilled() { return enemiesKilled; }
	public int getFriendshipPoints() { return friendshipPoints; }
	public int getNumQuestsCompleted() { return completedQuests.size(); }
	
	public EList<Quest> getCompletedQuests() { return completedQuests; }
	public EList<Quest> getStartedQuests() { return startedQuests; }
	
	//=========
    // Setters
    //=========
	
	public void setEnemiesKilled(int val) { enemiesKilled = val; }
	public void setFriendshipPoints(int val) { friendshipPoints = val; }
	
}
