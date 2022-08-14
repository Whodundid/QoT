package engine.windowLib.windowObjects.utilityObjects;

import engine.inputHandlers.Keyboard;
import engine.renderEngine.fontRenderer.EStringBuilder;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.ActionWindowParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;

//Author: Hunter Bragg

public class WindowDialogBox extends ActionWindowParent {
	
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
	protected IWindowObject<?> defaultObject;
	protected WindowButton yes, no, okButton;
	protected DialogBoxTypes type;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowDialogBox(IWindowObject<?> parentIn) { this(parentIn, DialogBoxTypes.CUSTOM); }
	public WindowDialogBox(IWindowObject<?> parentIn, DialogBoxTypes typeIn) {
		super(parentIn);
		type = typeIn;
		setSize(400, 200);
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
				double bw = NumberUtil.clamp((width - 10) / 3, 0, 140);
				double g = width / 30;
				
				yes = new WindowButton(this, midX - g - bw, endY - 30, bw, 20, "Yes");
				no = new WindowButton(this, midX + g, endY - 30, bw, 20, "No");
				
				no.setStringColor(EColors.yellow);
				yes.setStringColor(EColors.lgreen);
				
				addChild(yes, no);
				
				defaultObject = yes;
				break;
			case OK:
				okButton = new WindowButton(this, midX - 40, endY - 30, 80, 20, "Ok") {
					@Override
					public void onPress(int button) {
						playPressSound();
						getParent().close();
					}
				};
				okButton.setRunActionOnPress(true);
				addChild(okButton);
				
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
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedKey, int keyCode) {
		if (keyCode == Keyboard.KEY_ENTER) {
			if (defaultObject instanceof WindowButton<?> wb) {
				wb.performAction();
			}
		}
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowObject<?> getPrimaryObject() { return defaultObject; }
	
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
	
	public void setTitleColor(int colorIn) {
		titleColor = colorIn;
		if (getHeader() != null) header.setTitleColor(titleColor);
	}
	
	public void setMessage(String stringIn) {
		message = stringIn;
		wordWrappedLines = EStringBuilder.createWordWrapString(message, (int) width - 20);
	}
	
}
