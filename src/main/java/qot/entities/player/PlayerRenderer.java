package qot.entities.player;

import envision.engine.rendering.GLObject;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.game.entities.EntityRenderer;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Rotation;
import qot.assets.textures.item.ItemTextures;

public class PlayerRenderer extends EntityRenderer {
	
	//========
	// Fields
	//========
	
	protected QoT_Player player;
	
	//==============
	// Constructors
	//==============
	
	public PlayerRenderer(QoT_Player playerIn) {
		super(playerIn, true);
		player = playerIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void drawEntity(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		boolean flip = player.facing == Rotation.RIGHT || player.facing == Rotation.DOWN;
		
		RenderingManager.drawTexture(player.tex, x, y, w, h, flip, brightness);
		//healthBar.setDimensions(x, y - 7, w, 7);
		//healthBar.drawObject(Mouse.getMx(), Mouse.getMy());
		
		if ((player.recentlyAttacked || player.healthChanged) && !player.invincible && (player.health < player.maxHealth)) {
//			EDimension draw = new EDimension(x + 20, y - 7, x + w - 20, y);
//			
//			var cur = health;
//			var percent = (double) cur / (double) maxHealth;
//			var pw = (draw.width * percent);
//			
//			drawRect(draw.add(1), EColors.black);
//			var end = (this == Envision.thePlayer) ? 4 : 1;
//			drawRect(draw.startX, draw.startY, draw.startX + pw, draw.endY - end, EColors.mc_darkred);
			
			//healthBar.keepDrawing();
		}
		
		RenderingManager.drawStringC(player.headText, x + w/2, y - h/4);
		//drawStringC(recentlyAttacked + ":" + recentlyHit + ":" + mouseOver, x + w/2, y - h/2, 0.5, 0.5);
		
		// draw sword *crapily*
		if (player.attacking) {
			if (System.currentTimeMillis() - player.attackStart <= 100) {
				double drawX = x + w / 2 - ItemTextures.iron_sword.getWidth();
				double drawY = y + h / 2 - ItemTextures.iron_sword.getHeight() / 2;
				if (BatchManager.isEnabled()) BatchManager.drawTexture(ItemTextures.iron_sword, drawX, drawY, 64, 64);
				else RenderingManager.drawTexture(ItemTextures.iron_sword, drawX, drawY, 64, 64);
			}
			
			if ((System.currentTimeMillis() - player.attackStart) >= player.timeUntilNextAttack) {
				player.attacking = false;
			}
		}
		
		//draw cooldown bar
		if (System.currentTimeMillis() - player.attackStart < player.timeUntilNextAttack) {
			Dimension_d draw = new Dimension_d(x, y - 3, x + w, y - 1);
			
			var cur = System.currentTimeMillis() - player.attackStart;
			var percent = (double) cur / (double) player.timeUntilNextAttack / 2;
			var pw = (draw.width * percent);
			
			if (BatchManager.isEnabled()) BatchManager.drawRect(draw.startX + pw, draw.startY, draw.endX - pw, draw.endY, EColors.mc_gold.opacity(180));
			else GLObject.drawRect(draw.startX + pw, draw.startY, draw.endX - pw, draw.endY, EColors.mc_gold.opacity(180));
		}
	}
	
}
