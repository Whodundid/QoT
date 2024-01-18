package envision.debug.testStuff;

import envision.Envision;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextDocument;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea2;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.dialog.TextWriterOverTime;

public class TestWindow extends WindowParent {
	
	private WindowScrollList scrollList;
	private TabbedContainer container;
	
	private TextDocument document;
	private TextWriterOverTime text;
	
	private String building = "";
	
	public TestWindow(double x, double y, double w, double h) {
		init(Envision.getDeveloperDesktop(), x, y, w, h);
		setResizeable(true);
		setMinimizable(false);
		setMaximizable(true);
		setObjectName("TEST");
	}
	
	@Override
	public void initChildren() {
		defaultHeader();
		
//		container = new TabbedContainer(this, startX + 5, startY + 5, width - 10, height - 10);
//		
//		var tabA = container.addTab("A");
//		var tabB = container.addTab("B");
//		
//		var aDims = tabA.getTabDims();
//		var bDims = tabB.getTabDims();
//		
//		var bA = new WindowLabel(tabA, aDims.startX + 50, aDims.startY + 50, "LOL") {
//			@Override
//			public void mousePressed(int mXIn, int mYIn, int button) {
//				System.out.println("PRESSED");
//			}
//		};
//		tabA.addObject(bA);
//		
//		var bB = new WindowButton(tabB, bDims.startX + 50, bDims.startY + 50, 180, 50, "HEY THERE");
//		tabB.addObject(bB);
//		
//		IActionObject.setActionReceiver(this, bB);
		
		String toSay = "Have you ever heard the tale of Darth Plagius the Wise? " +
		               TextWriterOverTime.addPause(50) + "I thought not..";
		
		text = new TextWriterOverTime(toSay, 7000);
		
		WindowTextArea2 ta = new WindowTextArea2(this, startX + 5, startY + 5, width - 10, height - 10, document);
		document = ta.getDocument();
		document.clearListeners();
		document.registerListener(ta);
		//ta.setText(EColors.cyan + "Billy\n" + EColors.blue + "Bob\n" + EColors.green + "Nugget");
		
		addObject(ta);
	}
	
	@Override
	public void drawObject_i(float dt, int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject_i(dt, mXIn, mYIn);
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {

	}
	
	@Override
	public void onTickUpdate_i(float dt) {
	    super.onTickUpdate_i(dt);
	}
	
	@Override
	public void onTickUpdate(float dt) {
        text.update(dt);
        String s = text.getNextCharOverTime();
        //	      building += s;
        //	      drawString(text.getStringOverTime(), startX + 50, endY + 50);
        //	      drawString(text.getLastChar(), startX + 50, endY + 70);
        //	      drawString(building, startX + 50, endY + 90);
        //	      
        document.insertString(s);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
	    super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
	    super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		System.out.println(object);
	}
	
}
