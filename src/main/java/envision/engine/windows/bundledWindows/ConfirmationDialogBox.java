package envision.engine.windows.bundledWindows;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;

/**
 * A type of dialog box that requests either a 'YES' or 'NO' response from the user.
 * <p>
 * 
 * 
 * @param <E>
 * @author Hunter Bragg
 */
public class ConfirmationDialogBox<E> extends WindowDialogueBox {
    
    //========
    // Fields
    //========
    
    private Runnable completionAction;
    private WindowLabel textLabel;
    private WindowButton yes, no;
    
    //==============
    // Constructors
    //==============
    
    public ConfirmationDialogBox(String textIn) {
        this(Envision.getActiveTopParent(), textIn, null);
    }
    
    public ConfirmationDialogBox(String textIn, Runnable actionToRunIfYes) {
        this(Envision.getActiveTopParent(), textIn, actionToRunIfYes);
    }
    
    public ConfirmationDialogBox(IWindowObject parent, String textIn, Runnable actionToRunIfYes) {
        super(parent, DialogBoxTypes.YES_NO);
        
        message = textIn;
        completionAction = actionToRunIfYes;
    }
    
    private void setupWindow() {
        setGuiSize(300, 200);
        setMinDims(300, 200);
        setResizeable(true);
        setMaximizable(false);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void onAdded() {
        getTopParent().setFocusLockObject(this);
        this.requestFocus();
    }
    
    @Override
    public void initChildren() {
        defaultHeader();
        setTitle("Confirmation Request");
        
        textLabel = new WindowLabel(this, midX, midY - height * 0.20, message);
        textLabel.enableWordWrap(true, width);
        textLabel.setDrawCentered(true);
        
        var bY = textLabel.endY;
        var bWidth = width / 5;
        var gap = width / 20;
        var hGap = gap * 0.5; // half gap
        
        yes = new WindowButton(this, midX - (bWidth * 2) - hGap, bY, bWidth, 40);
        no = new WindowButton(this, midX + bWidth + hGap, bY, bWidth, 40);
        
        yes.setString(EColors.green + "Yes");
        no.setString(EColors.lred + "No");
        
        yes.onPress(() -> {
            if (completionAction != null) completionAction.run();
            close();
        });
        no.onPress(() -> close());
        
        addObject(textLabel, yes, no);
    }
    
    @Override
    public void postReInit() {
        //System.out.println("LOL");
        //textLabel.setPosition(midX, midY - textLabel.height / 2);
    }
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject(mXIn, mYIn);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ENTER) {
            if (completionAction != null) completionAction.run();
            close();
        }
    }
    
    //=========
    // Setters
    //=========
    
    public void setText(String textIn) {
        message = textIn;
        
        if (textLabel != null) {
            textLabel.setString(message);
        }
    }
    
    //=================
    // Static Builders
    //=================
    
    public static void showDialog(String text) { showDialog(text, null); }
    public static void showDialog(String text, Runnable action) {
        var dialog = buildDialog(text, action);
        
        // display on top parent
        Envision.getActiveTopParent().displayWindow(dialog);
    }
    
    public static <E> ConfirmationDialogBox<E> buildDialog(String text) { return buildDialog(text, null); }
    public static <E> ConfirmationDialogBox<E> buildDialog(String text, Runnable action) {
        return new ConfirmationDialogBox<>(text, action);
    }
    
}
