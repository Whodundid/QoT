package qot.particles;

import envision.Envision;
import envision.game.entities.Doodad;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;

public class FloatingTextEntity extends Doodad {
    
    protected String text;
    protected long timeAlive;
    protected long timeToLive = 1500;
    protected boolean dieAfterSpecifiedTime = false;
    protected EColors drawColor = EColors.white;
    
    public FloatingTextEntity(double x, double y, double w, double h, Object text) {
        super("FloatingText: " + text);
        init(x, y, w, h);
        this.text = text + "";
        setInvincible(true);
        canBeMoved = false;
        canMoveEntities = false;
    }
    
    public FloatingTextEntity(double x, double y, Object text) {
        this(x, y, 0, 0, text);
    }
    
    public FloatingTextEntity(double x, double y, double w, double h, Object text, long timeToLive) {
        super("FloatingText: " + text);
        init(0, 0, w, h);
        this.text = text + "";
        setInvincible(true);
        this.timeToLive = timeToLive;
        dieAfterSpecifiedTime = true;
        this.setPixelPos((int) x, (int) y);
        canBeMoved = false;
        canMoveEntities = false;
    }
    
    public FloatingTextEntity(double x, double y, Object text, long timeToLive) {
        this(x, y, 0, 0, text, timeToLive);
    }
    
    @Override
    public void draw(WorldCamera camera, double[] dims, boolean mouseOver) {
        double x = dims[0];
        double y = dims[1];
        double w = dims[2];
        double h = dims[3];
        
        if (dieAfterSpecifiedTime) {
            double moveAmount = 200; // 200 px
            double moveY = ((double) timeAlive / (double) timeToLive) * moveAmount;
            drawStringCS(text, x + w * 0.5, y + h * 0.5 - moveY, drawColor);
        }
        else {
            drawString(text, x, y, drawColor);
        }
    }
    
    public FloatingTextEntity setColor(EColors color) {
        drawColor = color;
        return this;
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        timeAlive += dt;
        if (!dieAfterSpecifiedTime) return;
        if (timeAlive >= timeToLive) {
            kill();
            Envision.theWorld.getEntitiesInWorld().remove(this);
        }
    }
    
    @Override
    public int getInternalSaveID() { return -1; }
    
}
