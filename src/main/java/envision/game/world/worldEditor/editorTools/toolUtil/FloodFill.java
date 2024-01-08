package envision.game.world.worldEditor.editorTools.toolUtil;


import envision.game.world.IGameWorld;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class FloodFill {
	
	private FloodFill() {}
	
	private static final int[] row = { -1, -1, -1,  0, 0,  1, 1, 1 };
    private static final int[] col = { -1,  0,  1, -1, 1, -1, 0, 1 };
	
	public static void floodFillReplace(IGameWorld world, WorldTile tileToFind, int sX, int sY, WorldTile replacement) {
		
	    EList<Point> tilesToReplace = EList.newList();
	    EList<Point> unexploredStack = EList.newList();
	    
	    // find all matching adjacent tiles
	    findSimilar(new Point(sX, sY), tileToFind, world, tilesToReplace, unexploredStack);
	    
	    for (Point p : tilesToReplace) {
	        WorldTile t = replacement.copy();
	        t.randomizeValues();
	        world.setTileAt(t, p.x, p.y);
	    }
	}
	
	private static void findSimilar(Point startingPoint, WorldTile tileToSearchFor,
	    IGameWorld world, EList<Point> tilesToReplace, EList<Point> unexploredStack)
	{
	    unexploredStack.add(startingPoint);
	    final int tileID = (tileToSearchFor != null) ? tileToSearchFor.getID() : -1;
	    
	    while (unexploredStack.isNotEmpty()) {
	        Point pos = unexploredStack.pop();
	        final int x = pos.x;
	        final int y = pos.y;
	        
	        if (tilesToReplace.contains(pos)) continue;
	        if (x < 0 || x >= world.getWidth()) continue;
	        if (y < 0 || y >= world.getHeight()) continue;
	        
	        WorldTile tile = world.getTileAt(x, y);
	        if (tile == null && tileToSearchFor != null) continue;
	        
	        int id = tile.getID();
	        if (id != tileID) continue;
	        
	        tilesToReplace.add(new Point(x, y));
	        unexploredStack.add(new Point(x + 1, y));
	        unexploredStack.add(new Point(x - 1, y));
	        unexploredStack.add(new Point(x, y + 1));
	        unexploredStack.add(new Point(x, y - 1));
	    }
	}
	
	private static class Point {
	    final int x, y;
	    public Point(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	    @Override
	    public boolean equals(Object o) {
	        if (!(o instanceof Point)) return false;
	        Point p = (Point) o;
	        return p.x == x && p.y == y;
	    }
	}
	
}
