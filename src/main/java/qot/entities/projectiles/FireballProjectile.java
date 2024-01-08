package qot.entities.projectiles;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Projectile;
import eutil.EUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.world_tiles.GlobalTileList;
import qot.world_tiles.TileIDs;
import qot.world_tiles.categories.NatureTiles;

public class FireballProjectile extends Projectile {

    public FireballProjectile() {
        super("Fireball");
        
        // 1.5 seconds
        this.maxLifeSpan = 400.0f;
        this.sprite = new Sprite(EntityTextures.fireBall_projectile);
        
        init(0, 0, 32, 32);
        sprite = new Sprite(EntityTextures.fireBall_projectile);
        setCollisionBox(startX - 8, startY - 8, endX + 8, endY + 8);
        
        setSpeed(1000);
    }
    
    @Override
    public void killProjectile() {
        super.killProjectile();
        
        final var cDims = this.getCollisionDims();
        int wx = (int) (cDims.midX / world.getTileWidth());
        int wy = (int) (cDims.midY / world.getTileHeight());
        
        if (wx >= 0 && wx < world.getWidth() && wx >= 0 && wx < world.getHeight()) {
            damageGround(wx, wy, 2);
            damageGround(wx, wy - 1);
            damageGround(wx - 1, wy);
            damageGround(wx, wy + 1);
            damageGround(wx + 1, wy);
        }
    }
    
    @Override
    public int getInternalSaveID() {
        return 0;
    }
    
    private void damageGround(int x, int y) { damageGround(x, y, 1); }
    private void damageGround(int x, int y, int amount) {
        if (!(x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight())) return;
        
        var t = world.getTileAt(x, y);
        //if (t == null || t.isWall()) return;
        if (t == null) return;
        
        var toSet = NatureTiles.dryPlainsGrass;
        final int id = t.getID();
        
        final int dam1 = TileIDs.DRY_PLAINS_GRASS.tileID;
        final int dam2 = TileIDs.DRY_CRACKED_DIRT.tileID;
        final int dam3 = TileIDs.CRACKED_DIRT.tileID;
        final int dam4 = TileIDs.ROCKY_DIRT.tileID;
        final int dam5 = TileIDs.DARK_DUNG_FLOOR.tileID;
        
        int curID = EUtil.findMatch(id, dam1, dam2, dam3, dam4, dam5);
        
        // transform into workable range
        curID = switch (TileIDs.getIdFrom(curID)) {
        case DRY_PLAINS_GRASS -> 0;
        case DRY_CRACKED_DIRT -> 1;
        case CRACKED_DIRT -> 2;
        case ROCKY_DIRT -> 3;
        case DARK_DUNG_FLOOR -> 4;
        default -> -1;
        };
        
        curID += amount;
        
        // transform back into normal tile ids
        int idToSet = switch (curID) {
        case 0 -> TileIDs.DRY_PLAINS_GRASS.tileID;
        case 1 -> TileIDs.DRY_CRACKED_DIRT.tileID;
        case 2 -> TileIDs.CRACKED_DIRT.tileID;
        case 3 -> TileIDs.ROCKY_DIRT.tileID;
        default -> TileIDs.DARK_DUNG_FLOOR.tileID;
        };
        
        toSet = GlobalTileList.getTileFromID(idToSet);
        
        if (toSet != null) world.setTileAt(toSet.copy(), x, y);
    }
    
}
