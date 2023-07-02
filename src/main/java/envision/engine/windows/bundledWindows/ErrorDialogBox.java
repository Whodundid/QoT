package envision.engine.windows.bundledWindows;

import envision.Envision;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import eutil.colors.EColors;

/**
 * A type of dialog box that shows an error on screen.
 * 
 * @param <E>
 * @author Hunter Bragg
 */
public class ErrorDialogBox<E> extends WindowDialogueBox {
    
    //==============
    // Constructors
    //==============
    
    public ErrorDialogBox(String textIn) {
        super(Envision.getActiveTopParent(), DialogBoxTypes.OK);
        
        message = textIn;
        setTitle("Error");
        setTitleColor(EColors.lred);
    }
    
    //=================
    // Static Builders
    //=================
    
    public static void showDialog(String text) {
        var dialog = buildDialog(text);
        
        // display on top parent
        Envision.getActiveTopParent().displayWindow(dialog);
    }
    
    public static <E> ErrorDialogBox<E> buildDialog(String text) {
        return new ErrorDialogBox<>(text);
    }
    
}
