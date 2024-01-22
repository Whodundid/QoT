package qot.entities.enemies.archer;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Projectile;
import qot.assets.textures.entity.EntityTextures;

public class Arrow extends Projectile {
	
	public Arrow() {
		super("Arrow");
		
        // 1.5 seconds
        this.maxLifeSpan = 400.0f;
        
        init(0, 0, 32, 32);
        sprite = new Sprite(EntityTextures.arrow_projectile);
        setCollisionBox(startX + 5, startY + 5, endX - 5, endY - 5);
        
        setSpeed(400);
	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
