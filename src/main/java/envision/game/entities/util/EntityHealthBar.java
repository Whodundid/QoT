package envision.game.entities.util;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import envision.engine.windows.windowTypes.WindowObject;
import envision.game.entities.Entity;
import eutil.colors.EColors;

public class EntityHealthBar extends WindowObject {
	
	private Entity theEntity;
	private boolean shouldDraw = false;
	private long countDownStart;
	private long countDownDuration = 0l;
	private long fadeOutStart;
	private long fadeOutDuration = 600l;
	
	public EntityHealthBar(Entity entityIn) {
		init(Envision.currentScreen);
		theEntity = entityIn;
	}
	
	public EntityHealthBar(Entity entityIn, double x, double y, double w, double h) {
		init(Envision.currentScreen, x, y, w, h);
		theEntity = entityIn;
	}
	
	//---------------------------
	// Overrides : IWindowObject
	//---------------------------
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		int opacity = 0;
		
		if (shouldDraw && System.currentTimeMillis() - countDownStart >= countDownDuration) {
			shouldDraw = false;
			fadeOutStart = System.currentTimeMillis();
		}
		
		if (shouldDraw) {
			opacity = 255;
		}
		else {
			if (System.currentTimeMillis() - fadeOutStart < fadeOutDuration) {
				var ratio = ((255L * (System.currentTimeMillis() - fadeOutStart)) / fadeOutDuration);
				opacity = 255 - (int) ratio;
			}
		}
		
		if (opacity > 0) {
			var cur = theEntity.health;
			var percent = (double) cur / (double) theEntity.maxHealth;
			var pw = (width * percent);
			
			RenderingManager.drawRect(getDimensions().add(1), EColors.black.opacity(opacity));
			var end = (theEntity == Envision.thePlayer) ? 4 : 1;
			RenderingManager.drawRect(startX, startY, startX + pw, endY - end, EColors.mc_darkred.opacity(opacity));
		}
	}

	//---------
	// Methods
	//---------
	
	public void keepDrawing() {
		shouldDraw = true;
		countDownStart = System.currentTimeMillis();
	}
	
	public void setCountDownDuration(long in) { countDownDuration = in; }
	public void setFadeOutDuration(long in) { fadeOutDuration = in; }
	
}
