package gameSystems.questSystem;

public class RouteTracker {
	
	private int enemiesKilled;
	private int questsCompleted;
	private int friendshipPoints;
	
	public RouteTracker() {
		enemiesKilled = 0;
		questsCompleted = 0;
		friendshipPoints = 0;
	}
	
	// Getters
	
	public int getEnemiesKilled() {
		return enemiesKilled;
	}
	
	public int getQuestsCompleted() {
		return questsCompleted;
	}
	
	public int getFriendshipPoints() {
		return friendshipPoints;
	}
	
	// Setters
	
	public void setEnemiesKilled(int enemiesKilledIn) {
		enemiesKilled = enemiesKilled + enemiesKilledIn;
	}
	
	public void setQuestsCompleted(int questsCompletedIn) {
		questsCompleted = questsCompleted + questsCompletedIn;
	}
	
	public void setFriendshipPoints(int friendshipPointsIn) {
		friendshipPoints = friendshipPoints + friendshipPointsIn;
	}
	
	// Methods
	
	
}
