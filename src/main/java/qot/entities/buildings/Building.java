package qot.entities.buildings;

import envision.game.entities.Entity;
import envision.game.entities.EntityRenderer;

public abstract class Building extends Entity {

	protected Building() {
		addComponent(new EntityRenderer(this));
		
        this.canRegenHealth = false;
        this.canRegenMana = false;
	}
	
}
