package envision.game.world.layerSystem;

import java.util.Iterator;

import envision.engine.internal.inputHandlers.Mouse;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class LayerSystem implements Iterable<ScreenLayer> {
    
    //========
    // Fields
    //========
    
    private final EList<ScreenLayer> layers = new EArrayList<>();

    //==========
    // Overrides
    //==========
    
    @Override
    public Iterator<ScreenLayer> iterator() {
        return layers.iterator();
    }
    
    //========
    // Render
    //========
    
    public void onRenderTick(long dt) {
        for (int i = 0; i < layers.size(); i++) {
            ScreenLayer l = layers.get(i);
            l.renderLayer(dt, Mouse.getMx(), Mouse.getMy());
        }
    }    
    //=========
    // Methods
    //=========
    
    public ScreenLayer pushLayer() {
        return layers.pushR(new ScreenLayer());
    }
    
    public ScreenLayer popLayer() {
        if (layers.isEmpty()) return null;
        return layers.pop();
    }
    
    public ScreenLayer insertLayerAtIndex(int index) {
        if (index < 0 || index >= layers.size()) return null;
        return layers.addR(index, new ScreenLayer());
    }
    
    public void setNumLayer(int num) {
        for (int i = 0; i < num; i++) {
            pushLayer();
        }
    }    
    //=========
    // Getters
    //=========
    
    public int getLayerNum() {
        return layers.size();
    }
    
    public ScreenLayer getLayerAt(int index) {
        return layers.get(index);
    }
    
    public boolean isEmpty() { return layers.isEmpty(); }
    public boolean isNotEmpty() { return layers.isNotEmpty(); }
    
}
