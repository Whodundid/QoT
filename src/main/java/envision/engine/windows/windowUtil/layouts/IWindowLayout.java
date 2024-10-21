package envision.engine.windows.windowUtil.layouts;

import java.util.Collection;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;

public interface IWindowLayout {
    
    void applyLayout(IWindowObject parent, Collection<IWindowObject> children);
    
}
