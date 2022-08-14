package engine.renderEngine.textureSystem;

import org.lwjgl.opengl.GL11;

import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.random.RandomUtil;
import main.settings.QoTSettings;

public class GameTexture {
	
	public static final String rDir = QoTSettings.getResourcesDir().toString();
	public static final String tDir = rDir + "\\textures\\";
	
	/** If this texture is a child variant, this is the parent for which it relates. If this value is null, there is no parent and this is not a child texture. */
	private GameTexture parentTexture;
	/** A collection of this texture's child variants. */
	protected BoxList<GameTexture, Integer> children = new BoxList<>();
	/** When calling for a random child ID, this percent will indicate the likely hood this specific child will be selected. */
	private int percent = -1;
	/** The internal use ID for this texture object. -1 indicates an unregistered texture ID. -- Assigned dynamically through texture registration. */
	private int textureID = -1;
	/** If this is a child texture for a set of textures, I.E. grass with its multiple variants, this child ID indicates which specific variant is being refered internally. */
	private int childID = 0;
	/** The file system path where this texture exists. */
	private String filePath;
	/** The texture's width in pixels. -- Assigned dynamically through texture registration. */
	private int width = -1;
	/** The texture's height in pixels. -- Assigned dynamically through texture registration. */
	private int height = -1;
	/** True if this texture ID has been deleted through OpenGL. */
	private boolean destroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GameTexture(String filePathIn) { this(null, "", filePathIn); }
	public GameTexture(String basePath, String filePathIn) { this(null, basePath, filePathIn); }
	public GameTexture(GameTexture parent, String filePathIn) { this(parent, "", filePathIn); }
	public GameTexture(GameTexture parent, String basePath, String filePathIn) {
		parentTexture = parent;
		filePath = basePath + filePathIn;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	protected void registerChildTextures(TextureSystem systemIn) {
		for (GameTexture t : children.getAVals()) {
			systemIn.registerTexture(t);
		}
	}
	
	//----------------
	// Public Methods
	//----------------
	
	public GameTexture setChildID(int id) { childID = id; return this; }
	public GameTexture addChild(String path) { return addChild(new GameTexture(path)); }
	public GameTexture addChild(String base, String path) { return addChild(new GameTexture(base, path)); }
	public GameTexture addChild(final GameTexture in) { return addChild(in, -1); }
	public GameTexture addChild(final GameTexture in, final int percentage) {
		if (in != null) {
			in.parentTexture = this;
			children.add(in, percentage);
			in.setChildID(children.size());
		}
		return in;
	}
	
	public boolean destroy() {
		if (textureID != -1) {
			GL11.glDeleteTextures(textureID);
			textureID = -1;
			destroyed = true;
			return true;
		}
		return false;
	}
	
	public boolean hasBeenRegistered() {
		return textureID != -1;
	}
	
	//---------
	// Getters
	//---------
	
	public GameTexture getParent() { return parentTexture; }
	public EList<GameTexture> getChildren() { return children.getAVals(); }
	public int getTextureID() { return textureID; }
	public int getChildID() { return childID; }
	public String getFilePath() { return filePath; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean hasBeenDestroyed() { return destroyed; }
	public boolean hasParent() { return parentTexture != null; }
	
	public EList<GameTexture> getAllChildren() {
		EList<GameTexture> found = new EArrayList<>();
		EList<GameTexture> withChildren = new EArrayList<>();
		EList<GameTexture> workList = new EArrayList<>();
		
		children.getAVals().forEach(c -> {
			found.add(c);
			if (c.children.isNotEmpty()) withChildren.add(c);
		});
		workList.addAll(withChildren);
		
		while (workList.isNotEmpty()) {
			found.addAll(workList);
			
			withChildren.clear();
			workList.filterForEach(c -> c.children.getAVals().isNotEmpty(), withChildren::add);
			
			workList.clear();
			withChildren.forEach(c -> workList.addAll(c.children.getAVals()));
		}
		
		return found;
	}
	
	private GameTexture getRandChild() {
		int pos = RandomUtil.getRoll(0, children.size() - 1);
		return children.getA(pos);
	}
	
	public void initVariantPercents() {
		BoxList<GameTexture, Integer> total = new BoxList<>(children);
		total.add(this, percent);
		//for (Box2<GameTexture, Integer> t : total) {
			//int amount = t.getB();
		//}
	}
	
	public GameTexture getRandVariant() {
		return (children.isNotEmpty() && RandomUtil.roll(0, 0, 1)) ? getRandChild() : this;
	}
	
	public GameTexture getChild(int id) {
		return children.getA(id);
	}
	
}
