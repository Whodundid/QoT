package envision.game.manager;

import envision.Envision;
import envision.game.manager.rules.Rule_DoDaylightCycle;
import envision.game.manager.rules.Rule_GodMode;
import envision.game.manager.rules.Rule_PlayerNoClip;
import envision.game.world.IGameWorld;

public class LevelManager {
	
    public static final LevelManager DUMMY_MANAGER = new LevelManager(null);
    public static final GameRules DUMMY_RULES = DUMMY_MANAGER.rules;
    
    public static boolean isLoaded() {
        return Envision.levelManager != null;
    }
    
    public static GameRules rules() {
        if (Envision.levelManager != null) {
            return Envision.levelManager.getGameRules();
        }
        
        return DUMMY_RULES;
    }
    
	//========
	// Fields
	//========
	
    private IGameWorld startingWorld;
	private IGameWorld activeWorld;
	private int timeOfDay; // time in game ticks
	private int dayLength; // the length of one day measured in game ticks
	private double cameraZoom;
	private GameRules rules;
	
	//==============
	// Constructors
	//==============
	
	public LevelManager(IGameWorld startingWorld) {
	    this.startingWorld = startingWorld;
		
		timeOfDay = 0;
		dayLength = 500; // laughably low to test with
		
		rules = new GameRules();
	}
	
	//=========
	// Methods
	//=========
	
	public void onGameTick(float dt) {
	    if (activeWorld != null && activeWorld.isLoaded()) {
	        activeWorld.onGameTick(dt);
	    }
		
		// world day/time calculations should probably be moved here
	}
	
	public void loadStartingWorld() {
	    loadWorld(startingWorld);
	}
	
	public void loadWorld(IGameWorld worldIn) {
		if (activeWorld != null) activeWorld.setLoaded(false);
		
		// preserve current world time
//		int curTime = 0;
//		if (activeWorld != null) curTime = activeWorld.getTime();
//		else if (worldIn != null) curTime = worldIn.getInitialTime();
		
		// preserve current camera zoom
		double curZoom = 2.0;
		if (activeWorld != null) curZoom = activeWorld.getCameraZoom();
		else if (worldIn != null) curZoom = worldIn.getInitialCameraZoom();
		
		activeWorld = worldIn;
		Envision.theWorld = activeWorld; // this line should probably be removed long term
		
		if (activeWorld != null) {
//		    activeWorld.setTime(curTime);
		    activeWorld.setCameraZoom(curZoom);
			activeWorld.onLoad();
			activeWorld.setLoaded(true);
		}
	}
	
	public void loadDefaultRules() {
	    rules.addRule(new Rule_DoDaylightCycle());
	    rules.addRule(new Rule_GodMode());
	    rules.addRule(new Rule_PlayerNoClip());
	}
	
	//===================
	// Getters | Setters
	//===================
	
	public IGameWorld getStartingWorld() { return startingWorld; }
	public void setStartingWorld(IGameWorld startingWorldIn) { startingWorld = startingWorldIn; }
	
	public IGameWorld getActiveWorld() { return activeWorld; }
	/** Aggressively changes the active game world without any performing any state changes. */
	public void setActiveWorld(IGameWorld worldIn) { activeWorld = worldIn; }
	
	public double getCameraZoom() { return cameraZoom; }
	public void setCameraZoom(double zoomIn) { cameraZoom = zoomIn; }
	
	public int getCurrentTimeOfDay() { return timeOfDay; }
	public void setCurrentTimeOfDay(int ticks) { timeOfDay = ticks; }
	
	public int getDayLength() { return dayLength; }
	public void setDayLength(int ticks) { dayLength = ticks; }
	
	public GameRules getGameRules() { return rules; }
	
}
