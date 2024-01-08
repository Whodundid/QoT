package envision.engine.notifications.window;

import envision.Envision;
import envision.engine.assets.WindowTextures;
import envision.engine.notifications.util.NotificationObject;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.ObjectPosition;

//Author: Hunter Bragg

public class NotificationRCM extends RightClickMenu {

	private NotificationObject note = null;
	
	public NotificationRCM() { this(null); }
	public NotificationRCM(NotificationObject noteIn) {
		super();
		note = noteIn;
	}
	
	@Override
	public void initWindow() {
		if (note != null) { addOption("Close", WindowTextures.close); }
		addOption("Settings", WindowTextures.settings);

		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Notifications");
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == this && args.length > 0) {
			switch ((String) args[0]) {
			case "Close": note.close(); break;
			case "Settings": openSettings(); break;
			}
		}
	}
	
	private void openSettings() {
	    Envision.getDeveloperDesktop().displayWindow(new NotificationWindow(), ObjectPosition.SCREEN_CENTER);
	}
	
}