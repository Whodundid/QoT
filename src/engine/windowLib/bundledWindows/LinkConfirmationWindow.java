package engine.windowLib.bundledWindows;

import engine.windowLib.bundledWindows.utilityWindows.WindowDialogueBox;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.basicObjects.WindowLabel;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import main.QoT;

//Author: Hunter Bragg

public class LinkConfirmationWindow extends WindowDialogueBox {
	
	String link;
	WindowButton yes, copy, no;
	
	public LinkConfirmationWindow(String linkIn) { this(QoT.getActiveTopParent(), linkIn); }
	public LinkConfirmationWindow(IWindowObject parentIn, String linkIn) {
		super(parentIn);
		setResizeable(false);
		link = linkIn;
	}
	
	@Override
	public void initObjects() {
		String prompt = "Confirm";
		String warning = "Warning";
		
		int longestString = 0;
		longestString = getStringWidth(prompt);
		int linkLength = getStringWidth(link);
		if (linkLength > longestString) { longestString = linkLength; }
		int copyLength = getStringWidth("Copy") + 8;
		int buttonLength = copyLength + 232; //length of all buttons and gaps inbetween
		if (buttonLength > longestString) { longestString = buttonLength; }
		
		double x = res.getWidth() / 2 - longestString / 2 - 15;
		double y = res.getHeight() / 2 - 60;
		double w = longestString > 400 ? 400 : longestString + 30;
		
		WindowLabel promptLabel = new WindowLabel(this, res.getWidth() / 2, y + 18, prompt).setDrawCentered(true).setColor(0xffbb00);
		WindowLabel linkLabel = new WindowLabel(this, res.getWidth() / 2, y + 33, link).setDrawCentered(true).enableWordWrap(true, longestString);
		WindowLabel warningLabel = new WindowLabel(this, res.getWidth() / 2, y + 38 + linkLabel.getTextHeight(), warning).setDrawCentered(true).setColor(0xff5555);
		
		double h = 100 + linkLabel.getTextHeight() < 110 ? 110 : 100 + linkLabel.getTextHeight();
		
		setDimensions(x, y, w, h);
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setTitle("Opening Web Link");
		setTitleColor(EColors.lgray.intVal);
		
		yes = new WindowButton(this, midX - copyLength - 35, endY - 35, 75, 20, "Yes").setStringColor(0x55ff55);
		copy = new WindowButton(this, midX - (copyLength  + 8) / 2, endY - 35, copyLength + 8, 20, "Copy").setStringColor(0xffffff);
		no = new WindowButton(this, midX + copyLength - 39, endY - 35, 75, 20, "No").setStringColor(0xff5555);
		
		addObject(promptLabel, linkLabel, warningLabel, yes, copy, no);
		
		super.initObjects();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		//if (object.equals(yes)) { openWebLink(link); close(); }
		//if (object.equals(copy)) { ((GuiScreen) getTopParent()).setClipboardString(link); close(); }
		if (object.equals(no)) { close(); }
	}
	
}
