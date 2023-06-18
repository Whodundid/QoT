package envision.engine.notifications;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import envision.Envision;
import envision.engine.notifications.util.NotificationObject;
import envision.engine.notifications.util.NotificationType;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.datatypes.util.EList;
import eutil.file.LineReader;
import qot.settings.QoTSettings;

//Author: Hunter Bragg

public class NotificationHandler {
	
	private static NotificationHandler instance = null;
	public static File configLocation = new File(QoTSettings.getLocalGameDir(), "notifications.cfg");
	protected Deque<NotificationObject> notificationQueue;
	protected EList<NotificationType> enabledNotifications = EList.newList();
	protected EList<NotificationType> disabledNotifications = EList.newList();
	protected NotificationObject curNote = null;
	protected long delayStart = 0l;
	protected long delayTime = 300l;
	
	//=================
	// Static Instance
	//=================
	
	public static NotificationHandler getInstance() {
		return instance = instance != null ? instance : new NotificationHandler();
	}

	private NotificationHandler() {
		notificationQueue = new ArrayDeque();
	}
	
	//========
	// Config
	//========
	
	public boolean loadConfig() {
		if (!configLocation.exists()) saveConfig();
		
		if (load()) Envision.info("EMC: Successfully loaded Notification config file!");
		else Envision.error("EMC Error: Failed to load Notification config file!");
		
		return false;
	}
	
	private boolean load() {
		Envision.info("EMC: Loading Notification config file...");
		try (var reader = new LineReader(configLocation)) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				
				if (line.equals("END")) break; //config end identifier
				if (line.isEmpty() || line.startsWith("**")) continue; //ignore comment line
				
				String[] parts = line.split(",");
				if (parts.length == 2) {
					
					String internal = "";
					boolean enabled = false;
					
					internal = parts[0];
					enabled = Boolean.parseBoolean(parts[1]);
					
					if (internal != null && !internal.isEmpty()) {
						for (NotificationType o : getNotificationTypes()) {
						    if (o == null || !o.getInternalName().equals(internal)) continue;
						    
						    if (enabled) enableNotificationType(o, false);
                            else disableNotificationType(o, false);
						}
					}
					
				} //while
			}
			
			return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	public boolean saveConfig() {
		if (!configLocation.exists()) {
			Envision.info("EMC: Notification config not found, attempting to create a new one...");
			
			//if no config exists, enable all
			for (NotificationType o : getNotificationTypes()) {
				enableNotificationType(o, false);
			}
		}
		
		if (!save()) {
			Envision.error("EMC Error: Failed to save Notification config file!");
			return false;
		}
		
		return true;
	}
	
	private boolean save() {
		try (PrintWriter saver = new PrintWriter(configLocation, "UTF-8")) {
			saver.println("** EMC Notification Config **");
			saver.println();
			
			boolean oneEnabled = false;
			
			for (NotificationType o : getNotificationTypes()) {
				if (o != null) {
					oneEnabled = true;
					saver.println(o.getInternalName() + "," + isNotificationTypeEnabled(o));
				}
			}
			
			if (oneEnabled) { saver.println(); }
			saver.print("END");
			
			return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}

	//=========
	// Methods
	//=========
	
	public NotificationHandler post(String message, WindowParent<?> attentionWindow) {
	    return post(new EnvisionNotification(message));
	}
	
	public NotificationHandler post(NotificationObject obj) {
		if (obj != null && enabledNotifications.contains(obj.getType())) {
		    notificationQueue.add(obj);
		}
		return this;
	}
	
	public void update() {
		if (curNote == null && !notificationQueue.isEmpty()) {
			if (System.currentTimeMillis() - delayStart > delayTime) {
			    displayNextNotification();
			}
		}
		
		if (curNote != null && curNote.isClosed()) {
		    curNote = null;
		}
	}
	
	public void clearAllNotifications() {
		if (Envision.getTopScreen().isWindowOpen(NotificationObject.class)) {
			Object o = Envision.getTopScreen().getWindowInstance(NotificationObject.class);
			if (o instanceof NotificationObject obj) {
				obj.close();
			}
		}
		notificationQueue.clear();
		curNote = null;
	}
	
	public void removeCurrentNotification() {
		curNote = null;
		delayStart = System.currentTimeMillis();
	}
	
	public void registerNotificationType(NotificationType typeIn) {
		if (!containsType(typeIn)) { enabledNotifications.add(typeIn); }
		else { Envision.error("EMC Error: A NotificationType of " + typeIn.getInternalName() + " already exists!"); }
	}
	
	public void unregisterNotificationType(NotificationType typeIn) {
		if (containsType(typeIn)) { 
			enabledNotifications.removeIfContains(typeIn);
			disabledNotifications.removeIfContains(typeIn);
		}
		else { Envision.error("EMC Error: A NotificationType of " + typeIn.getInternalName() + " does not exist!"); }
	}
	
	public void enableNotificationType(NotificationType typeIn, boolean save) {
		enabledNotifications.addNullContains(typeIn);
		disabledNotifications.removeIfContains(typeIn);
		if (save) { saveConfig(); }
		//Envision.reloadAllWindows();
		reloadWindows();
	}
	
	public void disableNotificationType(NotificationType typeIn, boolean save) {
		enabledNotifications.removeIfContains(typeIn);
		disabledNotifications.addNullContains(typeIn);
		if (save) { saveConfig(); }
		//Envision.reloadAllWindows();
		reloadWindows();
	}
	
	public boolean toggleNotificationEnabled(NotificationType typeIn, boolean save) {
		if (enabledNotifications.contains(typeIn)) {
		    disableNotificationType(typeIn, save);
		    return false;
		}
		else if (disabledNotifications.contains(typeIn)) {
		    enableNotificationType(typeIn, save);
		    return true;
		}
		return false;
	}
	
	public boolean isNotificationTypeEnabled(NotificationType typeIn) {
		return enabledNotifications.contains(typeIn);
	}
	
	public NotificationHandler reloadWindows() {
	    Envision.getTopScreen().getAllActiveWindows().forEach(w -> w.sendArgs("Reload Notifications"));
	    return this;
	}
	
	//=========
	// Getters
	//=========
	
	public EList<NotificationType> getNotificationTypes() {
		return EList.combineLists(enabledNotifications, disabledNotifications);
	}
	
	public EList<String> getInternalNames() {
		EList<String> names = EList.newList();
		for (NotificationType t : getNotificationTypes()) {
			names.add(t.getInternalName());
		}
		return names;
	}
	
	public NotificationObject getCurrentNotification() { return curNote; }
	
	//=========
	// Setters
	//=========
	
	public void setNotificationDelay(long delayIn) { delayTime = delayIn; }
	
	//==================
	// Internal Methods
	//==================
	
	protected void displayNextNotification() {
		if (!notificationQueue.isEmpty()) {
			NotificationObject n = notificationQueue.pop();
			
			if (n != null && isNotificationTypeEnabled(n.getType())) {
				curNote = n;
				Envision.getTopScreen().addObject(curNote);
			}
		}
	}
	
	protected boolean containsType(NotificationType typeIn) {
		if (typeIn != null) {
			for (NotificationType t : getNotificationTypes()) {
				if (t.getInternalName() == typeIn.getInternalName()) { return true; }
			}
		}
		return false;
	}
	
}
