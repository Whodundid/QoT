package envision.engine.creation.block;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eutil.datatypes.util.EList;

public abstract class FunctionBlock extends CreatorBlock {
    
    //========
    // Fields
    //========
    
    protected ConcurrentMap<Integer, CreatorBlock> inputConnections = new ConcurrentHashMap<>();
    protected ConcurrentMap<Integer, EList<CreatorBlock>> outputConnections = new ConcurrentHashMap<>();
    
    //==============
    // Constructors
    //==============
    
    protected FunctionBlock(String blockName) {
        super(blockName);
    }
    
    //=========
    // Methods
    //=========
    
    public void addInput(int point, CreatorBlock input) {
        if (input == null) return;
        if (point < 0) return;
        
        inputConnections.put(point, input);
    }
    
    public void addOutput(int point, CreatorBlock output) {
        if (output == null) return;
        if (point < 0) return;
        
        var list = outputConnections.get(point);
        if (list == null) {
            list = EList.newList();
            outputConnections.put(point, list);
        }
        list.addIfNotContains(output);
    }
    
    public void removeInput(int point, CreatorBlock input) {
        if (input == null) return;
        if (point < 0) return;
        
        inputConnections.remove(point);
    }
    
    public void removeOutput(int point, CreatorBlock output) {
        if (output == null) return;
        if (point < 0) return;
        
        var list = outputConnections.get(point);
        if (list == null) return;
        list.remove(output);
    }
    
    //=========
    // Getters
    //=========
    
    public CreatorBlock getInputConnection(int point) {
        return inputConnections.get(point);
    }
    
    public EList<CreatorBlock> getConnectionsForOutputPoint(int point) {
        return outputConnections.get(point);
    }
    
    public EList<Integer> getInputPoints() {
        EList<Integer> points = EList.of(inputConnections.keySet());
        points.sort((a, b) -> Integer.compare(a, b));
        return points;
    }
    
    public EList<Integer> getOutputPoints() {
        EList<Integer> points = EList.of(outputConnections.keySet());
        points.sort((a, b) -> Integer.compare(a, b));
        return points;
    }
    
    public int getNumInputs() { return inputConnections.size(); }
    public int getNumOutputs() { return outputConnections.size(); }
    
    //=========
    // Setters
    //=========
    
}
