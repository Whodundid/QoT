package qot.entities.house;

import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.BasicRenderedEntity;
import envision.game.entities.Entity;
import envision.game.items.Item;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class Chest extends BasicRenderedEntity {

    //========
    // Fields
    //========
    
    private Item item;
    
    //==============
    // Constructors
    //==============
    
    public Chest() { this(0, 0, null); }
    public Chest(int x, int y, Item item) {
        super("Chest");
        this.item = item;
        this.item = Items.random();
        
        init(x, y, 32, 32);
        setMaxHealth(5);
        setHealth(5);
        
        setCollisionBox(startX + 6, startY + 14, endX - 6, endY - 1);
        setExperienceRewardedOnKill(1);
        
        setSprite(HouseTextures.chest_sheet.getSprite(0));
        
        // item on death
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, this.item);
        itemOnDeath.setChance(1);
        
        addComponent(itemOnDeath);
        
        this.canBeMoved = false;
        this.canBeCarried = true;
        this.canMoveEntities = false;
    }
    
    @Override
    public void attackedBy(Entity ent, int amount) {
        super.attackedBy(ent, amount);
        
        setInvincible(true);
        setSprite(HouseTextures.chest_sheet.getSprite(1));
        
        world.dropItemOnGround(item, midX, midY + 16);
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.CHEST.ID;
    }
    
    public static Chest randomDropChest() {
        return new Chest(0, 0, Items.random());
    }
    
}