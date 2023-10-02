package envision.engine.windows.windowObjects.utilityObjects;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.ActionWindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

//Author: Hunter Bragg

public class WindowDialogueBox<E> extends ActionWindowParent {
	
	//-------
	// Types
	//-------
	
	public static enum DialogBoxTypes { YES_NO, OK, CUSTOM; }
	
	//--------
	// Fields
	//--------
	
	public int messageColor = 0xffffffff, titleColor = 0xffffffff;
	public String message = "", title = "";
	protected EList<String> wordWrappedLines;
	protected IWindowObject defaultObject;
	protected WindowButton yes, no, okButton;
	protected DialogBoxTypes type;
	protected E genericObject;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowDialogueBox(IWindowObject parentIn) { this(parentIn, DialogBoxTypes.CUSTOM); }
	public WindowDialogueBox(IWindowObject parentIn, DialogBoxTypes typeIn) {
		super(parentIn);
		type = typeIn;
		setGuiSize(400, 200);
		setResizeable(true);
		setMinDims(350, 150);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		getHeader().setTitle(title);
		getHeader().setTitleColor(titleColor);
		
		if (type != null) {
			switch (type) {
			case YES_NO:
				double bw = ENumUtil.clamp((width - 10) / 3, 0, 140);
				double g = width / 30;
				
				yes = new WindowButton(this, midX - g - bw, endY - 40, bw, 30, "Yes");
				no = new WindowButton(this, midX + g, endY - 40, bw, 30, "No");
				
				yes.setAction(() -> WindowDialogueBox.this.performAction("y", yes));
				no.setAction(() -> WindowDialogueBox.this.performAction("n", no));
				
				no.setStringColor(EColors.yellow);
				yes.setStringColor(EColors.lgreen);
				
				yes.setRunActionOnPress(true);
				no.setRunActionOnPress(true);
				
				addObject(yes, no);
				
				defaultObject = yes;
				break;
			case OK:
				okButton = new WindowButton<>(this, midX - 40, endY - 80, 80, 30, "Ok");
				okButton.setAction(() -> {
				    WindowButton.playPressSound();
				    close();
				});
				addObject(okButton);
				
				defaultObject = okButton;
				break;
			default: break;
			}
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (wordWrappedLines != null) {
			double lnWidth = wordWrappedLines.size() * 10;
			double totalWidth = (midY + 12) - startY;
			double lnStartY = startY + (totalWidth - lnWidth) / 2 - 10;
			double i = 0;
			
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			for (String s : wordWrappedLines) {
				drawStringCS(s, midX, lnStartY + (i * 26), messageColor);
				i++;
			}
			endScissor();
			
		}
	}
	
	@Override
	public void keyPressed(char typedKey, int keyCode) {
		if (keyCode == Keyboard.KEY_ENTER && defaultObject instanceof WindowButton<?> wb) {
		    wb.performAction();
		}
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowObject getPrimaryObject() { return defaultObject; }
	
	//---------
	// Setters
	//---------
	
	public void setDefaultObject(IWindowObject objIn) { defaultObject = objIn; }
	public void setMessageColor(EColors colorIn) { setMessageColor(colorIn.intVal); }
	public void setMessageColor(int colorIn) { messageColor = colorIn; }
	
	public void setTitle(String stringIn) {
		title = stringIn; setObjectName(title);
		if (getHeader() != null) header.setTitle(title);
	}
	
	public void setTitleColor(EColors color) { setTitleColor(color.intVal); }
	public void setTitleColor(int colorIn) {
		titleColor = colorIn;
		if (getHeader() != null) header.setTitleColor(titleColor);
	}
	
	public void setMessage(String stringIn) {
		message = stringIn;
		wordWrappedLines = EStringOutputFormatter.createWordWrapString(message, (int) width - 20);
	}
	
	//================
    // Generic Object
    //================
    
    public void setGenericObject(E objectIn) { genericObject = objectIn; }
    public E getGenericObject() { return genericObject; }
	
}
