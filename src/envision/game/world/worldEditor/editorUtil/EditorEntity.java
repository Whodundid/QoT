package envision.game.world.worldEditor.editorUtil;

import envision.engine.windows.windowTypes.WindowObject;
import envision.game.entities.Entity;

public class EditorEntity extends WindowObject<Entity> {
	
	private Entity entity;
	
	public EditorEntity(Entity entIn) {
		entity = entIn;
	}
	
}
