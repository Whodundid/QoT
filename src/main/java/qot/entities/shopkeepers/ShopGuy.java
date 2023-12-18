package qot.entities.shopkeepers;

import envision.Envision;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.component.ComponentType;
import envision.game.component.types.OnClickComponent;
import envision.game.effects.animations.AnimationHandler;
import envision.game.entities.BasicRenderedEntity;
import envision.game.entities.Enemy;
import envision.game.entities.Entity;
import envision.game.entities.EntityRenderer;
import envision.game.items.Item;
import envision.game.shops.Shop;
import envision.game.shops.Shopkeeper;
import envision.game.world.GameWorld;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class ShopGuy extends BasicRenderedEntity implements Shopkeeper {
    
    protected long randShort = 2000l;
    protected long randLong = 2500l;
    protected long waitDelay = 0l;
    protected long moveTime = 0l;
    protected long waitTime = 0l;
    protected long lastMove = 0l;
    protected long lastDialogTime = 0l;
    protected long dialogWaitTime = 0l;
    protected long dialogTimeOut = 0l;
    protected long timeSinceItemWasLastSold = 0l;
    protected long timeSinceItemWasLastBought = 0l;
    protected boolean inCombat;
    protected boolean hasRecentlySoldItems;
    protected boolean hasRecentlyBoughtItems;
    protected boolean hit = false;
    protected long timeSinceLastHit;
    protected Direction lastDir = Direction.N;
    protected Shop theShop = new Shop();
    
    public ShopGuy() { this(0, 0); }
    public ShopGuy(int posX, int posY) {
        super("ShopGuy");
        
        setMaxHealth(50);
        setHealth(50);
        setSpeed(32.0 * 1.0);
        setBaseMeleeDamage(2);
        
        this.baseInventorySize = 12;
        this.getInventory().setSize(this.baseInventorySize);
        int amount = ERandomUtil.getRoll(0, 4);
        for (int i = 0; i < amount; i++) {
            this.getInventory().addItem(Items.random());            
        }
        this.setGold(ERandomUtil.getRoll(0, 2500));
        
        theShop.addShopkeeper(this);
        
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
        waitTime = ERandomUtil.getRoll(randShort, randLong);
        determineOverheadChat(dt);
        if (isCurrentlySelling()) {
            if (animationHandler.isPlaying()) {
                animationHandler.unloadAnimation();
                animationHandler.stop();
            }
        }
        else if (timeSinceItemWasLastSold >= (60000 * 1.5)) {
            System.out.println(this + " RESTOCKED");
            this.setGold(ERandomUtil.getRoll(125, 1250));
            for (int i = 0; i < inventory.size(); i++) {
                inventory.setItem(i, null);
            }
            int amount = ERandomUtil.getRoll(0, 7);
            for (int i = 0; i < amount; i++) {
                this.getInventory().addItem(Items.random());            
            }
            timeSinceItemWasLastSold = 0L;
        }

        if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
            moveTime = ERandomUtil.getRoll(randShort, randLong);
            waitDelay = ERandomUtil.getRoll(800, 2000);
            lastMove = System.currentTimeMillis();
            lastDir = ERandomUtil.randomDir(false);
        }
        
        var dist = world.getDistance(Envision.thePlayer, this);
        EList<Entity> enemiesNear = world.getAllEntitiesWithinDistance(this, 200).stream().filter(e -> e instanceof Enemy || isEnemiesWith(e))
            .sorted((a, b) -> Integer.compare(a.getFavorTracker().getFavorWithEntity(this), b.getFavorTracker().getFavorWithEntity(this)))
            .collect(EList.toEList());
        Entity closeEnemy = enemiesNear.getFirst();
        boolean nearEnemy = closeEnemy != null;
        double distToEnemy = 0.0;
        if (closeEnemy != null) distToEnemy = world.getDistance(closeEnemy, this);
        
        if (nearEnemy) {
            if (distToEnemy > 150) {
                inCombat = false;
                this.headText = "";
                double angle = world.getAngleInDegressTo(this, closeEnemy);
                faceTowards(angle);
            }
            else {
                inCombat = true;
                Direction dirToEnemy = world.getDirectionTo(this, closeEnemy);
                //double angle = world.getAngleInDegressTo(this, closeEnemy);
                Dimension_d testDim = getCollisionDims();
                Dimension_d pDims = closeEnemy.getCollisionDims();
                this.headText = "" + dirToEnemy;
                
                if (testDim.partiallyContains(pDims)) {
                    if (hit) {
                        //System.out.println(System.currentTimeMillis() - timeSinceLastHit);
                        if ((System.currentTimeMillis() - timeSinceLastHit) >= 500) {
                            hit = false;
                        }
                    }
                    else {
                        hit = true;
                        timeSinceLastHit = System.currentTimeMillis();
                        closeEnemy.attackedBy(this, getBaseMeleeDamage());
                        
                        if (closeEnemy.isDead()) {
                            Envision.theWorld.removeEntity(closeEnemy);
                        }
                        
                        EList<String> chat = EList.newList();
                        chat.add("How do you like that!");
                        chat.add("Take this!");
                        chat.add("RAWWWW");
                        chat.add("DIE!");
                        for (int i = 0; i < 4; i++) chat.add("");
                        speak(chat.getRandom(), 2000, 4000);
                    }
                }
                
                moveTowards(dirToEnemy);
            }
        }
        else {
            inCombat = false;
            this.headText = "";
            
            if (dist < 60 && Envision.thePlayer != null) {
                double angle = world.getAngleInDegressTo(this, Envision.thePlayer);
                faceTowards(angle);
            }
            else if (System.currentTimeMillis() - lastMove >= moveTime) {
                moveTowards(lastDir);
            }
            else if (animationHandler.isPlaying()) {
                animationHandler.unloadAnimation();
                animationHandler.stop();
            }
        }
    }
    
    protected void faceTowards(double angle) {
        faceTowards(Direction.getDirection(angle));
    }
    protected void faceTowards(Direction direction) {
        switch (direction) {
        case N:
            animationHandler.setCurrentAnimation(AnimationHandler.WALKING_UP_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(0));
            break;
        case S:
            animationHandler.setCurrentAnimation(AnimationHandler.WALKING_DOWN_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(18));
            break;
        case E:
            animationHandler.setCurrentAnimation(AnimationHandler.WALKING_RIGHT_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(27));
            break;
        case W:
            animationHandler.setCurrentAnimation(AnimationHandler.WALKING_LEFT_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(9));
            break;
        default: break;
        }
    }
    
    protected void moveTowards(double angle) {
        moveTowards(Direction.getDirection(angle));
    }
    protected void moveTowards(Direction direction) {
        //faceTowards(direction);
        switch (direction) {
        case N:
            //animationHandler.setCurrentAnimation(AnimationHandler.WALKING_UP_1);
            animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_UP_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(0));
            break;
        case S:
            //animationHandler.setCurrentAnimation(AnimationHandler.WALKING_DOWN_1);
            animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_DOWN_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(18));
            break;
        case NE:
        case SE:
        case E:
            //animationHandler.setCurrentAnimation(AnimationHandler.WALKING_RIGHT_1);
            animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_RIGHT_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(27));
            break;
        case NW:
        case SW:
        case W:
            //animationHandler.setCurrentAnimation(AnimationHandler.WALKING_LEFT_1);
            animationHandler.playIfNotAlreadyPlaying(AnimationHandler.WALKING_LEFT_1);
            animationHandler.setEntityBaseTexture(EntityTextures.walksheet.getSprite(9));
            break;
        default: break;
        }
        move(direction);
    }
    
    @Override
    public void attackedBy(Entity ent, int amount) {
        super.attackedBy(ent, amount);
        
        EList<String> chat = EList.newList();
        if (health <= (maxHealth * 0.25)) {
            chat.add("Please!");
            chat.add("Mercy!");
            chat.add("I BEG YOU PLEASE STOP!");
            chat.add("I don't want to die!");
        }
        else {
            if (isPositiveFavorWith(ent)) chat.add("Please be careful!");
            if (isPositiveFavorWith(ent)) chat.add("Could you by chance NOT do that?!");
            if (isNeutralWith(ent)) chat.add("Whoa! What have I done to you?!");
            if (isNeutralWith(ent)) chat.add("You looking for a fight!?");
            if (isNeutralWith(ent)) chat.add("Stop that!");
            if (isNegativeFavorWith(ent)) chat.add("WATCH IT, ASSHOLE!");
            if (isEnemiesWith(ent)) chat.add("I'll kill you I swear!");
            if (isEnemiesWith(ent)) chat.add("You'll pay for that!");
        }
        chat.add("OWWW!");
        chat.add("AckKK!");
        chat.add("Auhgh!");
        
        speak(chat.getRandom());
    }
    
    protected void determineOverheadChat(float dt) {
        lastDialogTime += (long) dt;
        timeSinceItemWasLastBought += (long) dt;
        timeSinceItemWasLastSold += (long) dt;
        
        if (timeSinceItemWasLastBought >= 100000000L) timeSinceItemWasLastBought = 100000L;
        if (timeSinceItemWasLastSold >= 100000000L) timeSinceItemWasLastSold = 100000L;
        if (lastDialogTime >= dialogTimeOut) this.activeChat = "";
        if (lastDialogTime >= dialogWaitTime) {
            lastDialogTime = 0l;
            dialogTimeOut = ERandomUtil.getRoll(3000, 5000);
            dialogWaitTime = ERandomUtil.getRoll(dialogTimeOut + 2000, dialogTimeOut + 8000);
        }
        else return;
        
        EList<String> combatText = EList.newList();
        combatText.add("Time to die!");
        combatText.add("You wanted a fight, now you've got it!");
        combatText.add("Get over here and fight me!");
        combatText.add("I am going to enjoy gutting you!");
        combatText.add("You're going to wish you were dead when I am done with you!");
        
        EList<String> sellingText = EList.newList();
        if (isPositiveFavorWithPlayer()) sellingText.add("What are you intersted in trading?");
        if (isPositiveFavorWithPlayer()) sellingText.add("Perhaps I can help you find something?");
        if (isNegativeFavorWithPlayer()) sellingText.add("Are you actually going to trade or just stand there?");
        if (isNegativeFavorWithPlayer()) sellingText.add("I haven't got all day!");
        if (isNegativeFavorWithPlayer()) sellingText.add("Please stop wasting my time..");
        if (isNegativeFavorWithPlayer()) sellingText.add("Either buy something or get out of my sight!");
        
        System.out.println("FAVOR: " + getFavorTracker().getFavorWithEntity(Envision.thePlayer) + " : " + timeSinceItemWasLastSold);
        
        EList<String> playerNearOptions = EList.newList();
        if (isNeutralWithPlayer()) playerNearOptions.add("Can I help you?");
        if (isNeutralWithPlayer()) playerNearOptions.add("Hey, you want to trade?");
        if (isPositiveFavorWithPlayer()) playerNearOptions.add("You there, I've got a favor to ask!");
        if (isFriendsWithPlayer()) playerNearOptions.add("Hey there friend, want to trade?");
        if (isFriendsWithPlayer()) playerNearOptions.add("Ahhh, my favorite customer!");
        if (isNegativeFavorWithPlayer()) playerNearOptions.add("What are you doing?");
        if (isNegativeFavorWithPlayer()) playerNearOptions.add("I should really put up a 'No Loitering' sign..");
        if (isNegativeFavorWithPlayer()) playerNearOptions.add("You're not going to rob me, are you?");
        if (isNegativeFavorWithPlayer()) playerNearOptions.add("Oh dear not you again..");
        if (isEnemiesWithPlayer()) playerNearOptions.add("You are not welcome here!");
        if (isEnemiesWithPlayer()) playerNearOptions.add("Get out of my shop, " + Envision.thePlayer + "!");
        if (isEnemiesWithPlayer()) playerNearOptions.add("Get out of here, " + Envision.thePlayer + "!");
        if (isEnemiesWithPlayer()) playerNearOptions.add("You looking for a fight, asshole?");
        
        String time = "day";
        if (world.isDay()) time = "day";
        else if (world.isNight()) time = "night";
        else if (world.isSunrise()) time = "morning";
        else if (world.isSunset()) time = "evening";
        
        EList<String> textOptions = EList.newList();
        if (health <= (maxHealth * 0.25)) {
            textOptions.add("Guhh!");
            textOptions.add("The paaaaiiiinnnn..");
            textOptions.add("I think I'm dying..");
            textOptions.add("Blugghh");
            textOptions.add("I don't think I can take another hit like that...");
        }
        else {
            textOptions.add("What a wonderful " + time + "!");
            textOptions.add("Maybe someone will actually trade today");
            textOptions.add("5 more years Horvic.. Just 5 more years...");
            textOptions.add("Those dang kids keep making so much noise!");
            textOptions.add("I think the goblins stole a " + Items.random().getName().toLowerCase() + " of mine..");
            textOptions.add("I wonder what time it is...");
            textOptions.add("** Sigh **");            
        }
        
        EList<String> enemyNearOptions = EList.newList();
        enemyNearOptions.add("Get away from me vermin!");
        enemyNearOptions.add("Don't try it!");
        enemyNearOptions.add("Stay away from me you wretched beast!");
        
        var dist = world.getDistance(Envision.thePlayer, this);
        
        EList<Entity> entities = world.getAllEntitiesWithinDistance(this, 200);
        boolean nearEnemy = entities.containsInstanceOf(Enemy.class);
        
        if (inCombat) {
            this.activeChat = combatText.getRandom();
        }
        else if (isCurrentlySelling()) {
            this.activeChat = sellingText.getRandom();
        }
        else if (dist < 120 && entities.contains(Envision.thePlayer)) {
            this.activeChat = playerNearOptions.getRandom();
        }
        else if (nearEnemy) {
            this.activeChat = enemyNearOptions.getRandom();
        }
        else {
            this.activeChat = textOptions.getRandom();
        }
    }
    
    protected void speak(String text) {
        activeChat = text;
        lastDialogTime = 0l;
        dialogTimeOut = ERandomUtil.getRoll(3000, 5000);
        dialogWaitTime = ERandomUtil.getRoll(dialogTimeOut + 2000, dialogTimeOut + 8000);
    }
    
    protected void speak(String text, long minTime, long maxTime) {
        activeChat = text;
        lastDialogTime = 0l;
        dialogTimeOut = ERandomUtil.getRoll(minTime, maxTime);
        dialogWaitTime = ERandomUtil.getRoll(dialogTimeOut + 2000, dialogTimeOut + 8000);
    }
    
    @Override
    public void onMousePress(int mXIn, int mYIn, int button) {
        if (button == 1) {
            RightClickMenu rcm = new RightClickMenu(getName());
            rcm.addOption("Trade", () -> theShop.openShopWindow(this, Envision.thePlayer));
            rcm.displayOnCurrent();
        }
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.SHOPKEEPER.ID;
    }
    
    @Override public Shop getShop() { return theShop; }
    @Override public void setShop(Shop shopIn) { theShop = shopIn;}
    @Override public boolean isCurrentlySelling() { return theShop.isShopOpen(); }
    @Override public void setCurrentlySelling(boolean val) {}
    
    @Override
    public void itemWasBought(Entity buyingEntity, Item item) {
        EList<String> chat = EList.newList();
        chat.add("I'm sure I'll find a use for it.");
        chat.add("Such fine quality too!");
        chat.add("Indeed");
        chat.add("Mhmmm");
        if (isAnnoyedWith(buyingEntity)) chat.add("Didn't really want that anyways..");
        
        speak(chat.getRandom());
        hasRecentlyBoughtItems = true;
        timeSinceItemWasLastBought = 0L;
    }
    
    @Override
    public void itemWasSold(Entity entitySoldTo, Item item) {
        EList<String> chat = EList.newList();
        if (isPositiveFavorWith(entitySoldTo)) chat.add("I'm sure you'll find a good use for it.");
        if (isPositiveFavorWith(entitySoldTo)) chat.add("I hope you enjoy it!");
        if (isNeutralWith(entitySoldTo)) chat.add("Very well");
        if (isNeutralWith(entitySoldTo)) chat.add("Indeed");
        if (isNeutralWith(entitySoldTo)) chat.add("Mhmmm");
        if (isNeutralWith(entitySoldTo)) chat.add("Anything else?");
        if (isAnnoyedWith(entitySoldTo)) chat.add("You're not very bright are you?");
        if (isAnnoyedWith(entitySoldTo)) chat.add("That thing was garbage anyways");
        if (isAnnoyedWith(entitySoldTo)) chat.add("I see you fancy actual trash");
        if (isAnnoyedWith(entitySoldTo)) chat.add("Great, now take it and get out..");
        if (isAnnoyedWith(entitySoldTo)) chat.add("You truly have terrible taste.");
        
        speak(chat.getRandom());
        hasRecentlySoldItems = true;
        timeSinceItemWasLastSold = 0L;
    }
    
}
