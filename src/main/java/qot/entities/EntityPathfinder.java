package qot.entities;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import envision.game.entities.Entity;
import envision.game.world.IGameWorld;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.ExpandableGrid;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class EntityPathfinder {
    
    // https://gitlab.com/rainlash/lt-maker/-/blob/master/app/engine/pathfinding/pathfinding.py?ref_type=heads
    
    protected Entity theEntity;
    protected IGameWorld theWorld;
    
    protected ExpandableGrid<Cell> cellGrid;
    protected Cell startCell, endCell;
    protected final EList<Point2i> pathNodes = EList.newList();
    protected boolean isPossible;
    protected boolean isFindingPath;
    protected boolean pathFound;
    
    // comparator to find the smallest 'heuristic' between two cells
    protected PriorityQueue<Cell> queue = new PriorityQueue<>((a, b) -> (int) (a.f - b.f));
    
    protected Set<Cell> alreadyVisited = new HashSet<>();
    protected Set<Cell> cellsAdjToEnd = new HashSet<>();
    
    public boolean isPossible() { return isPossible; }
    public boolean isFindingPath() { return isFindingPath; }
    public boolean isPathFound() { return pathFound; }
    
    public EntityPathfinder(Entity entityIn) {
        theEntity = entityIn;
        theWorld = theEntity.world;
    }

    public EList<Cell> findPath(WorldTile destination, boolean adj_good_enough, double distance_limit) {
        setupCellGrid();
        
        startCell = cellGrid.get(theEntity.worldX, theEntity.worldY);
        endCell = cellGrid.get(destination.worldX, destination.worldY);
        
        queue.clear();
        alreadyVisited.clear();
        cellsAdjToEnd.clear();
        
        var adjCells = getAdjacentCells(endCell);
        adjCells.forEach(cellsAdjToEnd::add);
        
        return process(adj_good_enough, distance_limit);
    }
    
    protected void setupCellGrid() {
        int width = theWorld.getWidth();
        int height = theWorld.getHeight();
        cellGrid = new ExpandableGrid<>(width, height);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell c = new Cell(theWorld.getTileAt(x, y));
                cellGrid.set(c, x, y);
            }
        }
    }
    
    protected double getHeuristic(Cell cell) {
        // h: the approximate distance between this cell and goal cell
        return ENumUtil.distance(cell.x, cell.y, endCell.x, endCell.y);
        
        // This is for Manhattan distance calculations (grid based)
        // get main heuristic
        //double dx1 = cell.x - endCell.x;
        //double dy1 = cell.y - endCell.y;
        // double h = abs(dx1) + abs(dy1);
        // are we going in direction of goal?
        // slight nudge in direction that lies along path from start to end
        //double dx2 = startCell.x - endCell.x;
        //double dy2 = startCell.y - endCell.y;
        //double cross = abs(dx1 * dy2 - dx2 * dy1);
        // encourage going diagonally
        //return h + cross * 0.001;
    }
    
//    protected double getSimpleHeuristic(Cell cell) {
//        // get main heuristic
//        double dx1 = cell.x - endCell.x;
//        double dy1 = cell.y - endCell.y;
//        // h: the approximate distance between this cell and goal cell
//        double h = abs(dx1) + abs(dy1);
//        return h;
//    }
    
    protected EList<Cell> getAdjacentCells(Cell cell) {
        return getManhattanAdjacent(cell);
    }
    
    protected EList<Cell> getManhattanAdjacent(Cell cell) {
        EList<Cell> cells = EList.newList();
        
        final int x = cell.x;
        final int y = cell.y;
        
        // north
        if (y > 0) cells.add(cellGrid.get(x, y - 1));
        // east
        if (x < theWorld.getWidth() - 1) cells.add(cellGrid.get(x + 1, y));
        // south
        if (y < theWorld.getHeight() - 1) cells.add(cellGrid.get(x, y + 1));
        // west
        if (x > 0) cells.add(cellGrid.get(x - 1, y));
        
        return cells;
    }
    
    protected void updateCell(Cell adj, Cell cell) {
        // h is approximate distance between this cell and the goal
        // g is true distance between this cell and the starting position
        // f is simply them added together
        adj.g = cell.g + adj.cost;
        adj.h = getHeuristic(adj);
        adj.parent = cell;
        // h is favoring the end
        // g is favoring the start
        adj.f = adj.h + adj.g;
        
        // heuristic is the distance from the adj cell that "you're" on to the end
        
        // only for grid based movement:
        // adj.true_f = getSimpleHeuristic(adj) + adj.g;
    }
    
    protected EList<Cell> returnPath(Cell currentCell) {
        EList<Cell> path = EList.newList();
        
        // this an 'end' to 'start' path
        Cell c = currentCell;
        while (c != null) {
            path.add(c);
            c = c.parent;
        }
        
        return path;
    }
    
    protected EList<Cell> process(boolean adj_good_enough, double distance_limit) {
        // push starting cell onto the queue
        queue.add(startCell);
        
        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();
            
            // make sure we don't process the cell twice
            alreadyVisited.add(currentCell);
            
            // If this cell is beyond the distance limit, just return None
            // Uses f, not g, because g will cut off if first greedy path fails
            // f only cuts off if all cells are bad
            if (distance_limit > 0 && currentCell.f > distance_limit) {
                // if we don't want to even attempt moving, return an empty list
                return EList.newList();
            }
            
            // if we've found the end, then return the path
            if (currentCell.compare(endCell) || (adj_good_enough && cellsAdjToEnd.contains(currentCell))) {
                return returnPath(currentCell);
            }
            
            // get the adjacent cells for the current cell
            var adj_cells = getAdjacentCells(currentCell);

            for (var adj : adj_cells) {
                // don't care if not reachable
                if (!adj.reachable) continue;
                // don't care if we've already seen it
                if (alreadyVisited.contains(adj)) continue;
                
                // if adj cell is in queue
                if (queue.contains(adj)) {
                    // check if current path is better than the one previously found for this adj cell
                    if (adj.g > (currentCell.g + adj.cost)) {
                        updateCell(adj, currentCell);
                        queue.add(adj);
                    }
                }
                // otherwise, this is a new cell to process
                else {
                    updateCell(adj, currentCell);
                    queue.add(adj);
                }
            }
        }
        
        return EList.newList();
    }
    
}
