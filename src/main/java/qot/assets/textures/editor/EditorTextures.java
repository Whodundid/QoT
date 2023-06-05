package qot.assets.textures.editor;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class EditorTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final EditorTextures t = new EditorTextures();
	public static EditorTextures instance() { return t; }
	
	// Hide constructor
	private EditorTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\editor\\";

	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	rectangle = new GameTexture(textureDir, "rectsel.png"),
	move = new GameTexture(textureDir, "move.png"),
	magicWand = new GameTexture(textureDir, "magicwand.png"),
	paintBucket = new GameTexture(textureDir, "paintbucket.png"),
	brush = new GameTexture(textureDir, "brush.png"),
	pencil = new GameTexture(textureDir, "pencil.png"),
	eyeDropper = new GameTexture(textureDir, "eyedropper.png"),
	eraser = new GameTexture(textureDir, "eraser.png"),
	line = new GameTexture(textureDir, "line.png"),
	select = new GameTexture(textureDir, "select.png"),
	shape = new GameTexture(textureDir, "shape.png"),
	place = new GameTexture(textureDir, "place.png"),
	
	play = new GameTexture(textureDir, "play.png"),
	stop = new GameTexture(textureDir, "stop.png"),
	
	player_spawn = new GameTexture(textureDir, "player_start.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, rectangle);
		reg(sys, move);
		reg(sys, magicWand);
		reg(sys, paintBucket);
		reg(sys, brush);
		reg(sys, pencil);
		reg(sys, eyeDropper);
		reg(sys, eraser);
		reg(sys, line);
		reg(sys, select);
		reg(sys, shape);
		reg(sys, place);
		reg(sys, play);
		reg(sys, stop);
		reg(sys, player_spawn);
	}
	
}
