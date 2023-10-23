package qot.entities.shopkeepers;

import envision.engine.resourceLoaders.Sprite;
import envision.game.component.ComponentType;
import envision.game.component.types.OnClickComponent;
import envision.game.effects.animations.AnimationHandler;
import envision.game.entities.BasicRenderedEntity;
import envision.game.entities.EntityRenderer;
import eutil.datatypes.util.EList;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;

public class ShopGuy extends BasicRenderedEntity {
    
    protected long randShort = 2000l;
    protected long randLong = 2500l;
    protected long waitDelay = 0l;
    protected long moveTime = 0l;
    protected long waitTime = 0l;
    protected long lastMove = 0l;
    protected Direction lastDir = Direction.N;
    
    public ShopGuy() { this(0, 0); }
    public ShopGuy(int posX, int posY) {
        super("ShopGuy");
        
        setMaxHealth(50);
        setHealth(50);
        setSpeed(32.0 * 1.0);
        
        init(posX, posY, 64, 64);
        setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
        
        Sprite sprite = EList.of(EntityTextures.walksheet.getSprite(0),
                                 EntityTextures.walksheet.getSprite(9),
                                 EntityTextures.walksheet.getSprite(18),
                                 EntityTextures.walksheet.getSprite(27)).getRandom();
        
        setSprite(sprite);
        EntityRenderer renderer = this.getComponent(ComponentType.RENDERING);
        renderer.setFlipTextureWhenMoving(false);
        
        animationHandler = new AnimationHandler(this);
        
        var walkUp = animationHandler.createAnimationSet(AnimationHandler.WALKING_UP_1);
        var walkLeft = animationHandler.createAnimationSet(AnimationHandler.WALKING_LEFT_1);
        var walkDown = animationHandler.createAnimationSet(AnimationHandler.WALKING_DOWN_1);
        var walkRight = animationHandler.createAnimationSet(AnimationHandler.WALKING_RIGHT_1);
        
        walkUp.setUpdateInterval(10);
        walkLeft.setUpdateInterval(10);
        walkDown.setUpdateInterval(10);
        walkRight.setUpdateInterval(10);
        
        for (int i = 0; i < 9; i++) walkUp.addFrame(EntityTextures.walksheet.getSprite(i));
        for (int i = 9; i < 18; i++) walkLeft.addFrame(EntityTextures.walksheet.getSprite(i));
        for (int i = 18; i < 27; i++) walkDown.addFrame(EntityTextures.walksheet.getSprite(i));
        for (int i = 27; i < 36; i++) walkRight.addFrame(EntityTextures.walksheet.getSprite(i));        
        
        addComponent(new OnClickComponent(this));
    }

    @Override
    public void onLivingUpdate(float dt) {
        animationHandler.onRenderTick();
        
        if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
            waitTime = ERandomUtil.getRoll(randShort, randLong);
            moveTime = ERandomUtil.getRoll(randShort, randLong);
            waitDelay = ERandomUtil.getRoll(800, 2000);
            lastMove = System.currentTimeMillis();
            lastDir = ERandomUtil.randomDir(false);
        }
        
        this.activeChat = "What a wonderful day!";
        
        if (System.currentTimeMillis() - lastMove >= moveTime) {
            switch (lastDir) {
            case N:
                animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_UP_1);
                animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(0));
                break;
            case S:
                animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_DOWN_1);
                animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(18));
                break;
            case NE:
            case SE:
            case E:
                animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_RIGHT_1);
                animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(27));
                break;
            case NW:
            case SW:
            case W:
                animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_LEFT_1);
                animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(9));
                break;
            default: break;
            }
            move(lastDir);
        }
        else if (animationHandler.isPlaying()) {
            animationHandler.unloadAnimation();
            animationHandler.stop();
        }
    }
    
    @Override
    public void onMousePress(int mXIn, int mYIn, int button) {
        System.out.println("BANANA");
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.SHOPKEEPER.ID;
    }
    
}
