package mapEditor.editorTools;

import assets.textures.EditorTextures;
import assets.textures.WindowTextures;
import renderEngine.textureSystem.GameTexture;

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
