package game.screens.testingStuff;

import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.screens.main.MainMenuScreen;
import main.QoT;

public class TestInventory<E> extends WindowParent<E> {
	
	private WindowButton close;
	
	//--------------
	// Constructors
	//--------------
	
	public TestInventory() {
		//nothing
	}

	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setObjectName("Inventory");
		setMaximizable(true);
		setMinimizable(true);
		setResizeable(true);
		setMinDims(300, 300);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		close = new WindowButton(this, startX + 10, startY + 10, 50, 50);
		close.setActionReceiver(this);
		
		WindowParent innerWindow = new WindowParent(this, (int) (startX + 60), (int) (startY + 60), 160, 100) {
			private WindowButton sayHi;
			private boolean drawHi = false;
			private long timeOut = 2000;
			private long startTime;
			
			@Override
			public void initWindow() {
				setCloseable(false);
				setMinimizable(false);
			}
			
			@Override
			public void initChildren() {
				defaultHeader(this);
				header.height = 10;
				
				sayHi = new WindowButton(this, startX + 5, startY + 5, 100, 30, "Say Hi");
				sayHi.setActionReceiver(this);
				
				addChild(sayHi);
			}
			
			@Override
			public void drawObject(int mXIn, int mYIn) {
				drawDefaultBackground();
				
				if (drawHi) {
					drawString("HI", sayHi.startX, sayHi.endY + FontRenderer.FONT_HEIGHT, EColors.rainbow());
					
					if ((System.currentTimeMillis() - startTime) >= timeOut) {
						drawHi = false;
					}
				}
				
				super.drawObject(mXIn, mYIn);
			}
			
			@Override
			public void actionPerformed(IActionObject object, Object... args) {
				if (object == sayHi) {
					drawHi = true;
					startTime = System.currentTimeMillis();
				}
			}
		};
		
		innerWindow.setMoveWithParent(true);
		
		addChild(close);
		addChild(innerWindow);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		//AT THE END -- IN ORDER TO DRAW CHILD OBJECTS
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == close) closeWindow();
	}
	
	private void closeWindow() {
		QoT.displayScreen(new MainMenuScreen());
	}
	
}
