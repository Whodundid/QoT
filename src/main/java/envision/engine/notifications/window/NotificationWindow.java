package envision.engine.notifications.window;

import envision.Envision;
import envision.engine.notifications.util.NotificationType;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import qot.assets.textures.taskbar.TaskBarTextures;

public class NotificationWindow extends WindowParent {
	
	WindowButton enableAll, disableAll, back;
	WindowScrollList nList;
	WindowTextField searchField;
	BoxList<String, EList<NotificationType>> notes = new BoxList<>();
	
	private double vPos, hPos;
	
	public NotificationWindow() {
		super();
		aliases.add("notifications", "notif", "noteman", "note");
		windowIcon = TaskBarTextures.notification;
	}
	
	@Override
	public void initWindow() {
		defaultDims();
		setObjectName("Notification Manager");
		setMaximizable(true);
		setResizeable(true);
		setMinDims(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		buildNotifications();
		
		nList = new WindowScrollList(this, startX + 2, startY + 20, width - 4, height - 50);
		nList.setBackgroundColor(EColors.pdgray.intVal);
		//Dimension_d ld = nList.getListDimensions();
		
		int yPos = 8;
		
		for (var boxes : notes) {
			String catName = boxes.getA();
			EList<NotificationType> types = boxes.getB();
			
			nList.addObjectToList(new WindowLabel(nList, 6, yPos, catName, EColors.orange));
			yPos += 15;
			
			for (NotificationType t : types) {
				NoteSettingContainer con = new NoteSettingContainer(nList, t, yPos, false);
				yPos += con.getYPos();
			}
		}
		
		nList.fitItemsInList();
		
		enableAll = new WindowButton(this, startX + 5, endY - 25, 64, 20, "Enable All");
		disableAll = new WindowButton(this, midX - 32, endY - 25, 64, 20, "Disable All");
		back = new WindowButton(this, endX - 69, endY - 25, 64, 20, "Back");
		
		enableAll.setStringColor(EColors.green);
		disableAll.setStringColor(EColors.lred);
		back.setStringColor(EColors.yellow);
		
		addObject(nList);
		addObject(enableAll, disableAll, back);
	}
	
	@Override
	public void preReInit() {
		vPos = nList.getVScrollBar().getScrollPos();
		hPos = nList.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		nList.getVScrollBar().setScrollPos(vPos);
		nList.getHScrollBar().setScrollPos(hPos);
	}
	
	@Override
	public void drawObject_i(long dt, int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 2, startY + 2, endX - 2, startY + 20, EColors.black);
		drawRect(startX + 3, startY + 3, endX - 3, startY + 20, EColors.steel);
		drawStringCS("Select Enabled Notifications", midX, startY + 8, EColors.orange);
		
		super.drawObject(dt, mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == enableAll) {
			for (NotificationType t : Envision.getNotificationHandler().getNotificationTypes()) {
			    Envision.getNotificationHandler().enableNotificationType(t, true);
			}
			
			double pos = nList.getVScrollBar().getScrollPos();
			reInitChildren();
			nList.getVScrollBar().setScrollPos(pos);
		}
		if (object == disableAll) {
			for (NotificationType t : Envision.getNotificationHandler().getNotificationTypes()) {
				Envision.getNotificationHandler().disableNotificationType(t, true);
			}

			double pos = nList.getVScrollBar().getScrollPos();
			reInitChildren();
			nList.getVScrollBar().setScrollPos(pos);
		}
		if (object == back) { fileUpAndClose(); }
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1 && args[0] instanceof String msg) {
            if (msg.equals("Reload Notifications") || msg.equals("Reload")) {
                double pos = nList.getVScrollBar().getScrollPos();
                reInitChildren();
                nList.getVScrollBar().setScrollPos(pos);
            }
		}
	}
	
	private void buildNotifications() {
		notes.clear();
		EList<NotificationType> unSorted = EList.of(Envision.getNotificationHandler().getNotificationTypes());
		
		EList<String> categories = EList.newList();
		for (NotificationType t : unSorted) {
		    categories.addNullContains(t.getCategory());
		}
		
		EList<NotificationType> types = EList.newList();
		
		//search for envision
		{
	        var it = unSorted.iterator();
	        
	        boolean found = false;
	        while (it.hasNext()) {
	            NotificationType t = it.next();
	            if (t.getCategory() != null && t.getCategory().equals("Envision")) {
	                types.add(t);
	                it.remove();
	                found = true;
	            }
	        }
	        
	        if (found) {
	            notes.add(new Box2("Envision", types));
	        }
		}
		
		if (categories.contains("Envision")) {
		    categories.remove("Envision");
		}
		
		//get all other categories
		for (String s : categories) {
			var it = unSorted.iterator();
			while (it.hasNext()) {
				NotificationType t = it.next();
				if (t.getCategory() != null && t.getCategory().equals(s)) {
					types.add(t);
					it.remove();
				}
			}
			
			notes.add(s, EList.of(types));
		}
		
		//add all the rest as generic
		if (unSorted.isNotEmpty()) {
			notes.add("Non Specific", EList.of(unSorted));
		}
	}
	
}
 