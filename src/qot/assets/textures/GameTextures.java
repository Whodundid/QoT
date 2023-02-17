package qot.assets.textures;

import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.assets.textures.cursor.CursorTextures;
import qot.assets.textures.doodads.DoodadTextures;
import qot.assets.textures.editor.EditorTextures;
import qot.assets.textures.entity.EntityTextures;
import qot.assets.textures.general.GeneralTextures;
import qot.assets.textures.item.ItemTextures;
import qot.assets.textures.taskbar.TaskBarTextures;
import qot.assets.textures.window.WindowTextures;
import qot.assets.textures.windowbuilder.WindowBuilderTextures;
import qot.assets.textures.world.WorldTextures;

/** Contains mappings to all of QoT's textures. */
public class GameTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final GameTextures t = new GameTextures();
	public static GameTextures instance() { return t; }
	
	// Hide constructor
	private GameTextures() {}
	
	//-------------------------------
	
	public static CursorTextures cursorTextures = CursorTextures.instance();
	public static DoodadTextures doodadTextures = DoodadTextures.instance();
	public static EditorTextures editorTextures = EditorTextures.instance();
	public static EntityTextures entityTextures = EntityTextures.instance();
	public static GeneralTextures generalTextures = GeneralTextures.instance();
	public static ItemTextures itemTextures = ItemTextures.instance();
	public static TaskBarTextures taskBarTextures = TaskBarTextures.instance();
	public static WindowTextures windowTextures = WindowTextures.instance();
	public static WindowBuilderTextures windowBuilderTextures = WindowBuilderTextures.instance();
	public static WorldTextures worldTextures = WorldTextures.instance();
	
	//-------------------------------
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		cursorTextures.onRegister(sys);
		doodadTextures.onRegister(sys);
		editorTextures.onRegister(sys);
		entityTextures.onRegister(sys);
		generalTextures.onRegister(sys);
		itemTextures.onRegister(sys);
		taskBarTextures.onRegister(sys);
		windowTextures.onRegister(sys);
		windowBuilderTextures.onRegister(sys);
		worldTextures.onRegister(sys);
	}
	
}
