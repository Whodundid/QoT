package envisionEngine.windowLib.windowObjects.utilityObjects;

import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;
import envisionEngine.windowLib.windowObjects.basicObjects.WindowLabel;
import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import qot.QoT;

//Author: Hunter Bragg

public class LinkConfirmationWindow extends WindowDialogueBox {
	
	//--------
	// Fields
	//--------
	
	private String link;
	private WindowButton<?> yes, copy, no;
	
	//--------------
	// Constructors
	//--------------
	
	public LinkConfirmationWindow(String linkIn) { this(QoT.getActiveTopParent(), linkIn); }
	public LinkConfirmationWindow(IWindowObject<?> parentIn, String linkIn) {
		super(parentIn);
		setResizeable(false);
		link = linkIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
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
		
		WindowLabel promptLabel = new WindowLabel(this, res.getWidth() / 2, y + 18, prompt);
		WindowLabel linkLabel = new WindowLabel(this, res.getWidth() / 2, y + 33, link);
		WindowLabel warningLabel = new WindowLabel(this, res.getWidth() / 2, y + 38 + linkLabel.getTextHeight(), warning);
		
		promptLabel.setDrawCentered(true);
		promptLabel.setColor(0xffffbb00);
		
		linkLabel.setDrawCentered(true);
		linkLabel.enableWordWrap(true, longestString);
		
		warningLabel.setDrawCentered(true);
		warningLabel.setColor(0xffff5555);
		
		double h = 100 + linkLabel.getTextHeight() < 110 ? 110 : 100 + linkLabel.getTextHeight();
		
		setDimensions(x, y, w, h);
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setTitle("Opening Web Link");
		setTitleColor(EColors.lgray.intVal);
		
		yes = new WindowButton(this, midX - copyLength - 35, endY - 35, 75, 20, "Yes");
		copy = new WindowButton(this, midX - (copyLength  + 8) / 2, endY - 35, copyLength + 8, 20, "Copy");
		no = new WindowButton(this, midX + copyLength - 39, endY - 35, 75, 20, "No");
		
		yes.setStringColor(0xff55ff55);
		copy.setStringColor(0xffffffff);
		no.setStringColor(0xffff5555);
		
		addObject(promptLabel, linkLabel, warningLabel, yes, copy, no);
		
		super.initChildren();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		//if (object.equals(yes)) { openWebLink(link); close(); }
		//if (object.equals(copy)) { ((GuiScreen) getTopParent()).setClipboardString(link); close(); }
		if (object.equals(no)) close();
	}
	
}
