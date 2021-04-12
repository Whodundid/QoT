package gameSystems.textureSystem;

import eutil.EUtil;
import org.lwjgl.opengl.GL11;
import randomUtil.RandomUtil;
import storageUtil.EArrayList;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;

public class GameTexture {
	
	private GameTexture parentTexture;
	protected StorageBoxHolder<GameTexture, Integer> children = new StorageBoxHolder<GameTexture, Integer>();
	private String name;
	private int percent = -1;
	private int textureID = -1; //-1 indicates this texutre is currently unregistered
	private int childID = 0;
	private String filePath;
	private int width = -1, height = -1;
	private boolean destroyed = false;
	private int[] total = new int[100];
	
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
