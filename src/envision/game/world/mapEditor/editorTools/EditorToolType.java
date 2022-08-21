package envision.game.world.mapEditor.editorTools;

import envision.renderEngine.textureSystem.GameTexture;
import game.assets.textures.editor.EditorTextures;
import game.assets.textures.window.WindowTextures;

public enum EditorToolType {
	
	ADD_REGION("Add Region", WindowTextures.plus),
	SELECTOR("Selector", EditorTextures.select),
	RECTSELECT("Rectangle Select", EditorTextures.rectangle),
	MOVE("Move Selection", EditorTextures.move),
	MAGICWAND("Magic Wand", EditorTextures.magicWand),
	PAINTBUCKET("Paint Bucket", EditorTextures.paintBucket),
	BRUSH("Brush", EditorTextures.brush),
	PENCIL("Pencil", EditorTextures.pencil),
	EYEDROPPER("Eye Dropper", EditorTextures.eyeDropper),
	ERASER("Eraser", EditorTextures.eraser),
	LINE("Line", EditorTextures.line),
	SHAPE("Shape", EditorTextures.shape),
	REGION("", null),
	NONE("", null);
	
	public String hoverText;
	public GameTexture texture;
	
	EditorToolType(String in, GameTexture tex) {
		hoverText = in;
		texture = tex;
	}
	
}
