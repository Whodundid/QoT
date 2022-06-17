package engine.windowLib.windowObjects.actionObjects.stageButton;

import eutil.colors.EColors;

public class ButtonStage {

	//--------
	// Fields
	//--------
	
	String stageName;
	int stageColor;
	
	//--------------
	// Constructors
	//--------------
	
	ButtonStage(String nameIn) { this(nameIn, EColors.lgray.intVal); }
	ButtonStage(String nameIn, EColors colorIn) { this(nameIn, colorIn.intVal); }
	ButtonStage(String nameIn, int colorIn) {
		stageName = nameIn;
		stageColor = colorIn;
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return stageName; }
	public int getColor() { return stageColor; }
	
	//---------
	// Setters
	//---------
	
	public void setValues(String nameIn, EColors colorIn) { setValues(nameIn, colorIn.intVal); }
	public void setValues(String nameIn, int colorIn) {
		stageName = nameIn;
		stageColor = colorIn;
	}
	
	public void setName(String nameIn) { stageName = nameIn; }
	public void setColor(EColors colorIn) { stageColor = colorIn.intVal; }
	public void setColor(int colorIn) { stageColor = colorIn; }
	
}
