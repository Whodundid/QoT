package qot.entities.enemies;

import envision.debug.DebugSettings;
import envision.engine.loader.built.game.Sprite;
import envision.game.entities.BasicRenderedEntity;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.Cell;
import qot.entities.EntityList;
import qot.entities.EntityPathfinder;

public class PathfindingTestEntity extends BasicRenderedEntity {
    
    //========
    // Fields
    //========
    
    private EntityPathfinder pathfinder;
    private EList<Cell> pathCells;
    private int curCellIndex;
    private WorldTile lastTile;
    
    private boolean pathMade = false;
    private boolean hasNextCell = false;    
    //==============
    // Constructors
    //==============
    
    public PathfindingTestEntity() { this(0, 0); }
    public PathfindingTestEntity(int posX, int posY) {
        super("Pathfinder");
        
        setMaxHealth(10);
        setHealth(10);
        setSpeed(200);
        
        init(posX, posY, 32, 32);
        sprite = new Sprite(EntityTextures.whobro_blink2);
        
        setCollisionBox(startX + 4, startY + 4, endX - 4, endY - 4);
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        WorldTile tile = DebugSettings.lastClickedTile;
        if (tile == null || tile != lastTile) pathMade = false;
        
        if (!pathMade) createPath();
        if (!pathMade) return;
        
        moveToNextCell();
    }
    
    public void createPath() {
        pathfinder = new EntityPathfinder(this);
        
        WorldTile tile = DebugSettings.lastClickedTile;
        if (tile == null) return;
        
        //var cdims = this.getCollisionDims();
        lastTile = tile;
        pathCells = pathfinder.findPath(tile, false, -1).reversed();
        
//        for (var c : pathCells) {
//            System.out.println(c);
//        }
        
        hasNextCell = pathCells.isNotEmpty();
        curCellIndex = 0;
//        if (hasNextCell) {
//            var cell = pathCells.get(curCellIndex);
//            world.setTileAt(NatureTiles.redSand.copy(), cell.x, cell.y);
//        }
        pathMade = pathCells.isNotEmpty();
    }
    
    private void moveToNextCell() {
        if (!hasNextCell) return;
        
        final Cell curCell = pathCells.get(curCellIndex);
        if (curCell == null) {
            System.out.println("NO PATH CELL AT INDEX: " + curCellIndex);
            return;
        }
        
        final int tw = world.getTileWidth();
        final int th = world.getTileHeight();

        var sb = new EStringBuilder("MOVING TO CELL:");
        sb.a(" i=", curCellIndex);
        sb.a(" c[" + curCell.x * tw + ", " + curCell.y * th + "]");
        sb.a(" p[" + startX + ", " + startY + "]");
        
        
        boolean lessX, lessY, moreX, moreY;
        lessX = startX < curCell.x * tw;
        lessY = startY < curCell.y * th;
        moreX = startX > curCell.x * tw;
        moreY = startY > curCell.y * th;
        
        boolean goodX = Math.abs(startX - (curCell.x * tw)) < 1.00;
        boolean goodY = Math.abs(startY - (curCell.y * th)) < 1.00;
        
        boolean stillNeedsX = !goodX && lessX || moreX;
        boolean stillNeedsY = !goodY && lessY || moreY;
        
        double xDir = 0.0, yDir = 0.0;
        xDir = (lessX) ? 1 : -1;
        yDir = (lessY) ? 1 : -1;
        
        sb.a(" x=", stillNeedsX, " y=", stillNeedsY);
        sb.a(" dir[", xDir, ", ", yDir, "]");
        sb.a(" mX=", moreX, " lX=", lessX);
        sb.a(" mY=", moreY, " lY=", lessY);
        
        if (stillNeedsX) move(xDir, 0.0);
        if (stillNeedsY) move(0.0, yDir);
        //if (stillNeedsX || stillNeedsY) move(xDir, yDir);
        
        double checkX = startX, checkY = startY;
        //if (moreX) checkX = (int) (worldX + (width / world.getTileWidth()));
        //else checkX = (int) (worldX - (width / world.getTileWidth()));
        //if (moreY) checkY = (int) (worldY + (height / world.getTileHeight()));
        
        sb.a(" ch[", checkX, ", ", checkY, "]");
        
        boolean withinX = (Math.abs(checkX - curCell.x * tw) < 1.00);
        boolean withinY = (Math.abs(checkY - curCell.y * th) < 1.00);
        
        sb.a(" w[", checkX - curCell.x * tw, ", ", checkY - curCell.y * th, "]");
        //System.out.println(sb);
        
        if (withinX && withinY) {
            curCellIndex++;
            hasNextCell = curCellIndex < pathCells.size();
//            if (hasNextCell) {
//                var cell = pathCells.get(curCellIndex);
//                world.setTileAt(NatureTiles.redSand.copy(), cell.x, cell.y);
//            }
        }
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.PATHFINDER_TEST.ID;
    }
    
}
