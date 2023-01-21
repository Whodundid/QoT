package envisionEngine.gameEngine.world.worldEditor.editorUtil;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.windowLib.windowTypes.WindowObject;

public class EditorEntity extends WindowObject<Entity> {
	
	private Entity entity;
	
	public EditorEntity(Entity entIn) {
		entity = entIn;
	}
	
}
