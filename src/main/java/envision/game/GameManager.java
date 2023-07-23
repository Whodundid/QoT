package envision.game;

import envision.Envision;
import envision.game.world.IGameWorld;

public class GameManager {
	
	//========
	// Fields
	//========
	
	private IGameWorld activeWorld;
	private int timeOfDay; // time in game ticks
	private int dayLength; // the length of one day measured in game ticks
	private double cameraZoom;
	
	//==============
	// Constructors
	//==============
	
	public GameManager(IGameWorld startingWorld) {
		activeWorld = startingWorld;
		
		timeOfDay = 0;
		dayLength = 500; // laughably low to test with
	}
	
	//=========
	// Methods
	//=========
	
	public void onGameTick(float dt) {
		activeWorld.onGameTick(dt);
	}
	
	public void loadWorld(IGameWorld worldIn) {
		if (activeWorld != null) activeWorld.setLoaded(false);
		
		activeWorld = worldIn;
		Envision.theWorld = activeWorld;
		
		if (activeWorld != null) {
			activeWorld.onLoad();
			activeWorld.setLoaded(true);
		}
	}
	
	//===================
	// Getters | Setters
	//===================
	
	public IGameWorld getActiveWorld() { return activeWorld; }
	/** Aggressively changes the active game world without any performing any state changes. */
	public void setActiveWorld(IGameWorld worldIn) { activeWorld = worldIn; }
	
	public double getCameraZoom() { return cameraZoom; }
	public void setCameraZoom(double zoomIn) { cameraZoom = zoomIn; }
	
	public int getCurrentTimeOfDay() { return timeOfDay; }
	public void setCurrentTimeOfDay(int ticks) { timeOfDay = ticks; }
	
	public int getDayLength() { return dayLength; }
	public void setDayLength(int ticks) { dayLength = ticks; }
	
}
