package qot.screens.gameplay;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.RenderingManager;
import envision.engine.screens.GameScreen;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowRect;
import envision.engine.windows.windowObjects.basicObjects.WindowStatusBar;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.effects.sounds.SoundEngine;
import envision.game.entities.Entity;
import envision.game.entities.player.Player;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.worldTiles.WorldTile;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.assets.sounds.Songs;
import qot.entities.player.QoT_Player;
import qot.screens.character.CharacterScreen;
import qot.screens.gameplay.combat.DeathScreen;
import qot.screens.main.MainMenuScreen;

// rabbit fish

public class GamePlayScreen extends GameScreen {
    
    //========
    // Fields
    //========
    
    Player player;
    IGameWorld world;
    WindowRect topHud;
    WindowRect botHud; //don't know if actually want this one
    WindowStatusBar health, mana, stamina;
    WindowStatusBar healthRegen, manaRegen;
    WindowButton<?> character;
    
    public int midDrawX, midDrawY; //the world coordinates at the center of the screen
    public int worldXPos, worldYPos; //the world coordinates under the mouse
    
    private GamePauseWindow pauseWindow;
    private boolean openPause = false;
    
    //==============
    // Constructors
    //==============
    
    public GamePlayScreen() { this(false); }
    public GamePlayScreen(boolean openPauseOnStart) {
        super();
        //screenHistory.push(new MainMenuScreen());
        
        openPause = openPauseOnStart;
        if (world != null) Envision.levelManager.getCamera().setMinZoom(2);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initScreen() {
        SoundEngine.stopAll();
        SoundEngine.loop(Songs.battleTheme);
        
        setObjectName("Game Screen");
    }
    
    @Override
    public void initChildren() {
        getChildren().clear();
        
        //assign player field
        player = Envision.getPlayer();
        
        //topHud = new WindowRect(this, 0, 0, Envision.getWidth(), 39, EColors.dgray);
        //addObject(topHud);
        
        health = new WindowStatusBar(this, 7, 7, 190, 17, 0, player.getMaxHealth(), EColors.red);
        health.setBarValue(player.getHealth());
        addObject(health);
        
        mana = new WindowStatusBar(this, health.endX + 10, 7, 190, 17, 0, player.getMaxMana(), EColors.blue);
        mana.setBarValue(player.getMana());
        addObject(mana);
        
        stamina = new WindowStatusBar(this, mana.endX + 10, 7, 190, 17, 0, player.getMaxStamina(), EColors.green);
        stamina.setBarValue(player.getStamina());
        addObject(stamina);

        healthRegen = new WindowStatusBar(this, health.startX, health.endY + 3, health.width, 6, 0, player.healthRegenTime, EColors.mc_darkred);
        if (player.health >= player.maxHealth) healthRegen.setBarValue(player.healthRegenTime);
        addObject(healthRegen);
        
        manaRegen = new WindowStatusBar(this, mana.startX, mana.endY + 3, mana.width, 6, 0, player.magicRegenTime);
        if (player.magicLevel > 0) manaRegen.setColor(EColors.regal);
        else manaRegen.setColor(EColors.steel);
        if (player.mana >= player.maxMana) manaRegen.setBarValue(player.magicRegenTime);
        addObject(manaRegen);
        
        character = new WindowButton<>(this, mana.endX + 5, 5, 125, 30, "Stats");
        
        if (openPause) openPauseWindow();
        
        //addObject(character);
    }
    
    @Override
    public void drawScreen(float dt, int mXIn, int mYIn) {
        world = Envision.getWorld();
        
        if (DebugSettings.drawScreenAxis) {
            final double x = width * 0.5;
            final double y = height * 0.5;
            RenderingManager.drawRect(x, 0, x + 1, height, EColors.red);
            RenderingManager.drawRect(0, y, width, y + 1, EColors.red);
        }
        
        //top hud
        drawRect(0, 0, Envision.getWidth(), 39, EColors.lgray);
        drawRect(0, 39, Envision.getWidth(), 44, EColors.gray);
        //mouse pos
        
        if (world == null) {
            drawStringC("Null World!", midX, midY, EColors.lred);
            //keep trying to grab world instance
            world = Envision.theWorld;
            return;
        }
        else {
            //System.out.println(Envision.theWorld.getWorldName());
        }
        
        drawRect(health.startX - 2, health.startY - 2, health.endX + 2, healthRegen.endY + 2, EColors.black);
        drawRect(mana.startX - 2, mana.startY - 2, mana.endX + 2, manaRegen.endY + 2, EColors.black);
        drawRect(stamina.startX - 2, stamina.startY - 2, stamina.endX + 2, stamina.endY + 2, EColors.black);
        
        if (Envision.thePlayer != null) {
            final var p = Envision.thePlayer;
            health.setBarValue(p.getHealth());
            mana.setBarValue(p.getMana());
            stamina.setBarValue(p.getStamina());
            
            if (p.health >= p.maxHealth) healthRegen.setBarValue(p.healthRegenTime);
            else healthRegen.setBarValue(p.healthRegenUpdate);
            
            if (p.mana >= p.maxMana) manaRegen.setBarValue(p.magicRegenTime);
            else manaRegen.setBarValue(p.magicRegenUpdate);
            
            if (Envision.thePlayer.isDead()) { Envision.displayScreen(new DeathScreen()); }
            //drawString(world.getEntitiesInWorld().size(), 50, 100);
        }
        
        //drawString("x" + Envision.levelManager.getCamera(), Envision.getWidth() - 250.0, 12, EColors.dsteel);
        drawString("[" + player.worldX + ", " + player.worldY + "]", Envision.getWidth() - 900.0, 12, EColors.white);
        //drawString("[" + player.startX + ", " + player.startY + "]", Envision.getWidth() - 750, 12, EColors.white);
        //drawString("[" + world.getPixelWidth() + ", " + world.getPixelHeight() + "]", Envision.getWidth() - 550, 12, EColors.hotpink);
        
        //drawRect(midX, 0, midX + 1, endY, EColors.black);
        //drawRect(0, midY, endX, midY + 1, EColors.black);
        
        if (Envision.thePlayer != null) { Envision.thePlayer.getSpellbook().drawAbilities(this); }
    }
    
    @Override
    public void onGameTick(float dt) {
        if (!DeveloperDesktop.isOpen()) {
            QoT_Player p = (QoT_Player) Envision.thePlayer;
            
            double moveX = 0.0, moveY = 0.0;
            
            if (Keyboard.isADown()) moveX -= 1;
            if (Keyboard.isDDown()) moveX += 1;
            if (Keyboard.isWDown()) moveY -= 1;
            if (Keyboard.isSDown()) moveY += 1;
            
            if (!p.preventMovementInputs) {
                if (Keyboard.isAnyKeyDown(Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_W, Keyboard.KEY_S)) {
                    p.move(moveX, moveY);
                }
            }
            
            //			double moveSpeed = 1;
            //			if (Keyboard.isWDown()) p.move(0, -moveSpeed);
            //			if (Keyboard.isSDown()) p.move(0, moveSpeed);
            //			if (Keyboard.isADown()) p.move(-moveSpeed, 0);		
            //			if (Keyboard.isDDown()) p.move(moveSpeed, 0);
        }
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_TAB) openCharScreen();
        if (keyCode == Keyboard.KEY_ESC) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LWIN)) Envision.displayScreen(new MainMenuScreen());
            else openPauseWindow();
        }
        
        if (keyCode == Keyboard.KEY_LEFT) Envision.thePlayer.movePixel(-1, 0);
        if (keyCode == Keyboard.KEY_RIGHT) Envision.thePlayer.movePixel(1, 0);
        if (keyCode == Keyboard.KEY_UP) Envision.thePlayer.movePixel(0, -1);
        if (keyCode == Keyboard.KEY_DOWN) Envision.thePlayer.movePixel(0, 1);
        if (keyCode == Keyboard.KEY_SPACE) Envision.thePlayer.abilityTracker.useAbility(0);
        
        if (keyCode == Keyboard.KEY_N) {
            Entity obj = world.getEntitiesInWorld().getRandom();
            Envision.levelManager.getCamera().setFocusedObject(obj);
        }
        if (keyCode == Keyboard.KEY_M) { Envision.levelManager.getCamera().setFocusedObject(Envision.thePlayer); }
        
        if (Envision.thePlayer != null) { Envision.thePlayer.onKeyPress(typedChar, keyCode); }
        
        if (keyCode == Keyboard.KEY_O) DebugSettings.drawEntityHitboxes = !DebugSettings.drawEntityHitboxes;
        if (keyCode == Keyboard.KEY_H) DebugSettings.drawEntityCollisionBoxes = !DebugSettings.drawEntityCollisionBoxes;
        if (keyCode == Keyboard.KEY_P) DebugSettings.drawEntityPositionTiles = !DebugSettings.drawEntityPositionTiles;
        if (keyCode == Keyboard.KEY_Y) DebugSettings.drawScreenAxis = !DebugSettings.drawScreenAxis;
        if (keyCode == Keyboard.KEY_U) DebugSettings.drawFocusedEntityAxis = !DebugSettings.drawFocusedEntityAxis;
        
        //world.getWorldRenderer().keyPressed(typedChar, keyCode);
        
        //super.keyPressed(typedChar, keyCode);
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);
        
        final WorldCamera cam = Envision.levelManager.getCamera();
        double mouseX = cam.getMxPixel();
        double mouseY = cam.getMyPixel();
        int worldX = cam.getMxTile();
        int worldY = cam.getMyTile();
        
        if (worldX >= 0 && worldX < world.getWidth() && worldY >= 0 && worldY < world.getHeight()) {
            WorldTile tile = world.getTileAt(worldX, worldY);
            tile.onMousePress(mXIn, mYIn, button);
        }
        
        EList<Entity> entitiesUnderMouse = new EArrayList<>(30);
        
        //System.out.println((drawDistX - camX) + " : " + (drawDistY - camY));
        
        // garbage
        for (var e : world.getEntitiesInWorld()) {
            entitiesUnderMouse.addIf(e.getDimensions().contains(mouseX, mouseY), e);
        }
        
        // filter the entities under mouse to the ones closest to the actual mouse press location
        for (var e : entitiesUnderMouse) {
            e.onMousePress(mXIn, mYIn, button);
        }
        
        if (Envision.thePlayer != null) {
            Envision.thePlayer.onMousePress(mXIn, mYIn, button);
        }
    }
    
    @Override
    public void mouseScrolled(int change) {
        double c = Math.signum(change);
        double z;
        var cam = Envision.levelManager.getCamera();
        double curZoom = cam.getZoom();
        
        //if (Keyboard.isCtrlDown()) {
        if (c > 0 && curZoom == 0.25) z = 0.05;		//if at 0.25 and zooming out -- 0.05x
        else if (curZoom < 1.0) z = c * 0.1;	//if less than 1 zoom by 0.1x
        else if (c > 0) z = 0.25;		//if greater than 1 zoom by 0.25x
        else if (curZoom == 1.0) z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
        else z = c * 0.25;	//otherwise always zoom by 0.25x
        
        z = ENumUtil.round(curZoom + z, 2);
        cam.setZoom(z);
        //}
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        if (object == character) openCharScreen();
    }
    
    @Override
    public void onScreenClosed() {
        //Game.displayScreen(new MainMenuScreen());
        //Envision.loadWorld(null);
        //Songs.stopAllMusic();
    }
    
    private void openCharScreen() {
        Envision.displayScreen(new CharacterScreen(Envision.thePlayer), this);
    }
    
    public void openPauseWindowIfNotOpen() {
        if (pauseWindow != null && getChildren().contains(pauseWindow) && !pauseWindow.isClosed()) return;
        pauseWindow = new GamePauseWindow(this, 30, 30);
        displayWindow(pauseWindow);
    }
    
    public void openPauseWindow() {
        if (getChildren().notContains(pauseWindow) || pauseWindow == null) {
            pauseWindow = new GamePauseWindow(this, 30, 30);
            displayWindow(pauseWindow);
        }
        else {
            pauseWindow.close();
            pauseWindow = null;
        }
    }
    
    @Override
    public void onWorldLoaded() {
        this.world = Envision.theWorld;
    }
    
    @Override
    public void onScreenResized() {
        super.onScreenResized();
        
        int sx = 0;
        int sy = 44;
        int ex = Envision.getWidth();
        int ey = Envision.getHeight();
        Envision.levelManager.getCamera().setDrawableAreaDimensions(sx, sy, ex, ey);
    }
    
}
