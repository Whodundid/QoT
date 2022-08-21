package envision.topOverlay.desktopOverlay;

import envision.windowLib.windowObjects.advancedObjects.dropDownList.DropDownListEntry;
import envision.windowLib.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import envision.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import envision.windowLib.windowObjects.utilityObjects.RightClickMenu;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.ObjectPosition;
import envision.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import envision.windowLib.windowUtil.windowEvents.events.EventFocus;
import eutil.math.NumberUtil;
import eutil.misc.ScreenLocation;
import game.QoT;
import game.assets.textures.window.WindowTextures;

public class WindowDropDown extends WindowDropDownList {

	//--------
	// Fields
	//--------
	
	private TaskBarButton<?> parentButton;
	private DropDownListEntry<?> last = null;
	private RightClickMenu rcm = null;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowDropDown(TaskBarButton<?> taskButtonIn, double x, double y, double entryHeightIn, boolean useGlobalAction) {
		super(taskButtonIn, x, y, entryHeightIn, useGlobalAction);
		parentButton = taskButtonIn;
		setAlwaysOpen(true);
		setDrawTop(false);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		if (rcm == null) {
			var entry = getHoveringEntry(mXIn, mYIn);
			if (entry != null) {
				if (entry != last) {
					last = entry;
					var windows = QoT.getTopRenderer().getAllActiveWindows();
					QoT.getTopRenderer().revealHiddenObjects();
					
					for (var w : windows) {
						w.setDrawWhenMinimized(true);
					}
					
					if (entry.getEntryObject() instanceof IWindowParent<?> p) {
						for (var w : windows) {
							if (w != p) {
								w.setHidden(true);
								w.setDrawWhenMinimized(false);
							}
						}
					}
				} //if entry != last
			}
		} //rcm == null
		
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
			if (entry != null) {
				if (entry.getEntryObject() instanceof WindowParent) {
					WindowParent p = (WindowParent) entry.getEntryObject();
					parentButton.performAction(p);
					parentButton.destroyList();
					if (rcm != null) { rcm.close(); rcm = null; }
				}
			}
		}
		else if (button == 1) {
			openRCM(mXIn, mYIn);
		}
		
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MOUSE_PRESS)) {
			if (eventIn.getActionCode() == 0) {
				DropDownListEntry entry = getHoveringEntry(eventIn.getMX(), eventIn.getMY());
				if (entry != null) {
					if (entry.getEntryObject() instanceof WindowParent) {
						WindowParent p = (WindowParent) entry.getEntryObject();
						parentButton.destroyList();
						parentButton.performAction(p);
					}
				}
			}
			else if (eventIn.getActionCode() == 1) {
				openRCM(eventIn.getMX(), eventIn.getMY());
			} //mouse 1
		}
	}
		
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		if (rcm == null) {
			if (!parentButton.isMouseInside(mXIn, mYIn)) {
				parentButton.destroyList();
			}
			else {
				QoT.getTopRenderer().revealHiddenObjects();
				
				for (var w : QoT.getTopRenderer().getAllActiveWindows()) {
					w.setDrawWhenMinimized(false);
				}
				
				last = null;
			}
		}
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
				QoT.getTopRenderer().setFocusedObject(p);
				p.bringToFront();
				break;
			case "Minimize":
				parentButton.destroyList();
				p.setMaximized(ScreenLocation.OUT);
				p.miniaturize();
				QoT.getTopRenderer().setFocusedObject(p);
				p.bringToFront();
				break;
			case "Recenter":
				parentButton.destroyList();
				WindowHeader h = p.getHeader();
				TaskBar b = QoT.getTopRenderer().getTaskBar();
				
				double hh = (h != null) ? h.height : 0;
				double bh = (b != null) ? b.height : 0;
				double oH = hh + bh;
				double maxW = NumberUtil.clamp(p.width, 0, res.getWidth() - 40);
				double maxH = NumberUtil.clamp(p.height, 0, res.getHeight() - 40 - oH);
				
				p.setSize(maxW, maxH);
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
		
		DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
		if (entry != null && entry.getEntryObject() instanceof WindowParent<?> p) {
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
			
			QoT.getTopRenderer().displayWindow(rcm, ObjectPosition.CURSOR_CORNER);
		}
	}
	
}
