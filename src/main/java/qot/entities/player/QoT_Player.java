package qot.entities.player;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.entities.Entity;
import envision.game.entities.combat.CircularDirectionalAttack;
import envision.game.entities.player.EntityStats;
import envision.game.entities.player.Player;
import envision.game.shops.TradingWindow;
import envision.game.world.WorldCamera;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;
import qot.abilities.Abilities;
import qot.assets.textures.entity.EntityTextures;
import qot.items.Items;

public class QoT_Player extends Player {
    
    private EntityStats stats;
    
    public QoT_Player() { this("Player", 0, 0); }
    public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
    public QoT_Player(String nameIn, int posX, int posY) {
        super(nameIn);
        
        //init(posX, posY, 32, 32);
        init(posX, posY, 40, 40);
        
        setMaxHealth(10);
        setHealth(10);
        setBaseMeleeDamage(1);
        setMaxRange(65.0);
        
        baseInventorySize = 20;
        inventory.setSize(baseInventorySize);
        
        inventory.setItem(0, Items.lesserHealing);
        inventory.setItem(1, Items.lesserMana);
        
        setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
        sprite = new Sprite(EntityTextures.player);
        timeUntilNextAttack = 175l;
        
        addComponent(new PlayerRenderer(this));
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onMousePress(int mXIn, int mYIn, int button) {
        if (Envision.getCurrentScreen().isWindowOpen(TradingWindow.class)) return;
        if (Envision.getCurrentScreen().isWindowOpen(RightClickMenu.class)) return;
        
        final var cam = world.getCamera();
        final double mpx = cam.getMousePixelX();
        final double mpy = cam.getMousePixelY();
        CircularDirectionalAttack.attackAt(world, this, midX, midY, mpx, mpy, 45.0);
        
        if (!attacking && button == 0) {
            attacking = true;
            recentlyAttacked = true;
            attackDrawStart = System.currentTimeMillis();
            attackStart = System.currentTimeMillis();
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
        final var cam = world.getCamera();
        final double mpx = cam.getMousePixelX();
        final double mpy = cam.getMousePixelY();
        CircularDirectionalAttack.drawAttackAreaSector(this, this.midX, this.midY, mpx, mpy, 45);
    }
    
    @Override
    public int getInternalSaveID() { return 0; }
    
}
