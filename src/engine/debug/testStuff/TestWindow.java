package engine.debug.testStuff;

import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import engine.windowLib.windowTypes.WindowParent;
import main.QoT;

public class TestWindow extends WindowParent {
	
	private TabbedContainer tabContainer;
	
	public TestWindow(double w, double h) {
		init(QoT.getTopRenderer());
		setSize(w, h);
		setResizeable(true);
		setMinimizable(true);
		setObjectName("Test Window");
	}
	
	@Override
	public void initChildren() {
		defaultHeader();
		
		tabContainer = new TabbedContainer(this, startX + 5, startY + 5, width - 10, height - 10);
		
		var banana = tabContainer.addTab("Banana");
		var test = tabContainer.addTab("TEST");
		
		banana.addObject(new WindowButton(banana, banana.startX + 50, banana.startY + 50, 200, 40, "HEY"));
		
		addObject(tabContainer);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
}
