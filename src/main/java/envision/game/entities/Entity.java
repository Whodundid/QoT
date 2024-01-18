package envision.game.entities;

import java.util.HashMap;
import java.util.Map;

import envision.Envision;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.abilities.ActiveAbilityTracker;
import envision.game.abilities.EntitySpellbook;
import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.types.death.OnDeathComponent;
import envision.game.entities.inventory.EntityInventory;
import envision.game.entities.physics.EntityPhysicsHandler;
import envision.game.entities.physics.MovementCollisionHelper;
import envision.game.entities.player.EntityStats;
import envision.game.entities.util.EntityHealthBar;
import envision.game.entities.util.EntityLevel;
import envision.game.items.Item;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.math.vectors.Vec3d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import eutil.strings.EStringBuilder;
import qot.abilities.Abilities;
import qot.particles.FloatingTextEntity;

public abstract class Entity extends ComponentBasedObject {
    
    //========
    // Fields
    //========
    
    public String headText = "";
    public String activeChat = "";
    public boolean passable = false;
    public boolean allowNoClip = false;
    
    /** This entity's current level. */
    public int level;
    /** The amount of experience rewarded when killed. */
    public long experienceRewarded = 0;
    /** This entity's total amount of experience earned. */
    public long experience;
    /** The amount of experience needed for the next immediate level up. */
    public long xpNeeded;
    /** The amount XP an entity would earn upon killing this entity. */
    public long experienceWorth;
    /**
     * The maximum amount of health this entity can have. 'Determined by
     * hitpoints level'
     */
    public int maxHealth;
    /** The actual current amount of health this entity has. */
    public int health;
    /**
     * The maximum amount of mana this entity can have. 'Determined by magic
     * level'
     */
    public int maxMana;
    /** The actual amount of mana this entity has. */
    public int mana;
    
    public int stamina;
    public int maxStamina;
    public int staminaRechargeRate;
    public int staminaAttackDelay = 500;
    
    /** The amount of inventory slots this entity has. Default is 3. */
    public int baseInventorySize = 3;
    /** The entity's inventory. */
    public EntityInventory inventory;
    /**
     * Instead of making the gold an entity has an inventory item, gold is
     * stored directly on the character themselves.
     */
    public int gold;
    
    public boolean canMoveEntities = false;
    public boolean canBeMoved = true;
    public boolean canBeCarried = false;
    public Entity carryingEntity;
    
    /**
     * The rate (in ms) that hitpoints will regen on their own without potions.
     * Default is 1 hp every 20 seconds.
     */
    public long hpRegenRate = 20000;
    /** The amount of hp that an entity will heal each time they regen. */
    public int hpRegenAmount = 1;
    /** The point in time that hp last regenerated. */
    public long lastRegenUpdate = 0l;
    /** Distance in world pixels. */
    protected double maxRange = 50.0;
    
    public boolean invincible = false;
    
    public int strengthLevel = 0;
    public int hitpointsLevel = 0;
    public int magicLevel = 0;
    
    /**
     * An entity's melee damage is calculated from their base strength level.
     * Additional modifiers are added in later.
     */
    public int baseMeleeDamage;
    
    // TODO: Remove these
    // Stuff for keeping track of entity attacks
    public boolean attacking = false;
    public boolean recentlyAttacked = false;
    public long attackDrawStart;
    public long attackStart;
    public long timeUntilNextAttack = 300;
    public long recentlyAttackedTimeout = 3000l;
    
    // TODO: Remove these
    // Stuff for keeping track of when an entity was last hit
    public boolean healthChanged = false;
    public int lastHealthChangeAmount = 0;
    public boolean drawnHealthChange = false;
    public long healthChangedTime;
    public long healthChangedTimeout = 3000l;
    
    // TODO: Component-ize the health bar ?
    public EntityHealthBar healthBar;
    public final EntityFavorTracker favorTracker = new EntityFavorTracker(this);
    public final EntityFavorDecider favorDecider = new EntityFavorDecider(this);
    
    // TODO: What is this??
    public EList<IWindowObject> drawnObjects = EList.newList();
    
    public final MovementCollisionHelper collisionHelper;
    public final EntityPhysicsHandler physicsHandler;
    
    /**
     * I am.. SPEED
     * <p>
     * Measured in pixels per ms.
     */
    private double speed = (32 * 4.5) / 1000.0;
    
    public ActiveAbilityTracker abilityTracker;
    public EntitySpellbook spellbook;
    public EntityStats stats;
    
    /** Temp thing to test out stuff. */
    public Map<String, Double> activeEffectsTracker = new HashMap<>();
    
    public Item equipedWeapon;
    
    public boolean canRegenHealth = false;
    public boolean canRegenMana = false;
    public float magicRegenTime = 5000;
    public float healthRegenTime = 30000;
    public float magicRegenUpdate;
    public float healthRegenUpdate;
    public int magicRegenAmount = 1;
    public int healthRegenAmount = 1;
    
    //==============
    // Constructors
    //==============
    
    public Entity() { this(null); }
    public Entity(String nameIn) {
        super(nameIn);
        
        stats = new EntityStats(this);
        inventory = new EntityInventory(this, baseInventorySize);
        
        //determine initial next level
        xpNeeded = EntityLevel.getXPNeededForNextLevel(level + 1);
        healthBar = new EntityHealthBar(this);
        
        collisionHelper = new MovementCollisionHelper(this);
        physicsHandler = new EntityPhysicsHandler(this);
        abilityTracker = new ActiveAbilityTracker(this, 5);
        spellbook = new EntitySpellbook(this);
    }
    
    //==================
    // Back-end Methods
    //==================
    
    public void init(double posX, double posY, double widthIn, double heightIn) {
        startX = posX;
        startY = posY;
        endX = posX + widthIn;
        endY = posY + heightIn;
        midX = posX + (widthIn) / 2;
        midY = posY + (heightIn) / 2;
        width = widthIn;
        height = heightIn;
        collisionBox = new Dimension_d(startX, startY, endX, endY);
    }
    
    public void init(int posX, int posY, int widthIn, int heightIn) {
        startX = posX;
        startY = posY;
        endX = posX + widthIn;
        endY = posY + heightIn;
        midX = posX + (widthIn) / 2;
        midY = posY + (heightIn) / 2;
        width = widthIn;
        height = heightIn;
        collisionBox = new Dimension_d(startX, startY, endX, endY);
    }
    
    /**
     * Called from the world whenever an entity collides with another entity.
     */
    public void onEntityCollide(Entity collidingEntity) {}
    
    @Override
    public String toString() {
        if (name == null) return super.toString();
        return name;
    }
    
    //=========
    // Methods
    //=========
    
    protected void healthChanged(int amount) {
        healthChanged = true;
        lastHealthChangeAmount = amount;
        healthChangedTime = System.currentTimeMillis();
    }
    
    /**
     * Reduces health by amount. If result is less than or equal to 0, the
     * entity dies.
     */
    public void drainHealth(int amount) {
        if (invincible) return;
        
        if (activeEffectsTracker.containsKey("DEFENSE_MODIFIER")) {
            double reduce = Math.floor((double) amount * activeEffectsTracker.get("DEFENSE_MODIFIER"));
            System.out.println(reduce);
            amount += reduce;
        }
        
        health = ENumUtil.clamp(health - amount, 0, health);
        
        if (health == 0 && maxHealth > 0) kill();
        
        healthChanged(amount);
    }
    
    public void attackedBy(Entity ent, int amount) {
        drainHealth(amount);
        favorTracker.decreaseFavorWithEntity(ent, 25 * amount);
        magicRegenUpdate = 0;
        healthRegenUpdate = 0;
        
        boolean wasHigherDamage = amount > ent.getBaseMeleeDamage();
        
        // TODO this should eventually be a game setting
        boolean drawDamageSplash = true;
        if (drawDamageSplash && wasHigherDamage || ent != Envision.thePlayer) {
            String text = "" + amount;
            if (wasHigherDamage) text = amount + "!";
            var dmg = new FloatingTextEntity(midX, midY, 5, 5, text, 2500);
            dmg.setPixelPos(midX - dmg.width * 0.5, midY - dmg.height * 0.5);
            if (wasHigherDamage) dmg.setColor(EColors.mc_darkred);
            else dmg.setColor(EColors.lred);
            world.addEntity(dmg);
        }
        
        if (this.hasComponent(ComponentType.RENDERING)) {
            var renderingComponent = this.getComponent(ComponentType.RENDERING);
            if (renderingComponent instanceof EntityRenderer er) {
                if (wasHigherDamage) er.flashColor(EColors.mc_darkred, 500);
                else er.flashColor(EColors.lred, 200);
            }
        }
        
        if (isDead()) {
            ent.getStats().addKilled(1);
            world.removeEntity(this);
            ent.addXP(getExperienceRewardedOnKill());
            // TODO: move this to an 'onKilled' component
            if (ERandomUtil.roll(0, 0, 4)) ent.setGold(ent.getGold() + ERandomUtil.getRoll(1, 10));
        }
    }
    
    @Override
    public void onGameTick(float dt) {
        super.onGameTick(dt);
        
        favorTracker.updateFavorOverTime((long) dt);
        
        if (canRegenMana && mana < maxMana) magicRegenUpdate += dt;
        if (canRegenHealth && health < maxHealth) healthRegenUpdate += dt;
        
        if (canRegenMana && magicLevel >= 1) {
            if (magicRegenUpdate >= magicRegenTime) {
                magicRegenUpdate = 0;
                if (mana < maxMana) replenishMana(magicRegenAmount);
            }
        }
        
        if (canRegenHealth && healthRegenUpdate >= healthRegenTime) {
            healthRegenUpdate = 0;
            if (health < maxHealth) replenishHealth(healthRegenAmount);
        }
        
        if (healthChanged && (System.currentTimeMillis() - healthChangedTime >= healthChangedTimeout)) {
            healthChanged = false;
        }
        
        if (recentlyAttacked && (System.currentTimeMillis() - attackStart >= recentlyAttackedTimeout)) {
            recentlyAttacked = false;
        }
    }
    
    public void drainStamina(int amount) { stamina = ENumUtil.clamp(stamina - amount, 0, maxStamina); }
    
    /** Reduces mana by amount. */
    public void drainMana(int amount) {
        mana = ENumUtil.clamp(mana - amount, 0, maxMana);
    }
    /**
     * Heals the entity by the given amount. Note: does not exceed max health.
     */
    public void replenishHealth(int amount) {
        health = ENumUtil.clamp(health + amount, 0, maxHealth);
        var text = new FloatingTextEntity(midX, midY, 5, 5, amount, 1500);
        text.setPixelPos(midX - text.width * 0.5, midY - text.height * 0.5);
        text.setColor(EColors.green);
        world.addEntity(text);
        healthChanged(amount);
    }
    /**
     * Restores mana points by the given amount. Note: does not exceed max
     * mana.
     */
    public void replenishMana(int amount) {
        mana = ENumUtil.clamp(mana + amount, Integer.MIN_VALUE, maxMana);
//        var text = new FloatingTextEntity(worldX, worldY, width, height, amount, 1500);
//        text.setColor(EColors.blue);
//        world.addEntity(text);
        healthChanged(amount);
    }
    /**
     * Completely restores all hitpoints back to max health. Note: if max
     * health = 0, the entity will still be dead!
     */
    public void fullHeal() {
        int diff = maxHealth - health;
        health = maxHealth;
        var text = new FloatingTextEntity(startX, startY, 5, 5, diff, 1500);
        text.setPixelPos(midX - text.width * 0.5, midY - text.height * 0.5);
        text.setColor(EColors.green);
        world.addEntity(text);
        healthChanged(diff);
    }
    /** Completely restores all mana points back to max mana. */
    public void restoreMana() { mana = maxMana; }
    /** Reduces health to 0 effectively killing the entity. */
    public void kill() {
        int diff = health;
        health = 0;
        healthChanged(diff);
        if (this.hasComponent(ComponentType.ON_DEATH)) {
            OnDeathComponent c = getComponent(ComponentType.ON_DEATH);
            c.onDeath(null);
        }
    }
    
    public boolean giveItem(Item item) {
        if (item == null) return false;
        return getInventory().addItem(item);
    }
    
    public void dropItem(int index) {
        Item i = inventory.removeItemAtIndex(index);
        var cDims = getCollisionDims();
        world.dropItemOnGround(i, cDims.midX, cDims.midY);
    }
    
    public void move(Direction d) {
        switch (d) {
        case N:
            move(0, -1);
            break;
        case E:
            move(1, 0);
            break;
        case S:
            move(0, 1);
            break;
        case W:
            move(-1, 0);
            break;
        case NE:
            move(1, -1);
            break;
        case NW:
            move(-1, -1);
            break;
        case SE:
            move(1, 1);
            break;
        case SW:
            move(-1, 1);
            break;
        default:
            break;
        }
    }
    
    public void move(double x, double y) {
        //System.out.println("MOVING: " + x + " : " + y + " : " + Envision.getDeltaTime());
        collisionHelper.tryMove(x, y, Envision.getDeltaTime());
        
        // move the entity this one is carrying
        if (carryingEntity != null) {
            var e = carryingEntity;
            carryingEntity.startX = startX;
            carryingEntity.startY = startY;
            e.midX = e.startX + (e.width / 2);
            e.midY = e.startY + (e.height / 2);
            
            double valX = e.startX / world.getTileWidth();
            double valY = e.startY / world.getTileHeight();
            
            e.endX = e.startX + e.width;
            e.endY = e.startY + e.height;
            
            e.worldX = (int) valX;
            e.worldY = (int) valY;
        }
        
        if (!canMoveEntities) return;
        
        var cDims = this.getCollisionDims();
        
        final int len = world.getEntitiesInWorld().size();
        for (int i = 0; i < len; i++) {
            var e = world.getEntitiesInWorld().get(i);
            
            if (e == this) continue;
            if (!e.canBeMoved) continue;
            if (e instanceof Doodad) continue;
            
            final double dist = world.getDistance(this, e);
            if (dist < 15) {
                var ecDims = e.getCollisionDims();
                double dx = cDims.midX - ecDims.midX;
                double dy = cDims.midY - ecDims.midY;
                double mag = Math.sqrt(dx * dx + dy * dy);
                dx /= mag;
                dy /= mag;
//                System.out.println(cDims.midX + " : " + ecDims.midX + " | " + dx + " : " + dy + " | " + speed);
//                double kb = -KnockbackCalculator.calculateKnockbackForce(this, e, AttackType.MAGIC);
                
                dx *= -speed * Envision.getDeltaTime();
                dy *= -speed * Envision.getDeltaTime();
                e.getPhysicsHandler().applyImpulse(dx, dy);
            }
        }
    }
    
    public void movePixel(double x, double y) {
        collisionHelper.tryMovePixel(x, y);
        
        if (!canMoveEntities) return;
        
        var cDims = this.getCollisionDims();
        
        final int len = world.getEntitiesInWorld().size();
        for (int i = 0; i < len; i++) {
            var e = world.getEntitiesInWorld().get(i);
            
            if (e == this) continue;
            if (!e.canBeMoved) continue;
            if (e instanceof Doodad) continue;
            //System.out.println(e + " : " + e.getDimensions());
            
            final double dist = world.getDistance(this, e);
            if (dist < 15) {
                var ecDims = e.getCollisionDims();
                double dx = cDims.midX - ecDims.midX;
                double dy = cDims.midY - ecDims.midY;
                double mag = Math.sqrt(dx * dx + dy * dy);
                dx /= mag;
                dy /= mag;
//                System.out.println(cDims.midX + " : " + ecDims.midX + " | " + dx + " : " + dy + " | " + speed);
//                double kb = -KnockbackCalculator.calculateKnockbackForce(this, e, AttackType.MAGIC);
                
                dx *= -speed * Envision.getDeltaTime();
                dy *= -speed * Envision.getDeltaTime();
                e.getPhysicsHandler().applyImpulse(dx, dy);
            }
        }
    }
    
    public double distSquared(double x, double y) {
        System.out.println(midX + " : " + midY + " : " + x + " : " + y);
        return midX * x + midY * y;
    }
    
    /**
     * Returns true if this entity actually exists within a loaded world.
     * 
     * @return true if in loaded world
     */
    public boolean exists() {
        return world != null && world.isLoaded();
    }
    
    public void addXP(long xp) {
        experience += xp;
    }
    
    public void levelUp() {
        if (EntityLevel.checkLevelUp(level, experience)) {
            level++;
            xpNeeded = EntityLevel.getTotalXPNeeded(level + 1);
        }
    }
    
    /**
     * @return True if this entity has less than or equal to zero '0' health
     *         remaining.
     */
    public boolean isDead() { return health <= 0; }
    
    public boolean isNeutralWithPlayer() { return favorDecider.isNeutral(Envision.thePlayer); }
    public boolean isFriendsWithPlayer() { return favorDecider.isGoodFavor(Envision.thePlayer); }
    public boolean isGoodFriendsWithPlayer() { return favorDecider.isReallyGoodFavor(Envision.thePlayer); }
    public boolean isAnnoyedWithPlayer() { return favorDecider.isBadFavor(Envision.thePlayer); }
    public boolean isEnemiesWithPlayer() { return favorDecider.isReallyBadFavor(Envision.thePlayer); }
    public boolean isPositiveFavorWithPlayer() { return favorDecider.isPositiveFavor(Envision.thePlayer); }
    public boolean isNegativeFavorWithPlayer() { return favorDecider.isNegativeFavor(Envision.thePlayer); }
    
    public boolean isNeutralWith(Entity ent) { return favorDecider.isNeutral(ent); }
    public boolean isFriendsWith(Entity ent) { return favorDecider.isGoodFavor(ent); }
    public boolean isGoodFriendsWith(Entity ent) { return favorDecider.isReallyGoodFavor(ent); }
    public boolean isAnnoyedWith(Entity ent) { return favorDecider.isBadFavor(ent); }
    public boolean isEnemiesWith(Entity ent) { return favorDecider.isReallyBadFavor(ent); }
    public boolean isPositiveFavorWith(Entity ent) { return favorDecider.isPositiveFavor(ent); }
    public boolean isNegativeFavorWith(Entity ent) { return favorDecider.isNegativeFavor(ent); }
    
    //=========
    // Getters
    //=========
    
    public boolean isPassable() { return passable; }
    public boolean isNoClipping() { return allowNoClip; }
    public String getHeadText() { return headText; }
    
    public int getLevel() { return level; }
    public long getExperience() { return experience; }
    public long getXPNeeded() { return xpNeeded; }
    
    public int getMaxHealth() { return maxHealth; }
    public int getHealth() { return health; }
    public int getHitpointsLevel() { return hitpointsLevel; }
    
    public int getMaxMana() { return maxMana; }
    public int getMana() { return mana; }
    public int getMagicLevel() { return magicLevel; }
    
    public int getBaseMeleeDamage() {
        int damage = baseMeleeDamage;
        for (int i = 0; i < inventory.size(); i++) {
            var item = inventory.getItemAtIndex(i);
            if (item != null) damage += item.getDamageBonus();
        }
        return damage;
    }
    
    public int getStrengthLevel() { return strengthLevel; }
    public double getMaxRange() { return maxRange; }
    
    public int getBaseInventorySize() { return baseInventorySize; }
    public EntityInventory getInventory() { return inventory; }
    public int getGold() { return gold; }
    public boolean isInvincible() { return invincible; }
    public long getExperienceRewardedOnKill() { return experienceRewarded; }
    
    public MovementCollisionHelper getCollisionHelper() { return collisionHelper; }
    public EntityPhysicsHandler getPhysicsHandler() { return physicsHandler; }
    public ActiveAbilityTracker getAbilityTracker() { return abilityTracker; }
    public EntitySpellbook getSpellbook() { return spellbook; }
    public EntityFavorTracker getFavorTracker() { return favorTracker; }
    public EntityFavorDecider getFavorDecider() { return favorDecider; }
    public EntityStats getStats() { return stats; }
    
    //=========
    // Setters
    //=========
    
    public Entity setNoClipAllowed(boolean val) { allowNoClip = val; return this; }
    public Entity setPassable(boolean val) { passable = val; return this; }
    
    public Entity setHeadText(Object... textIn) {
        var sb = new EStringBuilder();
        sb.a(textIn);
        return setHeadText(sb.toString());
    }
    public Entity setHeadText(String textIn) { headText = textIn; return this; }
    
    public Entity setMaxHealth(int maxHealthIn) { maxHealth = maxHealthIn; return this; }
    public Entity setHealth(int healthIn) {
        int diff = healthIn - health;
        health = healthIn;
        healthChanged(diff);
        return this;
    }
    public void setHitpointsLevel(int levelIn) {
        hitpointsLevel = levelIn;
        maxHealth = EntityLevel.calculateMaxHealth(hitpointsLevel);
        fullHeal();
    }
    
    public Entity setMaxMana(int maxManaIn) { maxMana = maxManaIn; return this; }
    public Entity setMana(int manaIn) { mana = manaIn; return this; }
    public void setMagicLevel(int levelIn) {
        magicLevel = levelIn;
        maxMana = EntityLevel.calculateMaxMana(magicLevel);
        mana = ENumUtil.clamp(mana, 0, maxMana);
        
        if (magicLevel >= 0) {
            canRegenMana = true;
        }
        
        // TODO remove this
        if (levelIn == 1) {
            this.spellbook.learnAbility(Abilities.selfHeal);
            this.abilityTracker.addAbility(Abilities.selfHeal);
            this.mana = this.maxMana;
        }
        else if (levelIn == 5) {
            this.spellbook.learnAbility(Abilities.fireball);
            this.abilityTracker.addAbility(Abilities.fireball);
        }
    }
    
    public void setMaxRange(double rangeIn) { maxRange = ENumUtil.clamp(rangeIn, 0.0, Double.MAX_VALUE); }
    
    /**
     * Sets this entity's melee damage to the exact amount specified.
     * <p>
     * Note: This completely disregards the entity's strength level.
     * 
     * @param damageIn
     */
    public void setBaseMeleeDamage(int damageIn) { baseMeleeDamage = damageIn; }
    
    public void setStrengthLevel(int levelIn) {
        strengthLevel = levelIn;
        baseMeleeDamage = 1 + EntityLevel.calculateBaseDamage(levelIn);
    }
    
    public void setGold(int amountIn) { gold = amountIn; }
    
    public void setExperience(long expIn) {
        experience = expIn;
        level = EntityLevel.getLevelFromXP(experience);
        xpNeeded = EntityLevel.getTotalXPNeeded(level);
    }
    
    public void setLevel(int levelIn) {
        level = levelIn;
        experience = EntityLevel.getTotalXPNeeded(level);
        xpNeeded = EntityLevel.getTotalXPNeeded(level);
    }
    
    public Entity setDead(boolean val) {
        if (val) kill();
        else health = maxHealth; //if maxHealth is 0, the entity is still dead!
        return this;
    }
    
    public void setInvincible(boolean val) { invincible = val; }
    public void setExperienceRewardedOnKill(long amount) { experienceRewarded = amount; }
    
    /** Returns true if this entity had enough mana to use the given spell. */
    public boolean manaCheck(int spellCost) {
        if (mana >= spellCost) { return true; }
        return false;
    }
    
    public double getSpeed() { return speed; }
    
    /**
     * Sets entity movement speed in terms of pixels traveled per second.
     * 
     * @param speed pixels per second
     */
    public void setSpeed(double speed) { this.speed = speed / 1000.0; }
    
    public void carryEntity(Entity entityToCarry) {
        if (entityToCarry == null || !entityToCarry.canBeCarried) return;
        carryingEntity = entityToCarry;
    }
    
    public void dropEntity() {
        if (carryingEntity == null) return;
        carryingEntity = null;
    }
    
    public void throwCarriedEntity(Vec3d direction, double force) {
        
    }
    
    public void equipItem(Item item) {
        if (!inventory.containsItemType(item)) return;
        
        equipedWeapon = item;
    }
    
}
