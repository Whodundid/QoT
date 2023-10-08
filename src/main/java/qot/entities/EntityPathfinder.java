package qot.entities;

import static java.lang.Math.*;

import envision.game.entities.Entity;
import envision.game.world.IGameWorld;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;

public class EntityPathfinder {
    
    protected Entity theEntity;
    protected IGameWorld theWorld;
    
    protected final Point2i start_cell = new Point2i();
    protected final Point2i end_cell = new Point2i();
    protected final EList<Point2i> pathNodes = EList.newList();
    protected boolean isPossible;
    protected boolean isFindingPath;
    protected boolean pathFound;
    
    public boolean isPossible() { return isPossible; }
    public boolean isFindingPath() { return isFindingPath; }
    public boolean isPathFound() { return pathFound; }
    
    protected void findPath() {
        
    }
    
    protected double getHeuristic(Point2i cell) {
        // get main heuristic
        double dx1 = cell.x - end_cell.x;
        double dy1 = cell.y - end_cell.y;
        // h: the approximate distance between this cell and goal cell
        double h = abs(dx1) + abs(dy1);
        // are we going in direction of goal?
        // slight nudge in direction that lies along path from start to end
        double dx2 = start_cell.x - end_cell.x;
        double dy2 = start_cell.y - end_cell.y;
        double cross = abs(dx1 * dy2 - dx2 * dy1);
        return h + cross * 0.001;
    }
    
    protected double getSimpleHeuristic(Point2i cell) {
        // get main heuristic
        double dx1 = cell.x - end_cell.x;
        double dy1 = cell.y - end_cell.y;
        // h: the approximate distance between this cell and goal cell
        double h = abs(dx1) + abs(dy1);
        return h;
    }
    
}
