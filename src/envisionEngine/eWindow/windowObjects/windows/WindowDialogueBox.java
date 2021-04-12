package envisionEngine.eWindow.windowObjects.windows;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import gameSystems.fontRenderer.EStringBuilder;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import storageUtil.EArrayList;

//Author: Hunter Bragg

public class WindowDialogueBox extends WindowParent {
	
	public int messageColor = 0xffffff, titleColor = 0xffffff;
	public String message = "", title = "";
	protected EArrayList<String> wordWrappedLines;
	protected IWindowObject defaultObject;
	protected WindowButton yes, no, okButton;
	protected DialogueBoxTypes type;
	
	public enum DialogueBoxTypes { yesNo, ok, custom; }
	
	protected WindowDialogueBox() { this(DialogueBoxTypes.custom); }
	public WindowDialogueBox(DialogueBoxTypes typeIn) {
		super();
		type = typeIn;
		setDimensions(250, 75);
		setResizeable(true);
		setMinDims(150, 85);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		getHeader().setTitle(title);
		getHeader().setTitleColor(titleColor);
		
		if (type != null) {
			switch (type) {
			case yesNo:
				double bw = NumberUtil.clamp((width - 10) / 3, 0, 140);
				double g = width / 30;
				
				yes = new WindowButton(this, midX - g - bw, endY - 30, bw, 20, "Yes");
				no = new WindowButton(this, midX + g, endY - 30, bw, 20, "No");
				
				no.setStringColor(EColors.yellow);
				yes.setStringColor(EColors.lgreen);
				
				addObject(yes, no);
				
				defaultObject = yes;
				break;
			case ok:
				okButton = new WindowButton(this, midX - 40, endY - 30, 80, 20, "Ok") {
					@Override
					public void onPress() {
						playPressSound();
						parent.close();
					}
				};
				okButton.setRunActionOnPress(true);
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
			double lnStartY = startY + (totalWidth - lnWidth) / 2 - 2;
			double i = 0;
			
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			for (String s : wordWrappedLines) {
				drawStringCS(s, midX, lnStartY + (i * 10), messageColor);
				i++;
			}
			endScissor();
			
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedKey, int keyCode) {
		if (keyCode == 28) { //enter
			if (defaultObject != null && defaultObject instanceof WindowButton) {
				((WindowButton) defaultObject).performAction();
			}
		}
	}
	
	public WindowDialogueBox setDefaultObject(IWindowObject objIn) { defaultObject = objIn; return this; }
	public WindowDialogueBox setTitle(String stringIn) { title = stringIn; setObjectName(title); if (getHeader() != null) { header.setTitle(title); } return this; }
	public WindowDialogueBox setTitleColor(int colorIn) { titleColor = colorIn; if (getHeader() != null) { header.setTitleColor(titleColor); } return this; }
	public WindowDialogueBox setMessageColor(int colorIn) { messageColor = colorIn; return this; }
	public WindowDialogueBox setMessage(String stringIn) {
		message = stringIn;
		wordWrappedLines = EStringBuilder.createWordWrapString(message, (int) width - 20);
		return this;
	}
	
	public IWindowObject getPrimaryObject() { return defaultObject; }
	
}
