package qot.entities.player;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.registry.types.Sprite;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.entities.combat.CircularDirectionalAttack;
import envision.game.entities.player.EntityStats;
import envision.game.entities.player.Player;
import envision.game.shops.TradingWindow;
import envision.game.world.WorldCamera;
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
        setMaxRange(50.0);
        maxStamina = 100;
        stamina = 100;
        
        baseInventorySize = 20;
        inventory.setSize(baseInventorySize);
        
        inventory.setItem(0, Items.lesserHealing);
        inventory.setItem(1, Items.lesserMana);
        
        setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
        sprite = new Sprite(EntityTextures.player);
        timeUntilNextAttack = 175l;
        
        addComponent(new PlayerRenderer(this));
        canMoveEntities = true;
        canBeMoved = false;
        canRegenHealth = true;
        canRegenStamina = true;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onMousePress(int mXIn, int mYIn, int button) {
        if (world == null) return;
        if (Envision.getCurrentScreen().isWindowOpen(TradingWindow.class)) return;
        if (Envision.getCurrentScreen().isWindowOpen(RightClickMenu.class)) return;
        
        if (!attacking && button == 0) {
            attacking = true;
            recentlyAttacked = true;
            attackDrawStart = System.currentTimeMillis();
            attackStart = System.currentTimeMillis();
            
            final var cam = Envision.levelManager.getCamera();
            final double mpx = cam.getMxPixel();
            final double mpy = cam.getMyPixel();
            CircularDirectionalAttack.attackAt(world, this, midX, midY, mpx, mpy, 55.0);
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
        
        if (!spellbook.knowsAbility(Abilities.dodgeroll)) {
            this.spellbook.learnAbility(Abilities.dodgeroll);
            this.abilityTracker.addAbility(Abilities.dodgeroll);
        }
        
        if (!spellbook.knowsAbility(Abilities.fireball) && magicLevel >= 5) {
            this.spellbook.learnAbility(Abilities.fireball);
            this.abilityTracker.addAbility(Abilities.fireball);
        }
        // Add more learnable abilities here? Could be its own method
    }
    
    @Override
    public void draw(WorldCamera camera, double[] dims, boolean mouseOver) {
        if (this != Envision.thePlayer) return;
        
        final float mpx = camera.getMxPixelf();
        final float mpy = camera.getMyPixelf();
        
        CircularDirectionalAttack.drawAttackAreaSector(this, this.midX, this.midY, mpx, mpy, 55);
        
//        int tw = world.getTileWidth();
//        int th = world.getTileHeight();
//        int tileX = camera.getMxTile();
//        int tileY = camera.getMyTile();
//        final double[] area = camera.convertWorldPxToScreenPx(tileX * tw, tileY * th);
//        double x = area[0];
//        double y = area[1];
//        final double zoom = camera.getZoom();
//        
//        drawHRect(x, y, x + tw * zoom, y + th * zoom, 1, EColors.chalk);
//        drawString(((int) mpx / tw) + " : " + ((int) mpy / th), 150, 60);
        
        //Vector3f player = new Vector3f((float) midX, (float) midY, 0.0f);
       // Vector3f mouse = new Vector3f((float) mpx, (float) mpy, 0.0f);
//        RayCaster.RayCastResult result = RayCaster.checkRaycastHit(world, player, mouse, 100, Mouse.isLeftDown());
//        
//        drawLine(dims[0] + dims[2] * 0.5, dims[1] + dims[3] * 0.5, Mouse.getMx_double(), Mouse.getMy_double(), 3, EColors.red);
//        
//        if (result.collided) {
//            Vector3f intersection = result.intersection;
//            intersection.x *= tw;
//            intersection.y *= th;
//            double[] px = camera.convertWorldPxToScreenPx(intersection.x, intersection.y);
//            drawRect(px[0] - 5, px[1] - 5, px[0] + 5, px[1] + 5, EColors.yellow);
//        }
    }
    
    @Override
    public int getInternalSaveID() { return 0; }
    
}
