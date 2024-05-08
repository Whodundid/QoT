package qot.entities.player;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.registry.types.Sprite;
import envision.engine.registry.types.SpriteSheet;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.animations.AnimationHandler;
import envision.game.component.ComponentType;
import envision.game.effects.OutOfStaminaEffect;
import envision.game.entities.EntityRenderer;
import envision.game.entities.combat.CircularDirectionalAttack;
import envision.game.entities.player.Player;
import envision.game.shops.TradingWindow;
import envision.game.world.WorldCamera;
import eutil.random.ERandomUtil;
import qot.abilities.Abilities;
import qot.assets.textures.entity.EntityTextures;
import qot.items.Items;

public class QoT_Player extends Player {
    
    private long timeSinceLastBlink;
    private long delayTillNextBlink;
    
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
        //setSpeed(100);
        setSpeed(145);
        maxStamina = 100;
        stamina = 100;
        
        baseInventorySize = 20;
        inventory.setSize(baseInventorySize);
        inventory.setItem(0, Items.lesserHealing);
        inventory.setItem(1, Items.lesserMana);
        
        setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
        final SpriteSheet sheet = EntityTextures.amyIdleWalkSheet;
        sprite = sheet.getSprite(0);
        sprite = new Sprite(EntityTextures.player);
        timeUntilNextAttack = 175l;
        
        addComponent(new PlayerRenderer(this));
        EntityRenderer renderer = this.getComponent(ComponentType.RENDERING);
        //renderer.setFlipTextureWhenMoving(false);
        
        animationHandler = new AnimationHandler(this);
        
        var idleFront = animationHandler.createAnimationSet("idle_front");
        var idleRight = animationHandler.createAnimationSet("idle_right");
        var idleLeft = animationHandler.createAnimationSet("idle_left");
        var idleBack = animationHandler.createAnimationSet("idle_back");
        var idleFrontBlink = animationHandler.createAnimationSet("idle_front_blink");
        var idleRightBlink = animationHandler.createAnimationSet("idle_right_blink");
        var idleLeftBlink = animationHandler.createAnimationSet("idle_left_blink");
        var walkDown = animationHandler.createAnimationSet("walk_down");
        var walkRight = animationHandler.createAnimationSet("walk_right");
        var walkLeft = animationHandler.createAnimationSet("walk_left");
        var walkUp = animationHandler.createAnimationSet("walk_up");
        
        animationHandler.setGlobalUpdateInterval(40);
        idleFrontBlink.setUpdateInterval(10);
        idleRightBlink.setUpdateInterval(10);
        idleLeftBlink.setUpdateInterval(10);
        walkDown.setUpdateInterval(10);
        walkRight.setUpdateInterval(10);
        walkLeft.setUpdateInterval(10);
        walkUp.setUpdateInterval(10);
        
        for (int i = 0; i <= 3; i++) idleFront.addFrame(sheet.getSprite(i));
        for (int i = 4; i <= 7; i++) idleRight.addFrame(sheet.getSprite(i));
        for (int i = 20; i <= 23; i++) idleLeft.addFrame(sheet.getSprite(i));
        for (int i = 16; i <= 19; i++) idleBack.addFrame(sheet.getSprite(i));
        for (int i = 8; i <= 11; i++) idleFrontBlink.addFrame(sheet.getSprite(i));
        idleFrontBlink.addFrame(sheet.getSprite(10));
        idleFrontBlink.addFrame(sheet.getSprite(9));
        idleFrontBlink.addFrame(sheet.getSprite(8));
        for (int i = 12; i <= 15; i++) idleRightBlink.addFrame(sheet.getSprite(i));
        idleRightBlink.addFrame(sheet.getSprite(14));
        idleRightBlink.addFrame(sheet.getSprite(13));
        idleRightBlink.addFrame(sheet.getSprite(12));
        for (int i = 28; i <= 31; i++) idleLeftBlink.addFrame(sheet.getSprite(i));
        idleLeftBlink.addFrame(sheet.getSprite(30));
        idleLeftBlink.addFrame(sheet.getSprite(29));
        idleLeftBlink.addFrame(sheet.getSprite(28));
        for (int i = 32; i <= 39; i++) walkDown.addFrame(sheet.getSprite(i));
        for (int i = 40; i <= 47; i++) walkRight.addFrame(sheet.getSprite(i));
        for (int i = 48; i <= 55; i++) walkLeft.addFrame(sheet.getSprite(i));
        for (int i = 56; i <= 63; i++) walkUp.addFrame(sheet.getSprite(i));
        
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
            //this.drainStamina(10);
            //activeEffectsTracker.addEffect(new OutOfStaminaEffect(200));
            
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
        //this.headText = "" + facing;
        animationHandler.onRenderTick((long) dt);
        abilityTracker.onGameTick(dt);
        
        //if (!isMoving) playIdleAnimation();
        //else playMoveAnimation();
        
        if (!spellbook.knowsAbility(Abilities.ringOfFire)) {
            this.spellbook.learnAbility(Abilities.ringOfFire);
            this.abilityTracker.addAbility(Abilities.ringOfFire);
        }
        
        if (!spellbook.knowsAbility(Abilities.dodgeroll)) {
            this.spellbook.learnAbility(Abilities.dodgeroll);
            this.abilityTracker.addAbility(Abilities.dodgeroll);
        }
        
        if (!spellbook.knowsAbility(Abilities.fireball) && magicLevel >= 5) {
            this.spellbook.learnAbility(Abilities.fireball);
            this.abilityTracker.addAbility(Abilities.fireball);
        }
        // Add more learnable abilities here? Could be its own method
        
        super.onLivingUpdate(dt);
    }
    
    @Override
    public void preDraw(WorldCamera camera, double[] dims, boolean mouseOver) {
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
//        
//        Vector3f player = new Vector3f((float) midX, (float) midY, 0.0f);
//        Vector3f mouse = new Vector3f((float) mpx, (float) mpy, 0.0f);
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
    
    protected void playIdleAnimation() {
        if (System.currentTimeMillis() - timeSinceLastBlink >= delayTillNextBlink) {
            timeSinceLastBlink = System.currentTimeMillis();
            delayTillNextBlink = ERandomUtil.getRoll(5000, 9000);
            
            switch (facing) {
            case DOWN: animationHandler.switchToAnimationSet("idle_front_blink"); break;
            case LEFT: animationHandler.switchToAnimationSet("idle_left_blink"); break;
            case RIGHT: animationHandler.switchToAnimationSet("idle_right_blink"); break;
            case UP: animationHandler.switchToAnimationSet("idle_back"); break;
            default:
            }
        }
        else if (animationHandler.isAnimationLoaded()) {
            switch (facing) {
            case DOWN: animationHandler.switchToAnimationSet("idle_front"); break;
            case LEFT: animationHandler.switchToAnimationSet("idle_left"); break;
            case RIGHT: animationHandler.switchToAnimationSet("idle_right"); break;
            case UP: animationHandler.switchToAnimationSet("idle_back"); break;
            default:
            }
        }
    }
    
    protected void playMoveAnimation() {
        switch (facing) {
        case UP:
            animationHandler.playIfNotAlreadyPlaying("walk_up");
            animationHandler.setEntityBaseTexture("idle_back", 0);
            break;
        case DOWN:
            animationHandler.playIfNotAlreadyPlaying("walk_down");
            animationHandler.setEntityBaseTexture("idle_front", 0);
            break;
        case RIGHT:
            animationHandler.playIfNotAlreadyPlaying("walk_right");
            animationHandler.setEntityBaseTexture("idle_right", 0);
            break;
        case LEFT:
            animationHandler.playIfNotAlreadyPlaying("walk_left");
            animationHandler.setEntityBaseTexture("idle_left", 0);
            break;
        default: break;
        }
    }
    
    @Override
    public int getInternalSaveID() { return 0; }
    
}
