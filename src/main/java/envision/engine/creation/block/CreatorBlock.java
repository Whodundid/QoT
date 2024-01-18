package envision.engine.creation.block;

import java.util.HashMap;
import java.util.Map;

import envision.engine.windows.windowTypes.WindowParent;
import eutil.datatypes.util.EList;

public class CreatorBlock extends WindowParent {
    
    //========
    // Fields
    //========
    
    /** The last clicked connection point on any block. SHOULD PROBABLY BE IN A MANAGER! */
    public static BlockConnectionPoint<?> firstPoint;
    
    protected String blockName;
    protected final EList<BlockConnectionPoint<?>> inputPoints = EList.newList();
    protected final EList<BlockConnectionPoint<?>> outputPoints = EList.newList();
    
    //==============
    // Constructors
    //==============
    
    public CreatorBlock() {}
    public CreatorBlock(String blockNameIn) {
        blockName = blockNameIn;
        
        setObjectName(blockName);
        setResizeable(false);
        setMaximizable(false);
        setMinimizable(false);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        defaultHeader();
    }
    
    @Override
    public void onPostInit() {
        positionPoints();
    }
    
    @Override
    public void postReInit() {
        for (var input : inputPoints) addObject(input);
        for (var output : outputPoints) addObject(output);
        positionPoints();
    }
    
    @Override
    public String toString() {
        return (blockName != null) ? blockName : super.toString();
    }
    
    @Override
    public void onClosed() {
        for (var input : inputPoints) {
            for (int i = 0; i < input.connections.size(); i++) {
                var con = input.connections.get(i);
                con.removeConnection(input);
                input.removeConnection(con);
                i--;
            }
        }
        
        for (var output : outputPoints) {
            for (int i = 0; i < output.connections.size(); i++) {
                var con = output.connections.get(i);
                con.setValue(null);
                con.removeConnection(output);
                output.removeConnection(con);
                i--;
            }
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void connectInputToPoint(int inputPoint, BlockConnectionPoint<?> toPoint) {
        BlockConnectionPoint<?> input = getInputConnection(inputPoint);
        if (input == null) return;
        input.addConnection(toPoint);
    }
    
    public EList<String> getInputPointNames() {
        return inputPoints.map(BlockConnectionPoint::getPointName);
    }
    
    public EList<String> getOutputPointNames() {
        return outputPoints.map(BlockConnectionPoint::getPointName);
    }
    
    public void addInputPoint(BlockConnectionPoint<?> input) {
        inputPoints.add(input);
        addObject(input);
    }
    
    public void removeInputPoint(BlockConnectionPoint<?> input) {
        inputPoints.remove(input);
        removeObject(input);
    }
    
    public void addOutputPoint(BlockConnectionPoint<?> output) {
        outputPoints.add(output);
        addObject(output);
    }
    
    public void removeOutputPoint(BlockConnectionPoint<?> output) {
        outputPoints.remove(output);
        addObject(output);
    }
    
    public void clearInputs() {
        inputPoints.clear();
        for (var i : inputPoints) removeObject(i);
        positionPoints();
        
        // there should probably be a way for connections to know if their dest points still exist
        // and remove the connection to them if they no longer do.
    }
    
    public void clearOutputs() {
        outputPoints.clear();
        for (var i : outputPoints) removeObject(i);
        positionPoints();
        
        // there should probably be a way for connections to know if their dest points still exist
        // and remove the connection to them if they no longer do.
    }
    
    // Input Creators
    
    public <T> BlockConnectionPoint<T> createInputPoint(String pointName) { 
        return createInputPoint(pointName, Integer.MAX_VALUE, PointLocation.LEFT);
    }
    public <T> BlockConnectionPoint<T> createInputPoint(String pointName, int maxConnections) {
        return createInputPoint(pointName, maxConnections, PointLocation.LEFT);
    }
    public <T> BlockConnectionPoint<T> createInputPoint(String pointName, PointLocation location) {
        return createInputPoint(pointName, Integer.MAX_VALUE, location);
    }
    public <T> BlockConnectionPoint<T> createInputPoint(String pointName, int maxConnections, PointLocation location) {
        BlockConnectionPoint<T> point = new BlockConnectionPoint(this, pointName);
        point.setInput(true);
        point.setMaxPointConnections(maxConnections);
        point.setPointLocation(location);
        addInputPoint(point);
        return point;
    }
    
    // Output Creators
    
    public <T> BlockConnectionPoint<T> createOutputPoint(String pointName) { 
        return createOutputPoint(pointName, Integer.MAX_VALUE, PointLocation.RIGHT);
    }
    public <T> BlockConnectionPoint<T> createOutputPoint(String pointName, int maxConnections) {
        return createOutputPoint(pointName, maxConnections, PointLocation.RIGHT);
    }
    public <T> BlockConnectionPoint<T> createOutputPoint(String pointName, PointLocation location) {
        return createOutputPoint(pointName, Integer.MAX_VALUE, location);
    }
    public <T> BlockConnectionPoint<T> createOutputPoint(String pointName, int maxConnections, PointLocation location) {
        BlockConnectionPoint<T> point = new BlockConnectionPoint(this, pointName);
        point.setInput(false);
        point.setMaxPointConnections(maxConnections);
        point.setPointLocation(location);
        addOutputPoint(point);
        return point;
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void positionPoints() {
        if (!isInitialized()) return;
        
        // organize points by location
        Map<PointLocation, EList<BlockConnectionPoint<?>>> pointsByLocation = new HashMap<>();
        
        for (var input : inputPoints) {
            PointLocation location = input.getPointLocation();
            EList<BlockConnectionPoint<?>> list = pointsByLocation.get(location);
            if (list == null) {
                list = EList.newList();
                pointsByLocation.put(location, list);
            }
            list.add(input);
        }
        
        for (var output : outputPoints) {
            PointLocation location = output.getPointLocation();
            EList<BlockConnectionPoint<?>> list = pointsByLocation.get(location);
            if (list == null) {
                list = EList.newList();
                pointsByLocation.put(location, list);
            }
            list.add(output);
        }
        
        final int pointWidth = 10;
        final int pointHeight = 10;
        final double sx = startX;/* - pointWidth * 0.5;*/
        final double sy = startY;/*  - pointHeight * 0.5;*/
        final double ex = endX - pointWidth;/* * 0.5;*/
        final double ey = endY - pointHeight;/* * 0.5;*/
        
        final double tsy = sy + 10; // top startY
        final double bsy = ey - 10; // bot startY
        final double gap = 20;
        
        
        // iterate across each point by location and position accordingly
        for (PointLocation location : pointsByLocation.keySet()) {
            EList<BlockConnectionPoint<?>> list = pointsByLocation.get(location);
            
            // don't care if there's nothing to position
            if (list == null || list.isEmpty()) continue;
            
            int i = 0;
            switch (location) {
            case TOP_LEFT: for (var p : list) p.setPosition(sx, tsy + (i++ * gap)); break;
            case BOT_LEFT: for (var p : list) p.setPosition(sx, bsy - (i++ * gap)); break;
            case TOP_RIGHT: for (var p : list) p.setPosition(ex, tsy - (i++ * gap)); break;
            case BOT_RIGHT: for (var p : list) p.setPosition(ex, bsy - (i++ * gap)); break;
            case LEFT: 
            case RIGHT: {
                double x = (location == PointLocation.LEFT) ? sx : ex; // draw on left/right
                double y = midY - ((list.size() % 2 == 0) ? gap * 0.5 : 0.0); // offset if even
                y -= ((pointHeight * 0.5)); // move up to the size of the list
                for (var p : list) p.setPosition(x, y + (i++ * gap));
                break;
            }
            default:
                break;
            }
        }
    }
    
    //=========
    // Getters
    //=========
    
    public String getBlockName() { return blockName; }
    
    public int getNumInputs() { return inputPoints.size(); }
    public int getNumOutputs() { return outputPoints.size(); }
    
    public BlockConnectionPoint<?> getInputConnection(int point) {
        if (point < 0 || point >= inputPoints.size()) return null;
        return inputPoints.get(point);
    }
    
    public BlockConnectionPoint<?> getOutputConnection(int point) {
        if (point < 0 || point >= outputPoints.size()) return null;
        return outputPoints.get(point);
    }
    
    public Object getInputPointValue(int point) {
        BlockConnectionPoint<?> input = getInputConnection(point);
        if (input == null) return null;
        return input.getValue();
    }
    
    public <T> T getTypedInputPointValue(int point) {
        BlockConnectionPoint<?> input = getInputConnection(point);
        if (input == null) return null;
        return (T) input.getValue();
    }
    
    public Object getOutputPointValue(int point) {
        BlockConnectionPoint<?> output = getOutputConnection(point);
        if (output == null) return null;
        return output.getValue();
    }
    
    public <T> T getTypedOutputPointValue(int point) {
        BlockConnectionPoint<?> output = getOutputConnection(point);
        if (output == null) return null;
        return (T) output.getValue();
    }
    

    
    public EList<BlockConnectionPoint> getConnectionsForOutput(int point) {
        BlockConnectionPoint<?> output = getOutputConnection(point);
        if (output == null) return null;
        return output.getConnections();
    }
    
    //=========
    // Setters
    //=========
    
    public void setName(String nameIn) { blockName = nameIn; }
    
}
