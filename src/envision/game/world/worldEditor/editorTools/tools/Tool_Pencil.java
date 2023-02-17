package envision.game.world.worldEditor.editorTools.tools;

import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import envision.game.world.worldTiles.WorldTile;

public class Tool_Pencil extends EditorTool {

	private EditorObject item;
	
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
