package envision.engine.windows.windowObjects.advancedObjects.colorPicker;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.basicObjects.WindowContainer;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowObjects.basicObjects.WindowRect;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox.DialogBoxTypes;
import envision.engine.windows.windowTypes.ActionWindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;

//Author: Hunter Bragg

public class ColorPickerSimple extends ActionWindowParent {
	
	//--------
	// Fields
	//--------
	
	//color buttons
	private ColorPickerButton<?> lred, red, maroon, brown, dorange, borange, orange, lorange, yellow;
	private ColorPickerButton<?> lime, green, lgreen, dgreen, seafoam, turquoise, aquamarine;
	private ColorPickerButton<?> cyan, skyblue, blue, regal, grape, navy, violet, eggplant, purple, magenta, pink, hotpink;
	private ColorPickerButton<?> white, chalk, lgray, gray, mgray, dgray, pdgray, lsteel, steel, dsteel, vdgray, black;
	
	//functional objects
	private WindowTextField<?> inputField;
	private WindowButton<?> select, back;
	private WindowButton<?> advanced;
	private WindowRect<?> colorDisplay;
	private WindowLabel<?> colorLabel, inputLabel;
	
	//the current color
	private int currentColor = 0xffffffff;
	
	//--------------
	// Constructors
	//--------------
	
	public ColorPickerSimple(IWindowObject<?> parentIn) { this(parentIn, 0xffffffff); }
	public ColorPickerSimple(IWindowObject<?> parentIn, int colorIn) {
		super(parentIn);
		currentColor = colorIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setObjectName("Color Picker");
		setSize(407, 374);
		setPinnable(false);
	}
	
	@Override
	public void initChildren() {
		//initialize header
		defaultHeader(this);
		
		//create color display objects
		colorDisplay = new WindowRect(this, midX - 25, startY + 47, midX + 25, startY + 97, currentColor);
		colorLabel = new WindowLabel(this, midX, startY + 12, "", EColors.lime);
				
		colorLabel.setDrawCentered(true);
				
		updateValues();
		
		//create color buttons
		WindowContainer<?> colorContainer = new WindowContainer(this, startX + 5, colorDisplay.endY + 16, width - 10, 100);
		colorContainer.setTitle("Color Palette");
		colorContainer.setTitleColor(EColors.lgray.intVal);
		colorContainer.setBackgroundColor(0xff383838);
		colorContainer.setDrawTitle(false);
		
		double y = colorContainer.startY + 7;
		double w = 23;
		double h = 27;
		
		lred = new ColorPickerButton(this, colorContainer.startX + 7, y, w, h, EColors.lred);
		red = new ColorPickerButton(this, lred.endX + 1, y, w, h, EColors.red);
		maroon = new ColorPickerButton(this, red.endX + 1, y, w, h, EColors.maroon);
		brown = new ColorPickerButton(this, maroon.endX + 1, y, w, h, EColors.brown);
		dorange = new ColorPickerButton(this, brown.endX + 1, y, w, h, EColors.dorange);
		borange = new ColorPickerButton(this, dorange.endX + 1, y, w, h, EColors.borange);
		orange = new ColorPickerButton(this, borange.endX + 1, y, w, h, EColors.orange);
		lorange = new ColorPickerButton(this, orange.endX + 1, y, w, h, EColors.lorange);
		yellow = new ColorPickerButton(this, lorange.endX + 1, y, w, h, EColors.yellow);
		lime = new ColorPickerButton(this, yellow.endX + 1, y, w, h, EColors.lime);
		lgreen = new ColorPickerButton(this, lime.endX + 1, y, w, h, EColors.lgreen);
		seafoam = new ColorPickerButton(this, lgreen.endX + 1, y, w, h, EColors.seafoam);
		green = new ColorPickerButton(this, seafoam.endX + 1, y, w, h, EColors.green);
		dgreen = new ColorPickerButton(this, green.endX + 1, y, w, h, EColors.dgreen);
		turquoise = new ColorPickerButton(this, dgreen.endX + 1, y, w, h, EColors.turquoise);
		aquamarine = new ColorPickerButton(this, turquoise.endX + 1, y, w, h, EColors.aquamarine);
		
		double y1 = lred.endY + 3;
		
		cyan = new ColorPickerButton(this, maroon.startX, y1, w, h, EColors.cyan);
		skyblue = new ColorPickerButton(this, cyan.endX + 1, y1, w, h, EColors.skyblue);
		blue = new ColorPickerButton(this, skyblue.endX + 1, y1, w, h, EColors.blue);
		regal = new ColorPickerButton(this, blue.endX + 1, y1, w, h, EColors.regal);
		grape = new ColorPickerButton(this, regal.endX + 1, y1, w, h, EColors.grape);
		navy = new ColorPickerButton(this, grape.endX + 1, y1, w, h, EColors.navy);
		violet = new ColorPickerButton(this, navy.endX + 1, y1, w, h, EColors.violet);
		eggplant = new ColorPickerButton(this, violet.endX + 1, y1, w, h, EColors.eggplant);
		purple = new ColorPickerButton(this, eggplant.endX + 1, y1, w, h, EColors.purple);
		pink = new ColorPickerButton(this, purple.endX + 1, y1, w, h, EColors.pink);
		hotpink = new ColorPickerButton(this, pink.endX + 1, y1, w, h, EColors.hotpink);
		magenta = new ColorPickerButton(this, hotpink.endX + 1, y1, w, h, EColors.magenta);
		
		double y2 = navy.endY + 9;
		
		white = new ColorPickerButton(this, maroon.startX, y2, w, h, EColors.white);
		chalk = new ColorPickerButton(this, white.endX + 1, y2, w, h, EColors.chalk);
		lgray = new ColorPickerButton(this, chalk.endX + 1, y2, w, h, EColors.lgray);
		gray = new ColorPickerButton(this, lgray.endX + 1, y2, w, h, EColors.gray);
		mgray = new ColorPickerButton(this, gray.endX + 1, y2, w, h, EColors.mgray);
		dgray = new ColorPickerButton(this, mgray.endX + 1, y2, w, h, EColors.dgray);
		pdgray = new ColorPickerButton(this, dgray.endX + 1, y2, w, h, EColors.pdgray);
		lsteel = new ColorPickerButton(this, pdgray.endX + 1, y2, w, h, EColors.lsteel);
		steel = new ColorPickerButton(this, lsteel.endX + 1, y2, w, h, EColors.steel);
		dsteel = new ColorPickerButton(this, steel.endX + 1, y2, w, h, EColors.dsteel);
		vdgray = new ColorPickerButton(this, dsteel.endX + 1, y2, w, h, EColors.vdgray);
		black = new ColorPickerButton(this, vdgray.endX + 1, y2, w, h, EColors.black);
		
		//create manual color input field & label
		WindowContainer<?> inputContainer = new WindowContainer(this, startX + 5, colorContainer.endY + 6, width - 10, 107);
		inputContainer.setTitle("Hex Color Code");
		inputContainer.setTitleColor(EColors.lgray.intVal);
		inputContainer.setBackgroundColor(0xff383838);
		inputContainer.setHoverText("The current color value in (RGB) hexadecimal format");
		
		inputLabel = new WindowLabel(this, inputContainer.startX + 16, inputContainer.startY + 37, "(ARGB) color value");
		inputField = new WindowTextField(this, inputContainer.startX + 16, inputContainer.startY + 66, 240, 28);
		
		inputField.setMaxStringLength(10);
		//inputLabel.setHoverText("The current color value in (RGB) hexadecimal format");
		inputLabel.setColor(EColors.seafoam);
		
		//create select and back buttons
		select = new WindowButton(this, inputContainer.midX - 10 - 150, inputContainer.endY + 10, 150, 28, "Confirm");
		back = new WindowButton(this, inputContainer.midX + 10, inputContainer.endY + 10, 150, 28, "Back");
		
		back.setStringColor(EColors.yellow);
		select.setStringColor(EColors.lgreen);
		
		//add containers
		addObject(inputContainer);
		
		//add color buttons
		colorContainer.addObject(lred, red, maroon, brown, dorange, borange, orange, lorange, yellow);
		colorContainer.addObject(lime, green, lgreen, dgreen, seafoam, turquoise, aquamarine);
		colorContainer.addObject(cyan, skyblue, blue, regal, navy, grape, violet, eggplant, purple, magenta, pink, hotpink);
		colorContainer.addObject(white, chalk, lgray, gray, mgray, dgray, pdgray, lsteel, steel, dsteel, vdgray, black);
		
		//add functional objects
		inputContainer.addObject(inputField, inputLabel);
		
		addObject(select, back);
		addObject(colorContainer, colorDisplay, colorLabel);
		
		updateValues();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		//drawRect(0xff3b3b3b, 1);
		
		if (colorDisplay != null) {
			Dimension_d cDim = colorDisplay.getDimensions();
			drawRect(cDim.startX - 1, cDim.startY - 1, cDim.endX + 1, cDim.endY + 1, 0xff000000);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof ColorPickerButton b) {
			if (args.length > 0 && args[0] instanceof String val) {
				if (val != null && val.equals("dc")) {
					performAction(b.getColor());
					close();
				}
			}
			else {
				currentColor = b.getColor();
				updateValues();
			}
		}
		else {
			if (object == inputField) parseInputColor();
			if (object == select) { performAction(currentColor); close(); }
			if (object == back) close();
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void parseInputColor() {
		try {
			String text = inputField.getText();
			if (text.length() > 6) {
				if (text.startsWith("0x")) text = text.substring(2);
				String alpha = text.substring(0, 2);
				currentColor = (int) Long.parseLong(alpha + text.substring(2), 16);
			}
			else {
				currentColor = 0xff000000 + Integer.parseInt(text, 16);
			}
			updateValues();
		}
		catch (Exception e) {
			e.printStackTrace();
			WindowDialogueBox error = new WindowDialogueBox(this, DialogBoxTypes.OK);
			error.setTitle("Error!");
			error.setTitleColor(EColors.lred.c());
			error.setMessage("Cannot parse the value: " + inputField.getText());
			error.setMessageColor(EColors.lgray.c());
			getTopParent().displayWindow(error, ObjectPosition.SCREEN_CENTER);
			inputField.clear();
			inputField.setText("0x" + inputField.text);
		}
	}
	
	private void updateValues() {
		colorDisplay.setColor(currentColor);
		EColors c = EColors.byIntVal(currentColor);
		colorDisplay.setHoverText(c != null ? c.name : "" + currentColor);
		colorDisplay.setHoverTextColor(EColors.seafoam.intVal);
		String cs = "" + (c != null ? c.name : "0x" + Integer.toHexString(currentColor));
		colorLabel.setString(cs);
		try {
			String val = "";
			if (inputField != null && inputField.getText() != null) {
				if (inputField.getText().length() < 10) {
					
				}
				//if (inputField.getText().length() > 6) {
					val = String.format("%8x", currentColor);
				//}
				//else {
					//val = String.format("%6x", currentColor);
				//}
				inputField.setTextWhenEmpty("0x" + val);
				inputField.setText("0x" + val);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
