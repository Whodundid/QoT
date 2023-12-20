package qot.entities.player;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.entities.Entity;
import envision.game.entities.combat.CircularDirectionalAttack;
import envision.game.entities.player.Player;
import envision.game.entities.player.PlayerStats;
import envision.game.shops.TradingWindow;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;
import qot.abilities.Abilities;
import qot.assets.textures.entity.EntityTextures;
import qot.items.Items;

public class QoT_Player extends Player {
    
    private PlayerStats stats;
    
    public QoT_Player() { this("Player", 0, 0); }
    public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
    public QoT_Player(String nameIn, int posX, int posY) {
        super(nameIn);
        
        //init(posX, posY, 32, 32);
        init(posX, posY, 40, 40);
        
        setMaxHealth(10);
        setHealth(10);
        setBaseMeleeDamage(1);
        
        baseInventorySize = 20;
        inventory.setSize(baseInventorySize);
        
        inventory.setItem(0, Items.lesserHealing);
        inventory.setItem(1, Items.lesserMana);
        
        setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
        sprite = new Sprite(EntityTextures.player);
        timeUntilNextAttack = 175l;
        
        addComponent(new PlayerRenderer(this));
    }
    
    //----------------
    // Player Getters
    //----------------
    
    @Override
    public void onMousePress(int mXIn, int mYIn, int button) {
        if (Envision.getCurrentScreen().isWindowOpen(TradingWindow.class)) return;
        if (Envision.getCurrentScreen().isWindowOpen(RightClickMenu.class)) return;
        
        final var cam = world.getCamera();
        final double mpx = cam.getMousePixelX();
        final double mpy = cam.getMousePixelY();
        CircularDirectionalAttack.lastX = mXIn;
        CircularDirectionalAttack.lastY = mYIn;
        CircularDirectionalAttack.attackAt(midX, midY, mpx, mpy, 90.0);
        
        if (!attacking && button == 0) {
            attacking = true;
            recentlyAttacked = true;
            attackDrawStart = System.currentTimeMillis();
            attackStart = System.currentTimeMillis();
            
            EList<Entity> inRange = new EArrayList<>();
            for (var e : Envision.theWorld.getEntitiesInWorld()) {
                if (e == this) continue;
                if (e.isInvincible()) continue;
                if ((Envision.theWorld).getDistance(e, this) < 50) inRange.add((Entity) e);
            }
            
            for (var e : inRange) {
                var damage = getBaseMeleeDamage();
                e.attackedBy(this, damage);
                //addObject(new DamageSplash(e.startX + e.midX, e.startY + e.midY, damage));
                if (e.isDead()) {
                    //if (e instanceof Thyrah) giveItem(Items.random());
                    //else if (ERandomUtil.roll(5, 0, 10)) giveItem(Items.random());
                    
                    getStats().addKilled(1);
                    Envision.theWorld.removeEntity(e);
                    addXP(e.getExperienceRewardedOnKill());
                    if (ERandomUtil.roll(0, 0, 4)) setGold(getGold() + ERandomUtil.getRoll(1, 10));
                }
            }
        }
    }
    
    @Override
    public void onKeyPress(char typedChar, int keyCode) {
        if (keyCode >= Keyboard.KEY_0 && keyCode <= Keyboard.KEY_9) {
            int ability = keyCode - Keyboard.KEY_1;
            abilityTracker.useAbility(ability);
        }
        
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        abilityTracker.onGameTick(dt);
        
        if (!spellbook.knowsAbility(Abilities.fireball) && magicLevel >= 5) {
            this.spellbook.learnAbility(Abilities.fireball);
            this.abilityTracker.addAbility(Abilities.fireball);
        }
        // Add more learnable abilities here? Could be its own method
    }
    
    @Override
    public void draw(WorldCamera camera, double x, double y, double w, double h, boolean mouseOver) {
        double midX = x + w * 0.5;
        double midY = y + h * 0.5;
        
        final var cam = world.getCamera();
        final double mpx = cam.getMousePixelX();
        final double mpy = cam.getMousePixelY();
        CircularDirectionalAttack.lastX = Mouse.getMx_double();
        CircularDirectionalAttack.lastY = Mouse.getMy_double();
        CircularDirectionalAttack.attackAt(this.midX, this.midY, mpx, mpy, 15.0);
        
        if (CircularDirectionalAttack.set) {
            double lastX = CircularDirectionalAttack.lastX;
            double lastY = CircularDirectionalAttack.lastY;
            double upperX = CircularDirectionalAttack.upperX;
            double upperY = CircularDirectionalAttack.upperY;
            double lowerX = CircularDirectionalAttack.lowerX;
            double lowerY = CircularDirectionalAttack.lowerY;
            double upperPX = CircularDirectionalAttack.upperPX;
            double upperPY = CircularDirectionalAttack.upperPY;
            double lowerPX = CircularDirectionalAttack.lowerPX;
            double lowerPY = CircularDirectionalAttack.lowerPY;
            
            drawLine(midX, midY, lowerX, lowerY, EColors.red);
            drawStringCS((int) lowerPX + " : " + (int) lowerPY, lowerX, lowerY, EColors.red);
            
            drawLine(midX, midY, lastX, lastY, EColors.yellow);
            drawStringCS((int) mpx + " : " + (int) mpy, lastX, lastY, EColors.yellow);
            
            drawLine(midX, midY, upperX, upperY, EColors.blue);
            drawStringCS((int) upperPX + " : " + (int) upperPY, upperX, upperY, EColors.blue);
        }
    }
    
    @Override
    public int getInternalSaveID() { return 0; }
    
}
