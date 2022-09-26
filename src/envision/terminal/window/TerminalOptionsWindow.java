package envision.terminal.window;

import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.advancedObjects.WindowScrollList;
import envision.windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import envision.windowLib.windowObjects.basicObjects.WindowLabel;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.misc.ScreenLocation;

public class TerminalOptionsWindow extends WindowParent {

	WindowScrollList settings;
	WindowButton drawLineNumbers, backColor, maxLines;
	
	public TerminalOptionsWindow() {
		aliases.add("termoptions", "toptions");
	}
	
	@Override
	public void initWindow() {
		setSize(190, 110);
		setMinDims(75, 75);
		setResizeable(true);
		setObjectName("Terminal Settings");
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		settings = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		settings.setBackgroundColor(0xff303030);
		
		//Visual label
		WindowLabel visual = new WindowLabel(settings, startX + 8, startY + 10, "Visual", EColors.orange);
		
		settings.addObjectToList(false, visual);
		
		//buttons
		//drawLineNumbers = new WindowButton(settings, startX + 12, visual.endY + 8, 60, 20, CoreApp.termLineNumbers);
		backColor = new WindowButton(settings, startX + 13, drawLineNumbers.endY + 10, 20, 20) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawHRect(backColor.startX - 1, backColor.startY - 1, backColor.endX + 1, backColor.endY + 1, 1, EColors.black);
				drawHRect(backColor.startX, backColor.startY, backColor.endX, backColor.endY, 1, EColors.lgray);
				drawHRect(backColor.startX + 1, backColor.startY + 1, backColor.endX - 1, backColor.endY - 1, 1, EColors.black);
			}
		};
		
		backColor.setDrawBackground(true);
		//backColor.setBackgroundColor(CoreApp.termBackground.get());
		backColor.setTextures(null, null);
		
		IActionObject.setActionReceiver(this, drawLineNumbers, backColor);
		
		//labels
		WindowLabel numberLabel = new WindowLabel(settings, drawLineNumbers.endX + 10, drawLineNumbers.midY - 4, "Draw line numbers", EColors.lgray);
		WindowLabel background = new WindowLabel(settings, backColor.endX + 10, backColor.midY - 4, "Terminal background color", EColors.lgray);
		
		numberLabel.setHoverText("Displays the current line number in termainls");
		background.setHoverText("Sets the background color in terminals");
		
		//add to list
		settings.addObjectToList(false, drawLineNumbers, backColor);
		settings.addObjectToList(false, numberLabel, background);
		
		settings.fitItemsInList();
		
		addObject(settings);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		//System.out.println(this.getDimensions());
	}
	
	@Override
	public void sendArgs(Object... args) {
		
	}
	
	@Override
	public void resize(double xIn, double yIn, ScreenLocation areaIn) {
		try {
			if (xIn != 0 || yIn != 0) {
				double vPos = settings.getVScrollBar().getScrollPos();
				double hPos = settings.getHScrollBar().getScrollPos();
				super.resize(xIn, yIn, areaIn);
				settings.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
				settings.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == drawLineNumbers) { lineNumbers(); }
		if (object == backColor) { changeColor(); }
		
		if (object instanceof ColorPickerSimple) {
			if (args.length > 0) {
				try {
					int val = (int) args[0];
					//CoreApp.termBackground.set(val);
					backColor.setBackgroundColor(val);
					//CoreApp.instance().getConfig().saveMainConfig();
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
	
	@Override public boolean isDebugWindow() { return true; }
	
	private void lineNumbers() {
		//CoreApp.termLineNumbers.set(!CoreApp.termLineNumbers.get());
		//drawLineNumbers.toggleTrueFalse(CoreApp.termLineNumbers, CoreApp.instance(), true);
		
		//EnhancedMC.reloadAllWindowInstances(ETerminal.class);
	}
	
	private void changeColor() {
		//EnhancedMC.displayWindow(new ColorPickerSimple(this));
	}
	
}
