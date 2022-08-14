package engine.windowLib.windowObjects.actionObjects.stageButton;

import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

//Author: Hunter Bragg

/**
 * Similar to a standard WindowButton except that when pressed will cycle
 * between separate stages.
 * 
 * @author Hunter Bragg
 */
public class WindowButtonStaged<E> extends WindowButton<E> {

	//--------
	// Fields
	//--------
	
	protected EArrayList<ButtonStage> stages = new EArrayList();
	protected int curStage = 0;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowButtonStaged(IWindowObject<?> parentIn, double posX, double posY, double width, double height) {
		super(parentIn, posX, posY, width, height);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (isEnabled() && willBeDrawn()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(button); }
			else if (button == 0 || (button == 1 && acceptRightClicks)) {
				playPressSound();
				curStage = (curStage >= stages.size()) ? 0 : curStage + 1;
				loadStage(curStage);
				performAction(curStage);
			}
		}
	}
	
	//---------
	// Methods
	//---------
	
	public void loadStage(int stageNum) {
		curStage = NumberUtil.clamp(stageNum, 0, stages.size() - 1);
		
		// If there aren't any stages, apply default values
		if (stages.isEmpty()) {
			setString("undef");
			setStringColor(EColors.lgray.intVal);
			return;
		}
		
		ButtonStage stage = stages.get(curStage);
		setString(stage.getName());
		setStringColor(stage.getColor());
	}
	
	public void addStage(String nameIn) { addStage(nameIn, EColors.lgray.intVal); }
	public void addStage(String nameIn, EColors colorIn) { addStage(nameIn, colorIn.intVal); }
	public void addStage(String nameIn, int colorIn) {
		stages.add(new ButtonStage(nameIn, colorIn));
	}
	
	public void removeStage(int stageNum) {
		if (stageNum < 0 || stageNum >= stages.size()) return;
		stages.remove(stageNum);
		loadStage(0);
	}
	
	//---------
	// Getters
	//---------
	
	public int getCurStage() {
		return curStage;
	}
	
	public String getCurStageString() {
		return (!stages.isEmpty()) ? stages.get(curStage).getName() : "undef";
	}
	
	public int getCurStageColor() {
		return (!stages.isEmpty()) ? stages.get(curStage).getColor() : EColors.lgray.intVal;
	}
	
	/**
	 * Does not check for IndexOutOfBounds!
	 * 
	 * @param stageNum
	 * @return
	 */
	public String getStageString(int stageNum) {
		return stages.get(stageNum).getName();
	}
	
	/**
	 * Does not check for IndexOutOfBounds!
	 * 
	 * @param stageNum
	 * @return
	 */
	public int getStageColor(int stageNum) {
		return stages.get(stageNum).getColor();
	}
	
	//---------
	// Setters
	//---------
	
	public void setEveryString(String nameIn) {
		stages.forEach(s -> s.setName(nameIn));
	}
	
	public void setEveryColor(EColors colorIn) {
		setEveryColor(colorIn.intVal);
	}
	
	public void setEveryColor(int colorIn) {
		stages.forEach(s -> s.setColor(colorIn));
	}
	
	public void setCurStageValues(String newName, int colorIn) {
		if (stages.isEmpty()) return;
		stages.get(curStage).setValues(newName, colorIn);
	}
	
	public void setStageValues(int stageNum, String newName, int colorIn) {
		if (stageNum < 0 || stageNum >= stages.size()) return;
		stages.get(stageNum).setValues(newName, colorIn);
	}
	
}
