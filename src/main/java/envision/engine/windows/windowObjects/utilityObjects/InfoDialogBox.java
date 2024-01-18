package envision.engine.windows.windowObjects.utilityObjects;

import envision.Envision;
import eutil.colors.EColors;

/**
 * A type of dialog box that shows an error on screen.
 * 
 * @param <E>
 * @author Hunter Bragg
 */
public class InfoDialogBox<E> extends WindowDialogueBox {
    
    //==============
    // Constructors
    //==============
    
    public InfoDialogBox(String textIn) {
        this("Info", textIn);
    }
    
    public InfoDialogBox(String titleIn, String textIn) {
        super(Envision.getActiveTopParent(), DialogBoxTypes.OK);
        
        message = textIn;
        setTitle(titleIn);
        setTitleColor(EColors.lgreen);
    }
    
    //=================
    // Static Builders
    //=================
    
    public static void showDialog(String text) {
        // display on top parent
        var dialog = buildDialog(text);
        Envision.getActiveTopParent().displayWindow(dialog);
    }
    
    public static void showDialog(String title, String text) {
        // display on top parent
        var dialog = buildDialog(title, text);
        Envision.getActiveTopParent().displayWindow(dialog);
    }
    
    public static <E> InfoDialogBox<E> buildDialog(String text) {
        return new InfoDialogBox<>(text);
    }
    
    public static <E> InfoDialogBox<E> buildDialog(String title, String text) {
        return new InfoDialogBox<>(title, text);
    }
    
}
