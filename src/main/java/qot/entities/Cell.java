package qot.entities;

import envision.game.world.worldTiles.WorldTile;
import eutil.strings.EStringBuilder;

public class Cell {
    
    public Cell parent;
    public int x;
    public int y;
    public double cost;
    /** distance from start (greedy) */
    public double g;
    /** distance to goal */
    public double h;
    /** heuristic */
    public double f;
    public boolean reachable;
    public double true_f;
    
    public Cell() {}
    
    public Cell(WorldTile tile) {
        x = tile.worldX;
        y = tile.worldY;
        
        reachable = !tile.blocksMovement();
        cost = 1;
    }
    
    public Cell(int x, int y, boolean reachable, double cost) {
        this.x = x;
        this.y = y;
        this.reachable = reachable;
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        var sb = new EStringBuilder();
        sb.a("Cell(", x, ", ", y, "): [");
        sb.a("cost=", cost, ", ");
        sb.a("g=", g, ", ");
        sb.a("h=", h, ", ");
        sb.a("f=", f, "]");
        return sb.toString();
    }
    
    public boolean compare(Cell toCompareTo) {
        if (toCompareTo == null) return false;
        return this.x == toCompareTo.x && this.y == toCompareTo.y;
    }
    
    public void reset() {
        g = 0;
        h = 0;
        f = 0;
    }
    
    public boolean greaterThan(Cell c) {
        return cost > c.cost;
    }
    
    public boolean lessThan(Cell c) {
        return cost < c.cost;
    }
    
}
