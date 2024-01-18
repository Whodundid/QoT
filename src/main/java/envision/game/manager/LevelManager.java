package envision.game.manager;

import java.io.File;

import envision.Envision;
import envision.game.manager.rules.Rule_DoDaylightCycle;
import envision.game.manager.rules.Rule_GodMode;
import envision.game.manager.rules.Rule_PlayerNoClip;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.math.ENumUtil;
import eutil.strings.EStringUtil;

public class LevelManager {
    
    public static final LevelManager DUMMY_MANAGER = new LevelManager(null);
    public static final GameRules DUMMY_RULES = DUMMY_MANAGER.rules;
    
    public static boolean isLoaded() { return Envision.levelManager != null; }
    
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
    private final EList<IGameWorld> worlds = EList.newList();
    private int dayLength; // the length of one day measured in game ticks
    private GameRules rules;
    private WorldCamera camera;
    
    /** The time of day that the world will start with if there wasn't a previous world to inherit from. */
    protected int initialTime = 120000; // mid day
    protected float curTime = 0f;
    /** The current time of day measured in game ticks. */
    protected int timeOfDay = 0;
    /**
     * The full length of one day measured in game ticks. Default is 10 min
     * based on 60 tps.
     */
    protected int lengthOfDay = 1200000; // 20 minutes
    protected int ambientLightLevel = 255; // between [0-255]
    protected boolean isDay = false;
    protected boolean isNight = false;
    protected boolean isSunrise = false;
    protected boolean isSunset = false;
    
    //==============
    // Constructors
    //==============
    
    public LevelManager() {
        this(null);
    }
    
    public LevelManager(IGameWorld startingWorld) {
        this.startingWorld = startingWorld;
        
        rules = new GameRules();
        worlds.addIfNotNull(startingWorld);
        
        camera = new WorldCamera();
        camera.setZoom(2.0);
    }
    
    //=========
    // Methods
    //=========
    
    public void onGameTick(float dt) {
        if (activeWorld != null && activeWorld.isLoaded()) {
            activeWorld.onGameTick(dt);
        }
        
        // world day/time calculations should probably be moved here
        updateTime(dt);
    }
    
    public void updateTime(float dt) {
        if (!LevelManager.isLoaded()) return;
        if (!LevelManager.rules().isRuleEnabledAndEquals(Rule_DoDaylightCycle.NAME, true)) return;
        
        curTime += dt;
        timeOfDay = (int) curTime;
        if (curTime >= lengthOfDay) {
            curTime = 0;
            timeOfDay = 0;
        }
        
        int minLight = 40; // the minimum brightness of the world
        int maxLight = 255; // the maximum brightness of the world
        int deltaLight = maxLight - minLight;

        
        
        int transitionPeriodLength = (int) (lengthOfDay * 0.15);
        int sunrise = (int) (lengthOfDay * 0.15);
        int sunriseEnd = sunrise + transitionPeriodLength;
        int sunset = (int) (lengthOfDay * 0.85D);
        int sunsetEnd = sunset + transitionPeriodLength;
        
        // check still night
        if (timeOfDay < sunrise) {
            isNight = true; isDay = false; isSunrise = false; isSunset = false;
            ambientLightLevel = minLight;
        }
        // check sunrise
        else if (timeOfDay <= (sunrise + transitionPeriodLength)) {
            isNight = false; isDay = false; isSunrise = true; isSunset = false;
            ambientLightLevel = minLight + ((timeOfDay - sunrise) * deltaLight) / transitionPeriodLength;
        }
        // check sunset
        else if (timeOfDay >= sunset && timeOfDay <= (sunset + transitionPeriodLength)) {
            isNight = false; isDay = false; isSunrise = false; isSunset = true;
            ambientLightLevel = minLight + (deltaLight - (((timeOfDay - sunset) * deltaLight) / transitionPeriodLength));
        }
        // check day
        else if (timeOfDay >= sunriseEnd && timeOfDay < sunset) {
            isNight = false; isDay = true; isSunrise = false; isSunset = false;
            ambientLightLevel = maxLight;
        }
        // check night
        else if (timeOfDay >= sunsetEnd) {
            isNight = true; isDay = false; isSunrise = false; isSunset = false;
            ambientLightLevel = minLight;
        }
    }
    
    public void loadStartingWorld() {
        loadWorld(startingWorld);
    }
    
    public void tryLoadWorld(File worldFile) {
        if (!EFileUtil.fileExists(worldFile)) return;
        IGameWorld world = null;
        world = worlds.getFirst(w -> w.getWorldFile().equals(worldFile));
        if (world == null) {
            world = new GameWorld(worldFile);
            if (!world.isFileLoaded()) {
                Envision.error("Failed to load world: " + worldFile + " into current level: " + this);
                return;
            }
            worlds.add(world);
        }
        loadWorld(world);
    }
    
    public void loadWorld(String worldName) {
        if (EStringUtil.isNotPopulated(worldName)) return;
        IGameWorld world = null;
        world = worlds.getFirst(w -> w.getWorldName().equals(worldName));
        if (world == null) {
            Envision.error("No world loaded in this level with name: " + worldName + "!");
            return;
        }
        loadWorld(world);
    }
    
    public void loadWorld(IGameWorld worldIn) {
        if (activeWorld == null) {
            setTime((int) (lengthOfDay / 2.2));
        }
        
        if (activeWorld != null) {
            activeWorld.setLoaded(false);
            
            if (Envision.thePlayer != null) {
                var gw = (GameWorld) activeWorld;
                gw.lastPlayerWorldX = Envision.thePlayer.worldX;
                gw.lastPlayerWorldY = Envision.thePlayer.worldY;
                gw.lastPlayerStartX = Envision.thePlayer.startX;
                gw.lastPlayerStartY = Envision.thePlayer.startY;
            }
        }
        
        if (worldIn != null) {
            if (!worlds.contains(worldIn)) {
                worlds.add(worldIn);
            }
        }
        
        // preserve current world time
        //		int curTime = 0;
        //		if (activeWorld != null) curTime = activeWorld.getTime();
        //		else if (worldIn != null) curTime = worldIn.getInitialTime();
        
        // preserve current camera zoom
//        double curZoom = 2.0;
//        if (activeWorld != null) curZoom = activeWorld.getCameraZoom();
//        else if (worldIn != null) curZoom = worldIn.getInitialCameraZoom();
        
        activeWorld = worldIn;
        Envision.theWorld = activeWorld; // this line should probably be removed long term
        
        camera.setActiveWorld(activeWorld);
        
        if (activeWorld != null) {
            boolean wasLoaded = !((GameWorld) activeWorld).isFirstLoad;
            //camera.setZoom(curZoom);
            activeWorld.onLoad();
            activeWorld.setLoaded(true);
            
            if (Envision.thePlayer != null) {
                Envision.thePlayer.world = activeWorld;
                if (wasLoaded) {
                    var gw = (GameWorld) activeWorld;
                    Envision.thePlayer.worldX = gw.lastPlayerWorldX;
                    Envision.thePlayer.worldY = gw.lastPlayerWorldY;
                    Envision.thePlayer.startX = gw.lastPlayerStartX;
                    Envision.thePlayer.startY = gw.lastPlayerStartY;
                }
            }
            
            camera.setPixelOffsetX(-activeWorld.getTileWidth() >> 1);
            camera.setPixelOffsetY(-activeWorld.getTileHeight() >> 1);
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
    
    /**
     * Aggressively changes the active game world without any performing any
     * state changes.
     */
    public void setActiveWorld(IGameWorld worldIn) {
        activeWorld = worldIn;
        camera.setActiveWorld(activeWorld);
    }
    
    public double getCameraZoom() { return camera.getZoom(); }
    public void setCameraZoom(double zoomIn) { camera.setZoom(zoomIn); }
    
    public int getCurrentTimeOfDay() { return timeOfDay; }
    public void setCurrentTimeOfDay(int ticks) { timeOfDay = ticks; }
    
    public GameRules getGameRules() { return rules; }
    public WorldCamera getCamera() { return camera; }
    
    public void setTime(int timeInTicks) {
        timeOfDay = ENumUtil.clamp(timeInTicks, 0, lengthOfDay);
        curTime = timeOfDay;
        updateTime(0);
    }
    
    public void setDayLength(int timeInTicks) {
        lengthOfDay = ENumUtil.clamp(timeInTicks, 0, lengthOfDay);
        updateTime(0);
    }
    
    public int getInitialTime() { return initialTime; }
    public int getTime() { return timeOfDay; }
    public int getDayLength() { return lengthOfDay; }
    public boolean isDay() { return isDay; }
    public boolean isNight() { return isNight; }
    public boolean isSunrise() { return isSunrise; }
    public boolean isSunset() { return isSunset; }
    public int getAmbientLightLevel() { return ambientLightLevel; }
    
}
