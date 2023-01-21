package envisionEngine.gameState;

/**
 * A state that the engine is globally in at one point.
 * <p>
 * There can only be one active game state that the engine is in at any
 * given time and it will solely determine which options/inputs are
 * available to the player.
 * 
 * @author Hunter Bragg
 */
public enum IGameState {
	
	IN_GAME,
	IN_MENU,
	IN_DIALOG,
	IN_GAME_GUI, //shops and stuff
	
	WORLD_LOADING,
	WORLD_UNLOADING,
	
}

