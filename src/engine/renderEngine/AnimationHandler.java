package engine.renderEngine;

import engine.renderEngine.textureSystem.GameTexture;
import eutil.datatypes.EArrayList;
import game.entities.Entity;

/**
 * Simplistic keyframe animation system that uses game ticks to know
 * when to advance to the next frame.
 */
public class AnimationHandler {
	
	//--------
	// Fields
	//--------
	
	/** The entity for which this animation pertains to. */
	private final Entity theEntity;
	/** The set of all keyframes on this animation. */
	private final EArrayList<GameTexture> keyframes = new EArrayList<>();
	/** Interval measured in game ticks (not seconds/ms). */
	private long updateInterval = 40;
	/** The current game tick continuously counting up. */
	private long curGameTick = 0;
	/** The current keyframe's index. */
	private int frameIndex = -1;
	
	//--------------
	// Constructors
	//--------------
	
	public AnimationHandler(Entity entIn) {
		theEntity = entIn;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns the current keyframe aligning with the animation's update
	 * interval.
	 */
	public GameTexture getFrameTick() {
		curGameTick++;
		if (curGameTick >= updateInterval) {
			curGameTick = 0;
			advanceIndex();
		}
		return (keyframes.isEmpty()) ? null : keyframes.get(frameIndex);
	}
	
	public void addFrame(GameTexture tex) {
		keyframes.add(tex);
	}
	
	public void addFrameAtIndex(GameTexture tex, int index) {
		keyframes.add(index, tex);
	}
	
	public void clearFrames() {
		keyframes.clear();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void advanceIndex() {
		if (keyframes.isEmpty()) frameIndex = -1;
		else if (frameIndex >= keyframes.size() - 1) frameIndex = 0;
		else frameIndex++;
	}
	
	//---------
	// Getters
	//---------
	
	public Entity getEntity() {
		return theEntity;
	}
	
	public long getUpdateInterval() {
		return updateInterval;
	}
	
	public EArrayList<GameTexture> getFrames() {
		return keyframes;
	}
	
	//---------
	// Setters
	//---------
	
	public void setUpdateInterval(long intervalIn) {
		updateInterval = intervalIn;
	}
	
	public void setFrameAtIndex(GameTexture tex, int index) {
		keyframes.set(index, tex);
	}
	
}
