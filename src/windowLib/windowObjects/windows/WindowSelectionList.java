package windowLib.windowObjects.windows;

import java.util.Iterator;
import renderUtil.EColors;
import renderUtil.ScreenLocation;
import storageUtil.EArrayList;
import storageUtil.TrippleBox;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowObjects.advancedObjects.textArea.TextAreaLine;
import windowLib.windowObjects.advancedObjects.textArea.WindowTextArea;
import windowLib.windowTypes.ActionWindowParent;
import windowLib.windowTypes.interfaces.IActionObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowSelectionList extends ActionWindowParent {
	
	protected WindowButton select, cancel;
	protected WindowTextArea list;
	protected EArrayList<TrippleBox<String, Integer, Object>> toAdd = new EArrayList();
	protected IWindowObject actionReciever;
	
	private double vPos, hPos;
	
	//------------------------------
	//EGuiSelectionList Constructors
	//------------------------------
	
	public WindowSelectionList(IWindowObject parent) {
		super(parent);
	}
	
	//---------------------------
	//EnhancedGuiObject Overrides
	//---------------------------
	
	@Override
	public void initWindow() {
		setObjectName("Make A Selection..");
		setDimensions(200, 230);
		setMinDims(100, 100);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
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
		
		Iterator<TrippleBox<String, Integer, Object>> it = toAdd.iterator();
		while (it.hasNext()) {
			TrippleBox<String, Integer, Object> b = it.next();
			add(b.a, b.b, b.c);
			it.remove();
		}
		if (!list.getTextDocument().isEmpty()) { list.setSelectedLine(list.getTextLine(0)); }
		
		addObject(select, cancel, list);
	}
	
	@Override
	public void preReInit() {
		vPos = list.getVScrollBar().getScrollPos();
		hPos = list.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		list.getVScrollBar().setScrollBarPos(vPos);
		list.getHScrollBar().setScrollBarPos(hPos);
	}
	
	@Override
	public WindowSelectionList resize(double xIn, double yIn, ScreenLocation areaIn) {
		if (xIn != 0 || yIn != 0) {
			double vPos = list.getVScrollBar().getScrollPos();
			double hPos = list.getHScrollBar().getScrollPos();
			
			super.resize(xIn, yIn, areaIn);
			
			list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
			list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
		}
		return this;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (select != null && list != null) {
			select.setEnabled(list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) { selectCurrentOptionAndClose(); }
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == select) { selectCurrentOptionAndClose(); }
		if (object == cancel) { close(); }
	}
	
	//-------------------------
	//EGuiSelectionList Methods
	//-------------------------
	
	public WindowSelectionList writeLine() { return writeLine("", 0); }
	public WindowSelectionList writeLine(String text) { return writeLine(text, EColors.white.intVal); }
	public WindowSelectionList writeLine(String text, EColors color) { return writeLine(text, color.intVal); }
	public WindowSelectionList writeLine(String text, int color) {
		if (list != null) { list.addTextLine(text, color); }
		else { toAdd.add(new TrippleBox(text, color, null)); }
		return this;
	}
	
	public WindowSelectionList addOption(String text) { return addOption(text, EColors.green.intVal, text); }
	public WindowSelectionList addOption(String text, Object arg) { return addOption(text, EColors.green.intVal, arg); }
	public WindowSelectionList addOption(String text, EColors color, Object arg) { return addOption(text, color.intVal, arg); }
	public WindowSelectionList addOption(String text, int color, Object arg) {
		if (list != null) { add(text, color, arg); }
		else { toAdd.add(new TrippleBox(text, color, arg)); }
		return this;
	}
	
	protected void selectCurrentOptionAndClose() {
		if (list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null) {
			genericObject = list.getCurrentLine().getGenericObject();
			performAction(null, null);
			close();
		}
	}
	
	private void add(String text, int color, Object arg) {
		TextAreaLine l = new TextAreaLine(list, text, color, arg) {
			@Override
			public void onDoubleClick() {
				if (list.getCurrentLine() != null && list.getCurrentLine().getGenericObject() != null) {
					selectCurrentOptionAndClose();
				}
			}
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					selectCurrentOptionAndClose();
				}
			}
		};
		list.addTextLine(l);
	}
	
}
