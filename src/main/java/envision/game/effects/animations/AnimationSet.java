package envision.game.effects.animations;

import envision.engine.registry.types.Sprite;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.datatypes.util.EList;

/**
 * An AnimationSet comprises a single set of keyframes that pertain to
 * an individual action or event.
 * <p>
 * Each animation can specify its own rate at which it cycles through
 * its frames.
 * 
 * @author Hunter Bragg
 */
public class AnimationSet {
	
	//--------
	// Fields
	//--------
	
	/** The name of this animation set -- can be modified. */
	private String setName = "My New Animation";
	/** The set of frames for this animation. */
	private final EList<Sprite> frames = EList.newList();
	/** 40 game ticks by default -- can be modified. */
	private long updateInterval = 40;
	
	//--------------
	// Constructors
	//--------------
	
	public AnimationSet(String setNameIn) {
		setName = setNameIn;
	}
	
	public AnimationSet(String setNameIn, EList<Sprite> framesIn) {
		setName = setNameIn;
		frames.addAll(framesIn);
	}
	
	public AnimationSet(String setNameIn, EList<Sprite> framesIn, long updateIntervalIn) {
		setName = setNameIn;
		frames.addAll(framesIn);
		updateInterval = updateIntervalIn;
	}
	
	//---------
	// Methods
	//---------
	
	public void addFrame(GameTexture tex) {
		addFrame(new Sprite(tex));
	}
	
	public void addFrame(Sprite sprite) {
	    frames.add(sprite);
	}
	
	public void addFrameAtIndex(GameTexture tex, int index) {
		addFrameAtIndex(new Sprite(tex), index);
	}
	
	public void addFrameAtIndex(Sprite sprite, int index) {
	    frames.add(index, sprite);
	}
	
	public void clearFrames() {
		frames.clear();
	}
	
	//---------
	// Getters
	//---------
	
	public boolean isEmpty() { return frames.isEmpty(); }
	public boolean isNotEmpty() { return frames.isNotEmpty(); }
	public int length() { return frames.size(); }
	
	public String getSetName() { return setName; }
	public long getUpdateInterval() { return updateInterval; }
	public EList<Sprite> getFrames() { return frames; }
	public Sprite getFrameAtIndex(int index) { return frames.get(index); }
	
	//---------
	// Setters
	//---------
	
	public void setSetName(String nameIn) { setName = nameIn; }
	public void setFrameAtIndex(GameTexture tex, int index) { setFrameAtIndex(new Sprite(tex), index); }
	public void setFrameAtIndex(Sprite sprite, int index) { frames.set(index, sprite); }
	public void setUpdateInterval(long intervalIn) { updateInterval = intervalIn; }
}
