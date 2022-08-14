package engine.windowLib.windowObjects.advancedObjects.screenHandler;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;

public class WindowScreen<E> {
	
	//--------
	// Fields
	//--------
	
	private EList<IWindowObject<?>> objects = new EArrayList<>();
	private int curStage = 0;
	private int numStages = 1;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowScreen() { this(1); }
	public WindowScreen(int numStagesIn) { numStages = numStagesIn; curStage = 0; }
	
	//---------
	// Methods
	//---------
	
	public void drawScreen(double mXIn, double mYIn) {}
	public void onLoaded() {}
	public void onUnloaded() {}
	public void onStageChanged() {}
	
	public void setCurrentStage(int num) { curStage = NumberUtil.clamp(num, 0, numStages); }
	public void nextStage() { curStage = NumberUtil.clamp(curStage + 1, 0, numStages); }
	public void prevStage() { curStage = NumberUtil.clamp(curStage - 1, 0, numStages); }
	
	public void showScreen() { objects.forEach(o -> EUtil.nullDo(o, i -> i.setVisible(true))); }
	public void hideScreen() { objects.forEach(o -> EUtil.nullDo(o, i -> i.setVisible(false))); }
	
	//---------
	// Getters
	//---------
	
	public EList<IWindowObject<?>> getObjects() { return objects; }
	public int getNumStages() { return numStages; }
	public int getCurrentStage() { return curStage; }
	
	//---------
	// Setters
	//---------
	
	public void setObjects(IWindowObject<?>... objectsIn) { objects.add(objectsIn); }
	
}
