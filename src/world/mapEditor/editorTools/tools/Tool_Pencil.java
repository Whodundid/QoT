package world.mapEditor.editorTools.tools;

import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.util.EditorItem;
import world.mapEditor.editorTools.EditorTool;
import world.resources.WorldTile;

public class Tool_Pencil extends EditorTool {

	private EditorItem item;
	
	public Tool_Pencil(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		item = (button == 0) ? getPrimary() : getSecondary();
		
		if (item != null) {
			if (item.isTile()) setTile(WorldTile.randVariant(item.getTile()));
			else if (item.isEntity()) {}
		}
		else setTile(null);
	}

	@Override
	public void onUpdate() {
		setTile((item != null) ? WorldTile.randVariant(item.getTile()) : null);
	}
	
}
