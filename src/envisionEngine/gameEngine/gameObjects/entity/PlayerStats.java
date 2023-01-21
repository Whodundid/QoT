package envisionEngine.gameEngine.gameObjects.entity;

import envisionEngine.gameEngine.gameSystems.quests.Quest;
import eutil.datatypes.EArrayList;

public class PlayerStats {
	
	private Player player;
	private int enemiesKilled;
	private int friendshipPoints; // what is ??
	private EArrayList<Quest> completedQuests = new EArrayList();
	private EArrayList<Quest> incompletedQuests = new EArrayList();
	
	//-------------
	// Constructor
	//-------------
	
	public PlayerStats(Player playerIn) {
		player = playerIn;
		enemiesKilled = 0;
		friendshipPoints = 0;
	}
	
	//---------
	// Methods
	//---------
	
	public PlayerStats reset() {
		enemiesKilled = 0;
		friendshipPoints = 0;
		completedQuests.clear();
		incompletedQuests.clear();
		return this;
	}
	
	public PlayerStats addKilled(int val) { enemiesKilled += val; return this; }
	public PlayerStats addFriendship(int val) { friendshipPoints += val; return this; }
	public PlayerStats completeQuest(Quest questIn) { completedQuests.addIfNotContains(questIn); return this; }
	public PlayerStats startQuest(Quest questIn) { incompletedQuests.addIfNotContains(questIn); return this; }
	
	public boolean hasStartedQuest(Quest questIn) { return EArrayList.combineLists(completedQuests, incompletedQuests).contains(questIn); }
	public boolean hasCompletedQuest(Quest questIn) { return completedQuests.contains(questIn); }
	
	//---------
	// Getters
	//---------
	
	public Player getPlayer() { return player; }
	
	public int getEnemiesKilled() { return enemiesKilled; }
	public int getFriendshipPoints() { return friendshipPoints; }
	public int getNumQuestsCompleted() { return completedQuests.size(); }
	
	public EArrayList<Quest> getCompletedQuests() { return completedQuests; }
	public EArrayList<Quest> getIncompletedQuests() { return incompletedQuests; }
	
	//---------
	// Setters
	//---------
	
	public void setEnemiesKilled(int val) { enemiesKilled = val; }
	public void setFriendshipPoints(int val) { friendshipPoints = val; }
	
}
