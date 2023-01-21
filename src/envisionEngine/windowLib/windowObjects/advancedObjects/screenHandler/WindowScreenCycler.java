package envisionEngine.windowLib.windowObjects.advancedObjects.screenHandler;

import envisionEngine.windowLib.windowTypes.ActionObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;

public class WindowScreenCycler<E> extends ActionObject<E> {
	
	//--------
	// Fields
	//--------
	
	private IWindowObject<?> parent;
	private EArrayList<WindowScreen<E>> screens = new EArrayList();
	private int currentScreen = 0;
	private boolean atBeginning = false;
	private boolean atEnd = false;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowScreenCycler(IWindowObject<?> parentIn) { this(parentIn, (WindowScreen<E>[]) null); }
	public WindowScreenCycler(IWindowObject<?> parentIn, WindowScreen<E>... screensIn) {
		super(parentIn);
		if (screensIn != null) screens.add(screensIn);
		atBeginning = true;
		actionReceiver = parentIn;
	}
	
	//---------
	// Methods
	//---------
	
	public void drawCurrentScreen(double mXIn, double mYIn) {
		if (currentScreen >= 0 && currentScreen < screens.size()) {
			EUtil.nullDo(screens.get(currentScreen), s -> s.drawScreen(mXIn, mYIn));
		}
	}
	
	public void handleNext() { if (!nextScreenStage()) nextScreen(); }
	public void handlePrevious() { if (!previousScreenStage()) previousScreen(); }
	
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
			var screen = screens.get(currentScreen);
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
		var screen = screens.get(currentScreen);
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
	
	public void addScreen(WindowScreen<E>... screensIn) { screens.add(screensIn); }
	
	//---------
	// Getters
	//---------
	
	public WindowScreen<E> getCurrentScreen() { return screens.get(currentScreen); }
	public int getCurrentScreenNum() { return currentScreen; }
	public int getCurrentStage() { return (screens.isNotEmpty()) ? screens.get(currentScreen).getCurrentStage() : -1; }
	public EArrayList<WindowScreen<E>> getScreens() { return screens; }
	public IWindowObject<?> getParent() { return parent; }
	
	public boolean atBeginning() { return atBeginning; }
	public boolean atEnd() { return atEnd; }
	
	//---------
	// Setters
	//---------
	
	public void setCurrentScreen(int screenNum, int stageNum) {
		setCurrentScreen(screenNum);
		setCurrentStage(stageNum);
	}
	
	public void setCurrentScreen(int num) {
		currentScreen = ENumUtil.clamp(num, 0, screens.size() - 1);
		checkScreen();
	}
	
	public void setCurrentStage(int num) {
		WindowScreen<E> screen = screens.get(currentScreen);
		if (screen != null) {
			num = ENumUtil.clamp(num, 0, screen.getNumStages());
			screen.setCurrentStage(num);
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkScreen() {
		var s = screens.get(currentScreen);
		
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
