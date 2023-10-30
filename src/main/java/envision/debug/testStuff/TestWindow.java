package envision.debug.testStuff;

import envision.Envision;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;

public class TestWindow extends WindowParent {
	
	private WindowScrollList scrollList;
	private TabbedContainer container;
	
	public TestWindow(double x, double y, double w, double h) {
		init(Envision.getDeveloperDesktop(), x, y, w, h);
		setResizeable(true);
		setMinimizable(false);
		setObjectName("TEST");
	}
	
	@Override
	public void initChildren() {
		defaultHeader();
		
		container = new TabbedContainer(this, startX + 5, startY + 5, width - 10, height - 10);
		
		var tabA = container.addTab("A");
		var tabB = container.addTab("B");
		
		var aDims = tabA.getTabDims();
		var bDims = tabB.getTabDims();
		
		var bA = new WindowLabel(tabA, aDims.startX + 50, aDims.startY + 50, "LOL") {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				System.out.println("PRESSED");
			}
		};
		tabA.addObject(bA);
		
		var bB = new WindowButton(tabB, bDims.startX + 50, bDims.startY + 50, 180, 50, "HEY THERE");
		tabB.addObject(bB);
		
		IActionObject.setActionReceiver(this, bB);
		
		addObject(container);
	}
	
	@Override
	public void drawObject_i(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject_i(mXIn, mYIn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
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
