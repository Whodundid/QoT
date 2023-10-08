package envision.engine.terminal.commands;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public interface ListableCommand {
    
    /**
     * @return the 'list' of values from this listable command.
     */
    EList<String> getList();
    
    /**
     * Returns true if this listable command has a customized list display
     * method that should be called instead.
     * <p>
     * Returns false by default. Override in implementing instances.
     */
    default boolean hasCustomizedListDisplay() {
        return false;
    }
    
    /**
     * Used for customized list displays. Does nothing by default.
     * 
     * @param termIn The terminal to display to
     */
    default void displayList(ETerminalWindow termIn) {}
    
}
