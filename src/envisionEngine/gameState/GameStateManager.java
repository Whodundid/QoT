package envisionEngine.gameState;

public class GameStateManager {
	
	private GameStateManager() {}
	
	/** The current, global game state. Starts in the 'IN_MENU' state. */
	private static IGameState currentGameState = IGameState.IN_MENU;
	
	/** Returns the current, global game state. */
	public static IGameState getCurrentGameState() { return currentGameState; }
	
	public static void setCurrentGameState(IGameState stateIn) {
		currentGameState = stateIn;
	}
	
}
