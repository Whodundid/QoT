package qot.screens.gameplay;

import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.FutureTaskEventType;
import eutil.colors.EColors;
import qot.QoT;
import qot.screens.main.MainMenuScreen;
import qot.screens.main.OptionsScreen;

public class GamePauseWindow extends WindowParent {
	
	private GameScreen parent;
	
	private WindowButton<?> quit, resume;
	private WindowButton<?> options;
	
	public GamePauseWindow(GameScreen parentScreen, int x, int y) {
		init(parentScreen, x, y, 300, 200);
		setMaxDims(400, 200);
		setMinDims(200, 100);
		setMinimizable(false);
		setResizeable(true);
		setMoveable(false);
		
		parent = parentScreen;
	}
	
	@Override
	public void initChildren() {
		QoT.pause();
		
		//defaultHeader();
		
		var w = width - 60;
		var sX = midX - w / 2;
		var gap = 5;
		
		resume = new WindowButton(this, sX, startY + 20, w, 50, "Resume Game");
		options = new WindowButton(this, sX, resume.endY + gap, w, 50, "Options");
		quit = new WindowButton(this, sX, options.endY + gap, w, 50, "Quit");
		
		// Makes it so confirm and deny send an action to this confirmation window
		IActionObject.setActionReceiver(this, quit, options, resume);
		
		addObject(quit, options, resume);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(0, 0, QoT.getWidth(), QoT.getHeight(), EColors.vdgray.opacity(150));
		drawDefaultBackground();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == quit) {
			QoT.loadWorld(null); // Unload current world
			QoT.displayScreen(new MainMenuScreen()); // Display main menu
		}
		
		if (object == options) {
			var opScreen = QoT.displayScreen(new OptionsScreen(), parent);
			if (parent instanceof GamePlayScreen g) {
				opScreen.addFutureTask(FutureTaskEventType.ON_CLOSED, () -> g.openPauseWindowIfNotOpen());
			}
		}
		
		if (object == resume) close();
	}
	
	@Override
	public void onClosed() {
		QoT.unpause();
	}
	
}
