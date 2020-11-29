package eWindow.windowObjects.advancedObjects.screenHandler;

import eWindow.windowTypes.ActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import util.EUtil;
import util.mathUtil.NumUtil;
import util.storageUtil.EArrayList;

public class ScreenHandler<E> extends ActionObject<E> {
	
	private IWindowObject parent;
	private EArrayList<WindowScreen> screens = new EArrayList();
	private int currentScreen = 0;
	private boolean atBeginning = false;
	private boolean atEnd = false;
	
	//--------------------------
	//ScreenHandler Constructors
	//--------------------------
	
	public ScreenHandler(IWindowObject parentIn) { this(parentIn, (WindowScreen[]) null); }
	public ScreenHandler(IWindowObject parentIn, WindowScreen... screensIn) {
		super(parentIn);
		if (screensIn != null) { screens.add(screensIn); }
		atBeginning = true;
		actionReceiver = parentIn;
	}
	
	//---------------------
	//ScreenHandler Methods
	//---------------------
	
	public void drawCurrentScreen(double mXIn, double mYIn) {
		if (currentScreen >= 0 && currentScreen < screens.size()) {
			EUtil.nullDo(screens.get(currentScreen), s -> s.drawScreen(mXIn, mYIn));
		}
	}
	
	public void handleNext() { if (!nextScreenStage()) { nextScreen(); } }
	public void handlePrevious() { if (!previousScreenStage()) { previousScreen(); } }
	
	public boolean nextScreen() {
		if (currentScreen < screens.size() - 1) { //make sure not at end
			hideCurrent();
			currentScreen++;
			showCurrent();
			
			//check if at beginning or end
			checkScreen();
			actionReceiver.actionPerformed(this, currentScreen);
			return true;
		}
		return false;
	}
	
	public boolean previousScreen() {
		if (currentScreen > 0) { //make sure not at beginning
			hideCurrent();
			currentScreen--;
			showCurrent();
			
			//check if at beginning or end
			checkScreen();
			actionReceiver.actionPerformed(this, currentScreen);
			return true;
		}
		return false;
	}
	
	public boolean nextScreenStage() {
		if (screens.isNotEmpty()) {
			WindowScreen screen = screens.get(currentScreen);
			if (screen != null) {
				if (screen.getCurrentStage() < screen.getNumStages() - 1) {
					screen.nextStage();
					screen.onStageChanged();
					actionReceiver.actionPerformed(this, currentScreen);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean previousScreenStage() {
		WindowScreen screen = screens.get(currentScreen);
		if (screen != null) {
			if (screen.getCurrentStage() >= 1) {
				screen.prevStage();
				screen.onStageChanged();
				actionReceiver.actionPerformed(this, currentScreen);
				return true;
			}
		}
		return false;
	}
	
	public void showCurrent() { EUtil.nullDo(screens.get(currentScreen), s -> { s.showScreen(); s.onLoaded(); }); }
	public void hideCurrent() { EUtil.nullDo(screens.get(currentScreen), s -> { s.hideScreen(); s.onUnloaded(); }); }
	
	public void addScreen(WindowScreen... screensIn) { screens.add(screensIn); }
	
	//---------------------
	//ScreenHandler Getters
	//---------------------
	
	public WindowScreen getCurrentScreen() { return screens.get(currentScreen); }
	public int getCurrentScreenNum() { return currentScreen; }
	public int getCurrentStage() { return (screens.isNotEmpty()) ? screens.get(currentScreen).getCurrentStage() : -1; }
	public EArrayList<WindowScreen> getScreens() { return screens; }
	public IWindowObject getParent() { return parent; }
	
	public boolean atBeginning() { return atBeginning; }
	public boolean atEnd() { return atEnd; }
	
	//---------------------
	//ScreenHandler Setters
	//---------------------
	
	public ScreenHandler setCurrentScreen(int screenNum, int stageNum) {
		setCurrentScreen(screenNum);
		setCurrentStage(stageNum);
		return this;
	}
	
	public ScreenHandler setCurrentScreen(int num) {
		currentScreen = NumUtil.clamp(num, 0, screens.size() - 1);
		checkScreen();
		return this;
	}
	
	public ScreenHandler setCurrentStage(int num) {
		WindowScreen screen = screens.get(currentScreen);
		if (screen != null) {
			num = NumUtil.clamp(num, 0, screen.getNumStages());
			screen.setCurrentStage(num);
		}
		return this;
	}
	
	//------------------------------
	//ScreenHandler Internal Methods
	//------------------------------
	
	private void checkScreen() {
		WindowScreen s = screens.get(currentScreen);
		
		atBeginning = false;
		atEnd = false;
		
		if (currentScreen == screens.size() - 1) {
			atEnd = (s.getCurrentStage() == s.getNumStages() - 1);
		}
		else if (currentScreen == 0) {
			atBeginning = (s.getCurrentStage() == 0);
		}
	}
	
}
