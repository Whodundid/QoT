package envision.engine.internal.windows.windowUtil.layouts;

import java.util.Collection;

import envision.engine.internal.windows.windowObjects.advanced.header.WindowHeader;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.math.ENumUtil;

public class WindowBorderLayout implements IWindowLayout {
    
    //========
    // Fields
    //========
    
    public static final LayoutConstraint
    NORTH = new LayoutConstraint(0),
    EAST = new LayoutConstraint(1),
    SOUTH = new LayoutConstraint(2),
    WEST = new LayoutConstraint(3),
    CENTER = new LayoutConstraint(4);
    
    protected double hgap;
    protected double vgap;
    
    protected IWindowObject northObject;
    protected IWindowObject eastObject;
    protected IWindowObject southObject;
    protected IWindowObject westObject;
    protected IWindowObject centerObject;    
    //==============
    // Constructors
    //==============
    
    public WindowBorderLayout() { this(0.0, 0.0); }
    public WindowBorderLayout(double hgapIn, double vgapIn) {
        hgap = hgapIn;
        vgap = vgapIn;
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void applyLayout(IWindowObject parent, Collection<IWindowObject> children) {
        reset();
        
        // there's nothing to do if we don't have children to work with
        if (children == null || children.isEmpty()) {
            return;
        }
        
        // figure out which objects should be in the layout
        determineLayoutObjects(children);
        
        // apply the layout to each object
        final var dims = parent.getDimensions();
        double top = dims.startY;
        double bottom = dims.endY;
        double left = dims.startX;
        double right = dims.endX;
        
        if (northObject != null) {
            var d = northObject.getDimensions();
            double h = ENumUtil.clamp(d.height, northObject.getMinHeight(), northObject.getMaxHeight());
            northObject.setDimensions(left, top, right - left, h);
            top += h + vgap;
        }
        if (southObject != null) {
            var d = southObject.getDimensions();
            double h = ENumUtil.clamp(d.height, southObject.getMinHeight(), southObject.getMaxHeight());
            southObject.setDimensions(left, bottom - h, right - left, h);
            bottom -= h + vgap;
        }
        if (eastObject != null) {
            var d = eastObject.getDimensions();
            double w = ENumUtil.clamp(d.width, eastObject.getMinWidth(), eastObject.getMaxWidth());
            eastObject.setDimensions(right - w, top, w, bottom - top);
            right -= w + hgap;
        }
        if (westObject != null) {
            var d = westObject.getDimensions();
            double w = ENumUtil.clamp(d.width, westObject.getMinWidth(), westObject.getMaxWidth());
            westObject.setDimensions(left, top, w, bottom - top);
            left += w + hgap;
        }
        if (centerObject != null) {
            centerObject.setDimensions(left, top, right - left, bottom - top);
        }
        
        var h = parent.getHeader();
        if (h != null) {
            ((IWindowObject) h).setDimensions(left, h.startY, dims.width, h.height);
        }
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected void reset() {
        northObject = null;
        eastObject = null;
        southObject = null;
        westObject = null;
        centerObject = null;
    }
    
    protected void determineLayoutObjects(Collection<IWindowObject> children) {
        // if we only have one child, make it the center object
        if (children.size() == 1) {
            IWindowObject o = children.iterator().next();
            // don't reposition headers
            if (o instanceof WindowHeader) return;
            if (o != null) centerObject = o;
            return;
        }
        
        // go through every child and figure out if any have border layout constraints
        for (IWindowObject o : children) {
            if (o == null) continue;
            // don't reposition headers
            if (o instanceof WindowHeader) continue;
            
            LayoutConstraint co = o.getLayoutConstraint();
            if (co == null) {
                co = CENTER;
            }
            
            if (co.value instanceof Integer c) {
                switch (c) {
                case 0: northObject = o; break;
                case 1: eastObject = o; break;
                case 2: southObject = o; break;
                case 3: westObject = o; break;
                case 4: centerObject = o; break;
                default: // do nothing
                }
            }
        }
        
        // if we don't have any defined objects, make each the center
        if (EUtil.allNull(northObject, eastObject, southObject, westObject, centerObject)) {
            for (IWindowObject o : children) {
                if (o == null) continue;
                // don't reposition headers
                if (o instanceof WindowHeader) continue;
                centerObject = o;
            }
        }
    }    
    //=========
    // Getters
    //=========
    
    public double getHGap() { return hgap; }
    public double getVGap() { return vgap; }
    
    public IWindowObject getNorthObject() { return northObject; }
    public IWindowObject getEastObject() { return eastObject; }
    public IWindowObject getSouthObject() { return southObject; }
    public IWindowObject getWestObject() { return westObject; }
    public IWindowObject getCenterObject() { return centerObject; }    
    //=========
    // Setters
    //=========
    
    public void setHGap(double val) { hgap = val; }
    public void setVGap(double val) { vgap = val; }
    
}
