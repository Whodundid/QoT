package gameSystems.textureSystem;

import org.lwjgl.opengl.GL11;
import util.EUtil;
import util.mathUtil.NumUtil;
import util.storageUtil.EArrayList;

public class GameTexture {
	
	private GameTexture parentTexture;
	private GameTexture[] children = new GameTexture[0];
	private String name;
	private int textureID = -1; //-1 indicates this texutre is currently unregistered
	private String filePath;
	private int width = -1, height = -1;
	private boolean destroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GameTexture(String filePathIn) { this(null, "noname", filePathIn); }
	public GameTexture(String nameIn, String filePathIn) { this(null, nameIn, filePathIn); }
	public GameTexture(GameTexture parent, String filePathIn) { this(parent, "noname", filePathIn); }
	public GameTexture(GameTexture parent, String nameIn, String filePathIn) {
		parentTexture = parent;
		name = nameIn;
		filePath = filePathIn;
	}
	
	//----------------
	// Public Methods
	//----------------
	
	public GameTexture setChildren(final GameTexture... in) { children = in; return this; }
	
	public boolean destroy() {
		if (textureID != -1) {
			GL11.glDeleteTextures(textureID);
			textureID = -1;
			destroyed = true;
			return true;
		}
		return false;
	}
	
	public boolean hasBeenRegistered() { return textureID != -1; }
	
	//---------------------
	// GameTexture Getters
	//---------------------
	
	public GameTexture getParent() { return parentTexture; }
	public GameTexture[] getChildren() { return children; }
	public String getName() { return name; }
	public int getTextureID() { return textureID; }
	public String getFilePath() { return filePath; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean hasBeenDestroyed() { return destroyed; }
	public boolean hasParent() { return parentTexture != null; }
	
	public EArrayList<GameTexture> getAllChildren() {
		EArrayList<GameTexture> found = new EArrayList();
		EArrayList<GameTexture> withChildren = new EArrayList();
		EArrayList<GameTexture> workList = new EArrayList();
		
		EUtil.forEach(children, c -> { found.add(c); if (c.getChildren().length > 0) { withChildren.add(c); } });
		withChildren.forEach(c -> workList.addAll(c));
		
		while (workList.isNotEmpty()) {
			found.addAll(workList);
			
			withChildren.clear();
			workList.filterForEach(c -> c.children.length > 0, withChildren::add);
			
			workList.clear();
			withChildren.forEach(c -> workList.addAll(c.children));
		}
		
		return found;
	}
	
	private GameTexture getRandChild() {
		return children[NumUtil.getRoll(0, children.length - 1)];
	}
	
	public GameTexture getRandVariant() {
		return (children.length > 0) ? ((NumUtil.roll(0, 0, 1)) ? this : getRandChild()) : this;
	}
	
}
