package envision.game.world.mapEditor.editorTools.tools;

import envision.game.GameObject;
import envision.game.entity.Entity;
import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.editorParts.util.EditorObject;
import envision.game.world.mapEditor.editorTools.EditorTool;

public class Tool_Place extends EditorTool {
	
	private EditorObject object;
	
	//--------------
	// Constructors
	//--------------
	
	public Tool_Place(MapEditorScreen in) {
		super(in);
	}

	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onPress() {
		object = getPrimary();
		
		if (object == null) return;
		if (object.isTile()) placeTile();
		else if (object.isEntity()) placeEntity();
		else placeObject();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void placeTile() {
		
	}
	
	private void placeEntity() {
		Entity ent = object.getEntity();
		Class<? extends Entity> objClass = ent.getClass();
		
		try {
			Entity newEnt = objClass.getConstructor().newInstance();
			addEntityToWorld(newEnt);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void placeObject() {
		GameObject obj = object.getGameObject();
		System.out.println("PLACE OBJECT: " + obj);
	}
	
}
