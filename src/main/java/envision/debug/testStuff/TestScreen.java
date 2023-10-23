package envision.debug.testStuff;

import envision.engine.events.GameEvent;
import envision.engine.rendering.GLObject;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.screens.GameScreen;
import eutil.colors.EColors;
import eutil.datatypes.ValueMap;
import eutil.misc.Rotation;
import qot.assets.textures.entity.EntityTextures;

public class TestScreen extends GameScreen {
    
    private ValueMap fields = new ValueMap();
    
    public TestScreen(Object... args) {
        fields.put("sprite", EntityTextures.walksheet.getSprite(28));
        fields.put("rot", Rotation.UP);
        fields.put("start", System.currentTimeMillis());
    }
    
    /** Initializer method that is called before a screen is built. */
    @Override
    public void initScreen() {
        
    }
    
    @Override
    public void onFirstDraw() {
        
    }
    
    /** Called everytime this screen is about to be drawn. */
    @Override
    public void drawScreen(int mXIn, int mYIn) {
        Sprite s = fields.get("sprite");
        Rotation r = fields.get("rot");
        drawRect(100, 300, 400, 600, EColors.skyblue);
        drawSprite(s, 100, 300, 300, 300, false, r);
    }
    
    /** Called everytime a new game tick occurs. */
    @Override
    public void onGameTick(float dt) {
        long start = fields.getOrDefault("start", 0L);
        if (System.currentTimeMillis() - start >= 500L) {
            Rotation r = fields.get("rot");
            switch (r) {
            case LEFT: fields.put("rot", Rotation.DOWN); break;
            case RIGHT: fields.put("rot", Rotation.UP); break;
            case UP: fields.put("rot", Rotation.LEFT); break;
            case DOWN: fields.put("rot", Rotation.RIGHT); break;
            }
            fields.put("start", System.currentTimeMillis());
        }
    }
    
    @Override
    public void onEvent(GameEvent e) {
        
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        super.keyPressed(typedChar, keyCode);
    }
    
    /** Called whenever this screen is about to be closed. */
    @Override
    public void onScreenClosed() {
        
    }
    
    /** Called whenever a world is loaded. */
    @Override
    public void onWorldLoaded() {
        
    }
    
    @Override
    public void onScreenResized() {
        super.onScreenResized();
    }
    
    /**
     * Closes this screen and displays the previous screen in history.
     */
    @Override
    public void closeScreen() {
        super.closeScreen();
    }
    
}
