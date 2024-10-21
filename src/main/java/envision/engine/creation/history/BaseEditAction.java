package envision.engine.creation.history;

import java.util.ArrayList;
import java.util.List;

import eutil.datatypes.util.EList;

public abstract class BaseEditAction<T, O> implements IEditAction<T, O> {
    
    //========
    // Fields
    //========
    
    /** The visible description of this action when viewed in an edit history list. */
    protected final String actionDisplayString;
    /** The object that is ultimately being modified by this action. */
    protected final O targetObject;
    /** The items that this edit action is targeting. */
    protected final EList<T> items = EList.newList();
    /** Any arguments that support the edit action */
    protected final EList<Object> modifiers = EList.newList();
    
    //==============
    // Constructors
    //==============
    
    protected BaseEditAction(String displayString, O targetObjectIn, List<T> itemsIn) {
        this(displayString, targetObjectIn, itemsIn, new ArrayList<>());
    }
    
    protected BaseEditAction(String displayString, O targetObjectIn, List<T> itemsIn, List<?> modifiersIn) {
        actionDisplayString = displayString;
        targetObject = targetObjectIn;
        items.addAll(itemsIn);
        modifiers.addAll(modifiersIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EList<T> getItems() {
        return items;
    }
    
    @Override
    public O getTarget() {
        return targetObject;
    }
    
    @Override
    public EList<Object> getModifiers() {
        return modifiers;
    }
    
    //=========
    // Getters
    //=========
    
    public String getActionDisplayString() {
        return actionDisplayString;
    }
    
}
