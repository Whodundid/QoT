package world.mapEditor.editorTools.tools;

import game.GameObject;
import game.entities.Entity;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.util.EditorItem;
import world.mapEditor.editorTools.EditorTool;

public class Tool_Selector extends EditorTool {

	private EditorItem item;
	
	public Tool_Selector(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		item = getPrimary();
		
		if (item != null) {
			if (item.isTile()) {}
			else if (item.isGameObject()) {
				GameObject obj = item.getGameObject();
				if (obj instanceof Entity ent) {
					Class<? extends Entity> objClass = ent.getClass();
					try {
						Entity newEnt = objClass.getConstructor().newInstance();
						addObjectToWorld(newEnt);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
