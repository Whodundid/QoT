package envision.engine.creation.history;

import eutil.datatypes.util.EList;

public interface IEditAction<T, O> {
    
    EList<T> getItems();
    O getTarget();
    EList<Object> getModifiers();
    
    void doAction();
    void undoAction();
    
}
