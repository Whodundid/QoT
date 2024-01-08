package envision.engine.windows.developerDesktop.shortcuts;

import java.util.HashMap;
import java.util.Map;

import envision.Envision;
import envision.engine.assets.TaskBarTextures;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2d;
import eutil.strings.EStringBuilder;

public abstract class DesktopShortcut extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected String shortcutName = "New Shortcut";
    protected ShortcutType type;
    protected GameTexture icon;
    protected boolean selected = false;
    
    protected final Map<String, String> additionalProperties = new HashMap<>();
    protected boolean isPressed;
    protected boolean isMoving;
    protected final Point2d pressPoint = new Point2d();
    protected final Point2d oldPoint = new Point2d();
    
    protected RightClickMenu shortcutRCM;
    
    private boolean isRenaming = false;
    
    //==============
    // Constructors
    //==============
    
    // for use in parsing
    protected DesktopShortcut(String nameIn, ShortcutType typeIn) {
        shortcutName = nameIn;
        type = typeIn;
    }
    
    protected DesktopShortcut(String nameIn, double x, double y, ShortcutType typeIn) {
        shortcutName = nameIn;
        type = typeIn;
        init(x, y);
    }
    
    //======
    // init
    //======
    
    public void init() {
        init(startX, startY);
    }
    
    public void init(double x, double y) {
        double w = 60;
        double h = 60;
        
        init(Envision.getDeveloperDesktop(), x, y, w, h);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onAdded() {
        getTopParent().registerListener(this);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        // check if moving
        checkMoveShortcut(mXIn, mYIn);
        
        //draw icon
        GameTexture tex = icon;
        if (tex == null) tex = TaskBarTextures.window;
        drawTexture(icon);
        
        //draw hover color
        var color = EColors.lgray;
        if (isMouseInside()) drawHRect(color.opacity(200));
        if (selected) {
            if (isMouseInside()) drawRect(color.opacity(100));
            else drawRect(color.opacity(60));
        }
        
        double scale = 0.8;
        double nameWidth = strWidth(shortcutName);
        double drawX = midX + (nameWidth - nameWidth * scale);
        
        //draw description text below icon
        drawStringC(shortcutName, drawX, endY + 6, scale, scale, EColors.chalk);
        
        checkMouseMove();
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);
        
        if (button == 0) {
            var dd = DeveloperDesktop.getInstance();
            
            selected = true;
            
            if (!Keyboard.isCtrlDown()) {
                dd.clearHighlightedShortcuts();
            }
            
            dd.addToHighlighted(this);
            
            isPressed = true;
            pressPoint.set(mXIn, mYIn);
            oldPoint.set(mXIn, mYIn);
        }
        else if (button == 1) {
            openShortcutRCM();
        }
    }
    
    @Override
    public void mouseReleased(int mXIn, int mYIn, int button) {
        super.mouseReleased(mXIn, mYIn, button);
        
        if (isMoving && button == 0) {
            isPressed = false;
            isMoving = false;
            DeveloperDesktop.saveConfig();
        }
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        super.keyPressed(typedChar, keyCode);
        
        if (!selected) return;
        
        //if enter -- attempt to run shortcut
        if (keyCode == Keyboard.KEY_ENTER) tryOpenShortcut();
        //if delete -- attempt to remove shortcut from main over
        else if (keyCode == Keyboard.KEY_DELETE) deleteShortcut();
    }
    
    @Override
    public void onDoubleClick() {
        tryOpenShortcut();
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        if (e instanceof EventMouse em) {
            if (em.getMouseType() != MouseType.RELEASED) return;
            if (isMoving && em.getMouseButton() == 0) {                
                DeveloperDesktop.saveConfig();
            }
            isPressed = false;
            isMoving = false;
            pressPoint.set(-1, -1);
        }
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        super.actionPerformed(object, args);
    }
    
    @Override
    public void onClosed() {
        getTopParent().unregisterListener(this);
    }
    
    //=========
    // Methods
    //=========
    
    public void tryOpenShortcut() {
        try {
            openShortcut();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract void openShortcut() throws Exception;
    public abstract void openShortcutRCM();
    public abstract void generateSaveString(EStringBuilder sb);
    
    protected RightClickMenu createBaseShortcutRCM() {
        if (shortcutRCM != null) {
            shortcutRCM.close();
            shortcutRCM = null;
        }
        
        shortcutRCM = new RightClickMenu("Shortcut Options");
        
        return shortcutRCM;
    }
    
    protected void generateBaseSaveString(EStringBuilder sb) {
        sb.println(type.text, ": ", shortcutName);
        sb.incrementTabCount();
        sb.println("position: [", startX, ",", startY, "]");
    }
    
    protected void checkMouseMove() {
        if (isMoving) return;
        // don't care if not pressed or LMB isn't down
        if (!isPressed || !Mouse.isLeftDown()) return;
        // don't care if the mouse hasn't moved since being pressed
        if (pressPoint.distance(Mouse.getMx(), Mouse.getMy()) <= 10) return;
        
        isMoving = true;
    }
    
    protected void checkMoveShortcut(int mXIn, int mYIn) {
        if (!isMoving) return;
        
        double moveX = mXIn - oldPoint.x;
        double moveY = mYIn - oldPoint.y;
        
        // move this shortcut
        //move(moveX, moveY);
        oldPoint.set(mXIn, mYIn);
        
        // move all other shortcuts that are highlighted
        var shortcuts = DeveloperDesktop.getInstance().getHighlightedDesktopShortcuts();
        for (var s : shortcuts) {
            //if (s == this) continue;
            s.move(moveX, moveY);
        }
    }
    
    public void deleteShortcut() {
        DeveloperDesktop.removeShortcut(this);
    }
    
    public void renameShortcut() {
        
    }
    
    public void cancelRename() {
        
    }
    
    public void copyShortcut() {
        
    }
    
    public void cutShortcut() {
        
    }
    
    public void openProperties() {
        
    }
    
    //=========
    // Setters
    //=========
    
    public void setIcon(GameTexture iconIn) { icon = iconIn; }
    public void setName(String nameIn) { shortcutName = nameIn; }
    public void setSelected(boolean val) { selected = val; }
    public void setLocation(double xIn, double yIn) { this.setPosition(xIn, yIn); }
    
    public void addAdditionalProperty(String key, String value) {
        additionalProperties.put(key, value);
    }
    
    public Map<String, String> getAdditionalProperties() { return additionalProperties; }
    
    public String getPropertyValue(String key) { return additionalProperties.get(key); }
    
}
