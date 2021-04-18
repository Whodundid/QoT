package mapEditor.editorParts.sidePanel.toolPanels.regionTool;

import gameSystems.mapSystem.Region;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowObjects.actionObjects.WindowTextField;
import windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import windowLib.windowObjects.advancedObjects.textArea.TextAreaLine;
import windowLib.windowObjects.utilityObjects.ColorButton;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IActionObject;

public class RegionEditWindow extends WindowParent {
	
	TextAreaLine line;
	Region region;
	
	WindowButton rename, setColor, confirm, cancel;
	WindowTextField entryBox;
	ColorButton ownColor;
	
	String editName;
	int editColor;
	
	public RegionEditWindow(TextAreaLine<Region> lineIn) {
		line = lineIn;
		region = lineIn.getGenericObject();
		
		editName = region.getName();
		editColor = region.getColor();
	}
	
	@Override
	public void initWindow() {
		setObjectName("Edit Region");
		setDimensions(400, 400);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		//setMessage("Enter a new name for the region");
		
		double bw = NumberUtil.clamp((width - 10) / 3, 100, 200);
		double g = width / 30;
		
		double w = width / 1.5;
		entryBox = new WindowTextField(this, midX - w / 2, midY - 3, w, 28);
		ownColor = new ColorButton(this, endX - 100, startY + 6, 20, 20);
		
		confirm = new WindowButton(this, midX - g - bw, endY - 50, bw, 28, "Confirm").setStringColor(EColors.white);
		cancel = new WindowButton(this, midX + g, endY - 50, bw, 28, "Cancel").setStringColor(EColors.white);
		
		//assign values
		entryBox.setText(editName);
		entryBox.setTextWhenEmpty("region name");
		
		ownColor.setHoverText("Click to change");
		ownColor.setColor(editColor);
		
		
		addObject(entryBox);
		addObject(ownColor);
		addObject(confirm, cancel);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == entryBox || object == rename) { rename(); }
		else if (object == setColor || object == ownColor) { setColor(); }
		else if (object instanceof ColorPickerSimple && args.length == 1 && args[0] instanceof Integer) { recolor((int) args[0]); }
		else if (object == confirm) { confirm(); }
		else if (object == cancel) { close(); }
	}
	
	//--------------------------------------------------
	
	private void rename() {
		String text = entryBox.getText().trim();
		if (!text.isEmpty()) {
			editName = entryBox.getText();
		}
		else {
			entryBox.drawFocusLockBorder();
		}
	}
	
	private void setColor() {
		ColorPickerSimple cp = new ColorPickerSimple(this, editColor);
		getTopParent().displayWindow(cp);
		cp.getHeader().setTitle("Select Region Color");
	}
	
	private void recolor(int color) {
		editColor = color;
		ownColor.setColor(editColor);
	}
	
	/** Called to actually apply the changes to the region. */
	private void confirm() {
		region.setName(editName);
		region.setColor(editColor);
		line.setTextColor(editColor);
		close();
	}
	
}
