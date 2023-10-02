package envision.game.items;

import java.text.DecimalFormat;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.game.entities.BasicRenderedEntity;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;

public class ItemOnGround extends BasicRenderedEntity {
    
    protected long spawnTime;
    protected long timeToLive = 20000;
    protected boolean doesDecay = true;
    protected Item theItem;
    protected boolean ignoreClick;
    
    public ItemOnGround(Item item) {
        super("ITEM ON GROUND");
                
        init(0, 0, 16, 16);
        theItem = item;
        sprite = item.sprite;
        invincible = true;
        doesDecay = false;
        ignoreClick = Mouse.isLeftDown();
    }
    
    public ItemOnGround(Item item, long timeToLiveIn) {
        super("ITEM ON GROUND");
        
        init(0, 0, 16, 16);
        theItem = item;
        sprite = item.sprite;
        invincible = true;
        timeToLive = timeToLiveIn;
        ignoreClick = Mouse.isLeftDown();
    }
    
    @Override
    public void onAddedToWorld(IGameWorld world) {
        spawnTime = System.currentTimeMillis();
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        // don't care if doesn't decay
        if (!doesDecay) return;
        
        long timeLeft = timeToLive - (System.currentTimeMillis() - spawnTime);
        
        if (timeLeft <= 0) {
            destroy();
            return;
        }
        
        // this is garbage but I don't know how else to do this right now
        {
            if (ignoreClick && !Mouse.isLeftDown()) {
                ignoreClick = false;
            }
            
            var p = Envision.thePlayer;
            var cb = getCollisionDims();
            var pcb = p.getCollisionDims();
            
            if (cb.partiallyContains(pcb) && !ignoreClick && Mouse.isLeftDown()) {
                if (!Envision.thePlayer.getInventory().isFull()) {
                    Envision.thePlayer.giveItem(theItem);
                    destroy();
                    return;
                }
            }
        }
        
        var df = new DecimalFormat("#0.0");
        double decimalTime = timeLeft / 1000.0;
        String timeString = "";
        if (decimalTime <= 10 && decimalTime > 5) timeString += EColors.yellow;
        else if (decimalTime <= 5) timeString += EColors.lred;
        timeString += df.format(decimalTime);
        headText = timeString;
    }
    
    public void destroy() {
        kill();
        world.removeEntity(this);
    }
    
    public boolean getDoesDecay() { return doesDecay; }
    public void setDoesDecay(boolean val) { doesDecay = val; }
    
    @Override public int getInternalSaveID() { return 0; }
    
}
