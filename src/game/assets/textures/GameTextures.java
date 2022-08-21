package game.assets.textures;

import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;
import game.assets.textures.cursor.CursorTextures;
import game.assets.textures.doodads.DoodadTextures;
import game.assets.textures.editor.EditorTextures;
import game.assets.textures.entity.EntityTextures;
import game.assets.textures.general.GeneralTextures;
import game.assets.textures.item.ItemTextures;
import game.assets.textures.taskbar.TaskBarTextures;
import game.assets.textures.window.WindowTextures;
import game.assets.textures.world.WorldTextures;

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
	
	private CursorTextures cursorTextures = CursorTextures.instance();
	private DoodadTextures doodadTextures = DoodadTextures.instance();
	private EditorTextures editorTextures = EditorTextures.instance();
	private EntityTextures entityTextures = EntityTextures.instance();
	private GeneralTextures generalTextures = GeneralTextures.instance();
	private ItemTextures itemTextures = ItemTextures.instance();
	private TaskBarTextures taskBarTextures = TaskBarTextures.instance();
	private WindowTextures windowTextures = WindowTextures.instance();
	private WorldTextures worldTextures = WorldTextures.instance();
	
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
		worldTextures.onRegister(sys);
	}
	
}
