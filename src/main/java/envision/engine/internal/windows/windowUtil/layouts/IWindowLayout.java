package envision.engine.internal.windows.windowUtil.layouts;

import java.util.Collection;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;

public interface IWindowLayout {
    
    void applyLayout(IWindowObject parent, Collection<IWindowObject> children);
    
}
