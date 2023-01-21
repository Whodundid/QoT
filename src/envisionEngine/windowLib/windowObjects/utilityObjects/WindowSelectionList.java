package envisionEngine.windowLib.windowObjects.utilityObjects;

import envisionEngine.inputHandlers.Keyboard;
import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;
import envisionEngine.windowLib.windowObjects.advancedObjects.textArea.TextAreaLine;
import envisionEngine.windowLib.windowObjects.advancedObjects.textArea.WindowTextArea;
import envisionEngine.windowLib.windowTypes.ActionWindowParent;
import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.Box3;
import eutil.datatypes.EArrayList;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class WindowSelectionList extends ActionWindowParent {
	
	//--------
	// Fields
	//--------
	
	protected WindowButton select, cancel;
	protected WindowTextArea list;
	protected EArrayList<Box3<String, Integer, Object>> toAdd = new EArrayList<>();
	protected IWindowObject<?> actionReciever;
	
	private double vPos, hPos;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowSelectionList(IWindowObject<?> parent) {
		super(parent);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setObjectName("Make A Selection..");
		setSize(200, 230);
		setMinDims(100, 100);
		setResizeable(true);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		select = new WindowButton(this, endX - 85, endY - 25, 80, 20, "Select");
		cancel = new WindowButton(this, startX + 5, endY - 25, 80, 20, "Cancel");
		
		list = new WindowTextArea(this, startX + 5, startY + 5, width - 10, height - 35, false) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					selectCurrentOptionAndClose();
				}
			}
		};
		list.setResetDrawn(false);
		
		var it = toAdd.iterator();
		while (it.hasNext()) {
			var b = it.next();
			add(b.a, b.b, b.c);
			it.remove();
		}
		if (!list.getTextDocument().isEmpty()) list.setSelectedLine(list.getTextLine(0));
		
		addObject(select, cancel, list);
	}
	
	@Override
	public void preReInit() {
		vPos = list.getVScrollBar().getScrollPos();
		hPos = list.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		list.getVScrollBar().setScrollPos(vPos);
		list.getHScrollBar().setScrollPos(hPos);
	}
	
	@Override
	public void resize(double xIn, double yIn, ScreenLocation areaIn) {
		if (xIn != 0 || yIn != 0) {
			double vPos = list.getVScrollBar().getScrollPos();
			double hPos = list.getHScrollBar().getScrollPos();
			
			super.resize(xIn, yIn, areaIn);
			
			list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
			list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (select != null && list != null) {
			select.setEnabled(list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ENTER) selectCurrentOptionAndClose();
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == select) selectCurrentOptionAndClose();
		if (object == cancel) close();
	}
	
	//---------
	// Methods
	//---------
	
	public void writeLine() { writeLine("", 0); }
	public void writeLine(String text) { writeLine(text, EColors.white.intVal); }
	public void writeLine(String text, EColors color) { writeLine(text, color.intVal); }
	public void writeLine(String text, int color) {
		if (list != null) list.addTextLine(text, color);
		else toAdd.add(new Box3(text, color, null));
	}
	
	public void addOption(String text) { addOption(text, EColors.green.intVal, text); }
	public void addOption(String text, Object arg) { addOption(text, EColors.green.intVal, arg); }
	public void addOption(String text, EColors color, Object arg) { addOption(text, color.intVal, arg); }
	public void addOption(String text, int color, Object arg) {
		if (list != null) add(text, color, arg);
		else toAdd.add(new Box3(text, color, arg));
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void selectCurrentOptionAndClose() {
		if (list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null) {
			setGenericObject(list.getCurrentLine().getGenericObject());
			performAction(null, null);
			close();
		}
	}
	
	private void add(String text, int color, Object arg) {
		var l = new TextAreaLine(list, text, color, arg) {
			@Override
			public void onDoubleClick() {
				if (list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null) {
					selectCurrentOptionAndClose();
				}
			}
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == Keyboard.KEY_ENTER) {
					selectCurrentOptionAndClose();
				}
			}
		};
		list.addTextLine(l);
	}
	
}
