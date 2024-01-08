package envision.game.world.worldEditor.editorTools.tools;

import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import envision.game.world.worldEditor.editorTools.toolUtil.FloodFill;
import envision.game.world.worldTiles.WorldTile;

public class Tool_PaintBucket extends EditorTool {

	public Tool_PaintBucket(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		EditorObject item = (button == 0) ? getPrimary() : getSecondary();
		
		if (item == null || !item.isTile()) return;
		WorldTile tile = item.getTile();
		
		WorldTile tileAtMouse = editor.getEditorWorld().getTileAt(wx, wy);
		
		FloodFill.floodFillReplace(editor.getEditorWorld(), tileAtMouse, wx, wy, tile);
		editor.markUnsaved();
	}

	@Override
	public void onRelease() {
		
	}

	@Override
	public void onUpdate() {
		
	}
	
}
