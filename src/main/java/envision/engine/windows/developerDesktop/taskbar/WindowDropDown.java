package envision.engine.windows.developerDesktop.taskbar;

import envision.Envision;
import envision.engine.windows.windowObjects.advancedObjects.dropDownList.DropDownListEntry;
import envision.engine.windows.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import qot.assets.textures.window.WindowTextures;

public class WindowDropDown extends WindowDropDownList {

	//--------
	// Fields
	//--------
	
	private TaskBarButton parentButton;
	private DropDownListEntry last = null;
	private RightClickMenu rcm = null;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowDropDown(TaskBarButton taskButtonIn, double x, double y, double entryHeightIn, boolean useGlobalAction) {
		super(taskButtonIn, x, y, entryHeightIn, useGlobalAction);
		parentButton = taskButtonIn;
		setAlwaysOpen(true);
		setDrawTop(false);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
		
		if (rcm != null) return;
		
		var entry = getHoveringEntry(mXIn, mYIn);
		if (entry == null || entry == last) return;
		
		last = entry;
		var windows = Envision.getDeveloperDesktop().getAllActiveWindows();
		Envision.getDeveloperDesktop().revealHiddenObjects();
		
		for (var w : windows) {
			w.setDrawWhenMinimized(true);
		}
		
		if (entry.getEntryObject() instanceof IWindowParent p) {
			for (var w : windows) {
				if (w == p) continue;
				
				w.setHidden(true);
				w.setDrawWhenMinimized(false);
			}
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			openRCM(mXIn, mYIn);
			return;
		}
		
		if (button == 0) {
			DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
			if (entry == null) return;
			
			if (entry.getEntryObject() instanceof WindowParent p) {
				parentButton.performAction(p);
				parentButton.destroyList();
				
				if (rcm != null) {
					rcm.close();
					rcm = null;
				}
			}
		}
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (FocusType.MOUSE_PRESS.notEquals(eventIn.getFocusType())) return;
		
		if (eventIn.getActionCode() == 0) {
			DropDownListEntry entry = getHoveringEntry(eventIn.getMousePoint());
			if (entry != null && entry.getEntryObject() instanceof WindowParent p) {
				parentButton.destroyList();
				parentButton.performAction(p);
			}
		}
		else if (eventIn.getActionCode() == 1) {
			openRCM(eventIn.getMX(), eventIn.getMY());
		} //mouse 1
	}
		
	@Override
	public void mouseExited(int mXIn, int mYIn) {
	    if (rcm != null) return;
	    
	    if (!isMouseInside(mXIn, mYIn) && !parentButton.isMouseInside(mXIn, mYIn)) {
            parentButton.destroyList();
        }
        else {
            Envision.getDeveloperDesktop().revealHiddenObjects();
            
            for (var w : Envision.getDeveloperDesktop().getAllActiveWindows()) {
                w.setDrawWhenMinimized(false);
            }
            
            last = null;
        }
	}
	
	@Override
    public boolean isMouseOver() {
	    boolean m = isMouseInside();
	    var t = getTopParent();
        var h = t.getHighestZObjectUnderMouse();
        return m && this.equals(h);
    }
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == rcm && args.length > 0 && rcm.getGenericObject() instanceof WindowParent p) {
			switch ((String) args[0]) {
			case "Close":
				parentButton.destroyList();
				p.close();
				break;
			case "Maximize":
				parentButton.destroyList();
				p.setPreMax(p.getDimensions());
				p.setMaximized(ScreenLocation.CENTER);
				p.maximize();
				Envision.getDeveloperDesktop().setFocusedObject(p);
				p.bringToFront();
				break;
			case "Minimize":
				parentButton.destroyList();
				p.setMaximized(ScreenLocation.OUT);
				p.miniaturize();
				Envision.getDeveloperDesktop().setFocusedObject(p);
				p.bringToFront();
				break;
			case "Recenter":
				parentButton.destroyList();
				WindowHeader h = p.getHeader();
				TaskBar b = Envision.getDeveloperDesktop().getTaskBar();
				
				double hh = (h != null) ? h.height : 0;
				double bh = (b != null) ? b.height : 0;
				double oH = hh + bh;
				double maxW = ENumUtil.clamp(p.width, 0, res.width - 40);
				double maxH = ENumUtil.clamp(p.height, 0, res.height - 40 - oH);
				
				p.setGuiSize(maxW, maxH);
				p.centerObjectWithSize(maxW, maxH);
				p.setPosition(p.startX, p.startY + hh);
				p.reInitChildren();
				break;
			}
		}
	}
	
	@Override
	public void close() {
		super.close();
		if (rcm != null) {
			rcm.close();
			rcm = null;
		}
	}
	
	//------------------
	// Internal Methods
	//------------------

	private void openRCM(int mXIn, int mYIn) {
		if (rcm != null) {
			rcm.close();
			rcm = null;
		}
		
		DropDownListEntry<?> entry = getHoveringEntry(mXIn, mYIn);
		if (entry != null && entry.getEntryObject() instanceof WindowParent p) {
			int total = 0;
			if (p.isClosable()) total++;
			if (p.isMaximizable()) total++;
			if (p.isMoveable()) total++;
			
			if (total > 0) {
				rcm = new RightClickMenu() {
					@Override
					public void close() {
						super.close();
						rcm = null;
						parentButton.destroyList();
					}
				};
				
				rcm.setTitle(p.getObjectName());
				rcm.setActionReceiver(this);
				rcm.setGenericObject(p);
				
				if (p.isClosable()) rcm.addOption("Close", WindowTextures.close);
				if (p.isMaximizable()) {
					if (p.isMaximized()) rcm.addOption("Minimize", WindowTextures.min);
					else rcm.addOption("Maximize", WindowTextures.max);
				}
				if (p.isMoveable()) {
					rcm.addOption("Recenter", WindowTextures.move);
				}
			}
			
			Envision.getDeveloperDesktop().displayWindow(rcm, ObjectPosition.CURSOR_CORNER);
		}
	}
	
}
