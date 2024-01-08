package envision.engine.notifications;

import envision.Envision;
import envision.engine.notifications.util.NotificationObject;
import envision.engine.notifications.util.NotificationType;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class EnvisionNotification extends NotificationObject {
	
	private GameTexture image = null;
	
	public EnvisionNotification(String messageIn) { this(messageIn, null, Envision.envisionNotifaction); }
	public EnvisionNotification(String messageIn, NotificationType typeIn) { this(messageIn, null, typeIn); }
	public EnvisionNotification(String messageIn, GameTexture imageIn) { this(messageIn, null, Envision.envisionNotifaction); }
	public EnvisionNotification(String messageIn, GameTexture imageIn, NotificationType typeIn) {
		super(typeIn);
		message = messageIn;
		image = imageIn;
	}
	
	@Override
	public void initWindow() {
		setDimensions(startX, res.height - 52, 44 + strWidth(message), 30);
		setPinned(true);
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
		
		//GlStateManager.color(1f, 1f, 1f, 0.75f);
		
		if (image != null) {
			drawTexture(image, startX + 5, midY - 10, 20, 20);
		}
		
		drawStringS(message, startX + 30, midY - 3, EColors.seafoam);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0) {
			WindowButton.playPressSound();
			close();
			if (attentionObject != null) {
				if (Envision.getDeveloperDesktop().getChildren().contains(attentionObject)) {
				    attentionObject.requestFocus();
				}
				else {
					Envision.getDeveloperDesktop().displayWindow(attentionObject);
				}
			}
		}
	}
	
	public GameTexture getImage() { return image; }
	
	public EnvisionNotification setImage(GameTexture imageIn) { image = imageIn; return this; }
	
}
