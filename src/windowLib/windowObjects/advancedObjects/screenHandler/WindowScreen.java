package windowLib.windowObjects.advancedObjects.screenHandler;

import eutil.EUtil;
import eutil.math.NumberUtil;
import eutil.storage.EArrayList;
import windowLib.windowTypes.interfaces.IWindowObject;

public class WindowScreen<E> {
	
	private EArrayList<IWindowObject<?>> objects = new EArrayList();
	private int curStage = 0;
	private int numStages = 1;
	
	//-------------------------
	//WindowScreen Constructors
	//-------------------------
	
	public WindowScreen() { this(1); }
	public WindowScreen(int numStagesIn) { numStages = numStagesIn; curStage = 0; }
	
	//--------------------
	//WindowScreen Methods
	//--------------------
	
	public void drawScreen(double mXIn, double mYIn) {}
	public void onLoaded() {}
	public void onUnloaded() {}
	public void onStageChanged() {}
	
	public void setCurrentStage(int num) { curStage = NumberUtil.clamp(num, 0, numStages); }
	public void nextStage() { curStage = NumberUtil.clamp(curStage + 1, 0, numStages); }
	public void prevStage() { curStage = NumberUtil.clamp(curStage - 1, 0, numStages); }
	
	public void showScreen() { objects.forEach(o -> EUtil.nullDo(o, i -> i.setVisible(true))); }
	public void hideScreen() { objects.forEach(o -> EUtil.nullDo(o, i -> i.setVisible(false))); }
	
	//--------------------
	//WindowScreen Getters
	//--------------------
	
	public EArrayList<IWindowObject<?>> getObjects() { return objects; }
	public int getNumStages() { return numStages; }
	public int getCurrentStage() { return curStage; }
	
	//--------------------
	//WindowScreen Setters
	//--------------------
	
	public WindowScreen setObjects(IWindowObject<?>... objectsIn) {
		objects.add(objectsIn);
		return this;
	}
	
}
