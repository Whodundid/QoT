package envision.game.world.mapEditor.editorTools.tools;

import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.editorParts.util.EditorItem;
import envision.game.world.mapEditor.editorTools.EditorTool;
import envision.game.world.worldTiles.WorldTile;

public class Tool_Pencil extends EditorTool {

	private EditorItem item;
	
	public Tool_Pencil(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		item = (button == 0) ? getPrimary() : getSecondary();
		
		if (item != null && item.isTile()) {
			setTile(WorldTile.randVariant(item.getTile()));
		}
		else setTile(null);
	}

	@Override
	public void onUpdate() {
		setTile((item != null) ? WorldTile.randVariant(item.getTile()) : null);
	}
	
}
