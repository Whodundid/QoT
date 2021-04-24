package renderEngine.textureSystem;

import eutil.EUtil;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import randomUtil.RandomUtil;
import storageUtil.EArrayList;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;

public class GameTexture {
	
	/** If this texture is a child variant, this is the parent for which it relates. If this value is null, there is no parent and this is not a child texture. */
	private GameTexture parentTexture;
	/** A collection of this texture's child variants. */
	protected StorageBoxHolder<GameTexture, Integer> children = new StorageBoxHolder<GameTexture, Integer>();
	/** The internal name of this texture. */
	private String name;
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
	/** An internal use BufferedImage containing the coresponding texture. -- Assigned dynamically through texture registration. */
	private BufferedImage bi;
	
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
	
	//-------------------
	// Protected Methods
	//-------------------
	
	protected void registerChildTextures(TextureSystem systemIn) {}
	
	//----------------
	// Public Methods
	//----------------
	
	public GameTexture setChildID(int id) { childID = id; return this; }
	public GameTexture addChild(final GameTexture in) { return addChild(in, -1); }
	public GameTexture addChild(final GameTexture in, final int percentage) {
		if (in != null) {
			in.parentTexture = this;
			children.add(in, percentage);
		}
		return this;
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
	
	public boolean hasBeenRegistered() { return textureID != -1; }
	
	//---------------------
	// GameTexture Getters
	//---------------------
	
	public GameTexture getParent() { return parentTexture; }
	public EArrayList<GameTexture> getChildren() { return children.getAVals(); }
	public String getName() { return name; }
	public int getTextureID() { return textureID; }
	public int getChildID() { return childID; }
	public String getFilePath() { return filePath; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean hasBeenDestroyed() { return destroyed; }
	public boolean hasParent() { return parentTexture != null; }
	public BufferedImage GBI() { return bi; }
	
	public EArrayList<GameTexture> getAllChildren() {
		EArrayList<GameTexture> found = new EArrayList();
		EArrayList<GameTexture> withChildren = new EArrayList();
		EArrayList<GameTexture> workList = new EArrayList();
		
		EUtil.forEach(children.getAVals(), c -> { found.add(c); if (c.children.size() > 0) { withChildren.add(c); } });
		withChildren.forEach(c -> workList.add(c));
		
		while (workList.isNotEmpty()) {
			found.addAll(workList);
			
			withChildren.clear();
			workList.filterForEach(c -> c.children.getAVals().length() > 0, withChildren::add);
			
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
		StorageBoxHolder<GameTexture, Integer> total = new StorageBoxHolder(children);
		total.add(this, percent);
		for (StorageBox<GameTexture, Integer> t : total) {
			int amount = t.getB();
		}
	}
	
	public GameTexture getRandVariant() {
		if (children.isNotEmpty()) {
			boolean val = (RandomUtil.roll(0, 0, 1));
			if (val) {
				return getRandChild();
			}
		}
		return this;
	}
	
	public GameTexture getChild(int id) {
		return children.getA(id);
	}
	
}
