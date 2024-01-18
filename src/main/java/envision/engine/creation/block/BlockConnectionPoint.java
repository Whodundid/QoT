package envision.engine.creation.block;

import envision.engine.windows.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

/**
 * A connection point for a creator block -- can be either an input or
 * output point.
 * 
 * @param <T> The datatype of this point's value
 * 
 * @author Hunter
 */
public class BlockConnectionPoint<T> extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected final CreatorBlock parentBlock;
    protected String pointName;
    protected boolean isInput;
    protected int pointColor = EColors.seafoam.intVal;
    protected PointLocation pointLocation = PointLocation.LEFT;
    protected int maxPointConnections = Integer.MAX_VALUE;
    protected EList<BlockConnectionPoint> connections = EList.newList();
    protected T value;
    
    //==============
    // Constructors
    //==============
    
    public BlockConnectionPoint(CreatorBlock parentIn) {
        this(parentIn, "Connection Point");
    }
    
    public BlockConnectionPoint(CreatorBlock parentIn, String nameIn) {
        parentBlock = parentIn;
        pointName = nameIn;
        
        setObjectName(nameIn);
        setSize(10, 10);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return (pointName != null) ? pointName : super.toString();
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        if (!isInput) {
            synchronized (connections) {
                for (BlockConnectionPoint<?> point : connections) {
                    drawLine(midX, midY, point.midX, point.midY, 3, pointColor);
                }
            }
        }
        
        if (this == CreatorBlock.firstPoint) drawRect(EColors.yellow);
        else drawRect(pointColor);
        
        if (isMouseInside()) drawRect(EColors.lgreen, 2);
        else drawRect(EColors.vdgray, 2);
        
        if (isMouseInside()) {
            drawString(connections, startX, endY + 5);
        }
    }
    
    @Override
    public void mousePressed(int mX, int mY, int button) {
        if (CreatorBlock.firstPoint == null) {
            CreatorBlock.firstPoint = this;
        }
        else if (CreatorBlock.firstPoint.isInput && !this.isInput) {
            if (connections.contains(CreatorBlock.firstPoint)) removeConnection(CreatorBlock.firstPoint);
            else addConnection(CreatorBlock.firstPoint);
            CreatorBlock.firstPoint = null;
        }
        else if (!CreatorBlock.firstPoint.isInput && this.isInput) {
            if (connections.contains(CreatorBlock.firstPoint)) removeConnection(CreatorBlock.firstPoint);
            else CreatorBlock.firstPoint.addConnection(this);
            CreatorBlock.firstPoint = null;
        }
        
        super.mousePressed(mX, mY, button);
    }
    
    //=========
    // Methods
    //=========
    
    public void addConnection(BlockConnectionPoint point) {
        if (point == null) return;
        if (point == this) return;
        if (connections.contains(point)) return;
        if (maxPointConnections == 0) return;
        
        connections.add(point);
        point.addConnection(this);
        if (isInput) {
            setValue((T) point.value);
            if (parentBlock instanceof FunctionBlock b) b.evaluate();
        }
        
        if (connections.size() > maxPointConnections) {
            var p = connections.removeFirst();
            p.removeConnection(this);
        }
    }
    
    public void removeConnection(BlockConnectionPoint<?> point) {
        connections.remove(point);
        point.connections.remove(this);
        if (isInput) {
            setValue((T) point.value);
            if (parentBlock instanceof FunctionBlock b) b.evaluate();
        }
    }
    
    public void clearConnections() {
        connections.clear();
    }
    
    //=========
    // Getters
    //=========
    
    public CreatorBlock getParentBlock() { return parentBlock; }
    public String getPointName() { return pointName; }
    public boolean isInput() { return isInput; }
    public int getPointColor() { return pointColor; }
    public PointLocation getPointLocation() { return pointLocation; }
    public int getMaxPointConnections() { return maxPointConnections; }
    public EList<BlockConnectionPoint> getConnections() { return connections; }
    public T getValue() { return value; }
    
    //=========
    // Setters
    //=========
    
    public void setPointName(String pointName) { this.pointName = pointName; }
    public void setInput(boolean val) { this.isInput = val; }
    public BlockConnectionPoint<T> setPointColor(EColors color) { this.pointColor = color.intVal; return this; }
    public BlockConnectionPoint<T> setPointColor(int color) { this.pointColor = color; return this; }
    public BlockConnectionPoint<T> setPointLocation(PointLocation locationIn) { pointLocation = locationIn; return this; }
    
    public BlockConnectionPoint<T> setValue(T value) {
        this.value = value;
        
        // REALLY ROUGH AND DIRTY WAY TO HANDLE THIS!
        for (var p : connections) {
            if (!p.isInput) continue;
            new Thread(() -> {
                p.setValue(value);
                if (p.getParentBlock() instanceof FunctionBlock fb) {
                    fb.evaluate();
                }
            }).start();
        }
        
        return this;
    }
    
    /**
     * Sets this point's maximum number of connections.
     * <p>
     * Clamps value between [0, MAX_INT].
     * 
     * @param maxPointConnections The maximum number of connections this point
     *                            can have
     */
    public BlockConnectionPoint<T> setMaxPointConnections(int maxPointConnections) {
        this.maxPointConnections = ENumUtil.clamp(maxPointConnections, 0, Integer.MAX_VALUE);
        return this;
    }
    
}
