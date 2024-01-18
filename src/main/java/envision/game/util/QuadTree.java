package envision.game.util;

import envision.game.GameObject;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;

/**
 * https://code.tutsplus.com/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374t
 * 
 * @author Steven Lambert
 * @author Hunter Bragg
 */
public class QuadTree {
    
    private int maxObjects;
    private int maxLevels;
    
    private int level;
    private EList<GameObject> objects;
    private Dimension_d bounds;
    private QuadTree[] nodes;
    
    public QuadTree(int level, Dimension_d boundsIn) {
        this.level = level;
        objects = EList.newList();
        bounds = boundsIn;
        nodes = new QuadTree[4];
    }
    
    public void clear() {
        objects.clear();
        
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    
    private void split() {
        double w = bounds.halfWidth;
        double h = bounds.halfHeight;
        double x = bounds.startX;
        double y = bounds.startY;
        int l = level + 1;
        
        nodes[0] = new QuadTree(l, new Dimension_d(x + w, y    , w, h));
        nodes[1] = new QuadTree(l, new Dimension_d(x    , y    , w, h));
        nodes[2] = new QuadTree(l, new Dimension_d(x    , y + h, w, h));
        nodes[3] = new QuadTree(l, new Dimension_d(x + w, y + h, w, h));
    }
    
    private int getIndex(GameObject object) {
        int index = -1;
        double midX = bounds.midX_d();
        double midY = bounds.midY_d();
        
        boolean top = object.startY < midY && object.endY < midY;
        boolean bot = object.startY > midY;
        
        if (object.startX < midX && object.endX < midX) {
            if (top) index = 1;
            else if (bot) index = 2;
        }
        else if (object.startX > midX) {
            if (top) index = 0;
            if (bot) index = 3;
        }
        
        return index;
    }
    
    public void insert(GameObject object) {
        if (nodes[0] != null) {
            int index = getIndex(object);
            
            if (index != -1) {
                nodes[index].insert(object);
                return;
            }
        }
        
        objects.add(object);
        
        if (objects.size() > maxObjects && level < maxLevels) {
            if (nodes[0] == null) split();
            
            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) nodes[index].insert(objects.remove(i));
                else i++;
            }
        }
    }
    
    public EList<GameObject> getObjectsNearObject(GameObject object) {
        int index = getIndex(object);
        if (index != -1 && nodes[0] != null) {
            nodes[index].getObjectsNearObject(object);
        }
        
        return objects.copy();
    }
    
}
