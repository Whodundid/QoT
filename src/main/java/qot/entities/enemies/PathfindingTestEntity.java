package qot.entities.enemies;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;
import qot.entities.EntityPathfinder;

public class PathfindingTestEntity extends BasicRenderedEntity {

    private EntityPathfinder pathfinder;
    
    public PathfindingTestEntity() { this(0, 0); }
    public PathfindingTestEntity(int posX, int posY) {
        super("Pathfinder");
        
        setMaxHealth(10);
        setHealth(10);
        setSpeed(32.0 * 5.0);
        
        init(posX, posY, 32, 32);
        sprite = new Sprite(EntityTextures.whobro_blink2);
        
        setCollisionBox(startX + 4, startY + 4, endX - 4, endY - 4);
        
        pathfinder = new EntityPathfinder();
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.PATHFINDER_TEST.ID;
    }
    
}
