package engine.animationHandler;

import java.util.HashMap;

import engine.renderEngine.textureSystem.GameTexture;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import game.entities.Entity;

/**
 * Simplistic keyframe animation system that uses game ticks to know
 * when to advance to the next frame.
 */
public class AnimationHandler {
	
	//-----------------------------------
	// Commonly Used Animation Set Names
	//-----------------------------------
	
	public static final String IDLE_ANIMATION_1 = "idle1";
	public static final String IDLE_ANIMATION_2 = "idle2";
	public static final String IDLE_ANIMATION_3 = "idle3";
	
	public static final String WALKING_1 = "walk1";
	public static final String WALKING_2 = "walk2";
	public static final String WALKING_3 = "walk3";
	
	public static final String ATTACK_1 = "att1";
	public static final String ATTACK_2 = "att2";
	public static final String ATTACK_3 = "att3";
	
	//--------
	// Fields
	//--------
	
	/** The entity for which this animation pertains to. */
	private final Entity theEntity;
	/** The original Entity's game texture. */
	private final GameTexture origTex;
	/** The set of all keyframes on this animation. */
	private final HashMap<String, AnimationSet> animationSets = new HashMap<>();
	/** The current game tick continuously counting up. */
	private long curGameTick = 0;
	/** Indicates whether or not the animation will continue to update over time. */
	private boolean playing = false;
	
	//-------------------------------------
	
	/** The current animation set. */
	private AnimationSet currentAnimation;
	/** The current animation frame. */
	private int frameIndex = -1;
	/** Indicates that the current animation will be unloaded after it finishes playing. */
	private boolean stopAtEnd;
	
	//-------------------------------------
	
	//--------------
	// Constructors
	//--------------
	
	public AnimationHandler(Entity entIn) {
		theEntity = entIn;
		origTex = theEntity.getTexture();
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns the current keyframe aligning with the current animation
	 * set's update interval.
	 */
	public void onRenderTick() {
		var t = update();
		theEntity.setTexture(t);
	}
	
	private GameTexture update() {
		//If there is no animation to play, just return the entity's base texture
		if (currentAnimation == null) return origTex;
		
		//if the animation is actively playing, continue to update animation frames
		if (playing) {
			curGameTick++;
			if (curGameTick >= currentAnimation.getUpdateInterval()) {
				curGameTick = 0;
				advanceIndex();
			}
		}
		
		return getNextTexture();
	}
	
	public AnimationSet createAnimationSet(String animationName) {
		if (animationSets.containsKey(animationName)) return null;
		return getSetInternal(animationName);
	}
	
	/**
	 * Resumes the current animation.
	 * 
	 * @return True if successful
	 */
	public boolean play() {
		if (currentAnimation == null || currentAnimation.isEmpty()) return false;
		playing = true;
		return true;
	}
	
	/**
	 * Pauses the current animation.
	 * 
	 * @return True if successful
	 */
	public boolean pause() {
		if (currentAnimation == null || currentAnimation.isEmpty()) return false;
		playing = false;
		return true;
	}
	
	public boolean stop() {
		if (currentAnimation == null || currentAnimation.isEmpty()) return false;
		theEntity.setTexture(origTex);
		playing = false;
		return true;
	}
	
	public void stopOnceAnimationEnds() {
		if (currentAnimation == null || currentAnimation.isEmpty()) return;
	}
	
	public void playOnceIfNotAlreadyPlaying(String setName) {
		if (currentAnimation != null && currentAnimation.getSetName().equals(setName)) return;
		if (loadAnimation(setName)) {
			stopAtEnd = true;
			play();
		}
	}
	
	public boolean playIfNotAlreadyPlaying(String setName) {
		if (currentAnimation == null) loadAnimation(setName);
		if (currentAnimation == null || currentAnimation.isEmpty()) return false;
		play();
		return true;
	}
	
	public boolean loadAnimation(String setName) {
		return setCurrentAnimation(setName);
	}
	
	public boolean unloadAnimation() {
		return setCurrentAnimation(null);
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the entity for which this animation handler pertains to.
	 * 
	 * @return The entity
	 */
	public Entity getEntity() {
		return theEntity;
	}
	
	/**
	 * Returns the base texture of the entity.
	 */
	public GameTexture getEntitysBaseTexture() {
		return origTex;
	}
	
	/**
	 * Returns the currently loaded animation set's game tick update
	 * interval.
	 * <p>
	 * If no animation is currently loaded, then -1 is returned by
	 * default.
	 * 
	 * @return The current animation's game tick update interval
	 */
	public long getCurrentUpdateInterval() {
		return (currentAnimation != null) ? currentAnimation.getUpdateInterval() : -1;
	}
	
	/**
	 * Returns the current animation. (May be null)
	 */
	public AnimationSet getCurrentAnimation() {
		return currentAnimation;
	}
	
	/**
	 * Returns the current animation's frame index.
	 * <p>
	 * This could be -1 is no animation is currently loaded or if there
	 * are no frames in the current animation.
	 */
	public int getCurrentFrameIndex() {
		return frameIndex;
	}
	
	/**
	 * Returns the current texture associated with the immediate frame index.
	 */
	public GameTexture getCurrentFrameTexture() {
		if (currentAnimation == null) return null;
		if (currentAnimation.isEmpty()) return null;
		return currentAnimation.getFrameAtIndex(frameIndex);
	}
	
	/**
	 * Returns the number of textures (frames) associated with the
	 * currently loaded animation.
	 * <p>
	 * If no animation is current loaded, then -1 is returned by default.
	 * 
	 * @return The number of frames on the current animation
	 */
	public int getCurrentAnimationLength() {
		return (currentAnimation != null) ? currentAnimation.length() : -1;
	}
	
	/**
	 * @return True if there is an animation currently loaded.
	 */
	public boolean isAnimationLoaded() {
		return currentAnimation != null;
	}
	
	/**
	 * Returns true if there is an actively loaded animation and it is
	 * currently playing.
	 */
	public boolean isPlaying() {
		return currentAnimation != null && currentAnimation.isNotEmpty() && playing;
	}
	
	/**
	 * Returns true if there is an actively loaded animation and it is
	 * not currently advancing frames.
	 */
	public boolean isPaused() {
		return currentAnimation != null && currentAnimation.isNotEmpty() && !playing;
	}
	
	/**
	 * Returns a list containing every animation set stored within this
	 * handler.
	 */
	public EList<AnimationSet> getAllAnimations() {
		EList<AnimationSet> r = new EArrayList<>();
		for (var s : animationSets.keySet())
			r.add(animationSets.get(s));
		return r;
	}
	
	//---------
	// Setters
	//---------
	
	/**
	 * Attempts to set the current animation set for this handler. A
	 * successful switch returns true.
	 * <p>
	 * If 'null' is passed for the setName, then the current animation is
	 * unloaded and true is returned.
	 * <p>
	 * False is only ever returned if attempting to switch to a
	 * non-existent set under the given name.
	 * 
	 * @param setName The name of the set to switch to
	 * @return True if successfully set
	 */
	public boolean setCurrentAnimation(String setName) {
		//if null -- unload current and return true
		if (setName == null) {
			unloadCurrentWorkingSet();
			return true;
		}
		//otherwise attempt to switch to the new set
		return changeWorkingSet(setName);
	}
	
	/**
	 * Sets the currently loaded animation's key frame index.
	 * 
	 * @param index The keyframe index to set
	 */
	public void setCurrentFrame(int index) {
		if (currentAnimation == null) return;
		frameIndex = index;
	}
	
	/**
	 * Retrieves the game tick update interval for the specified
	 * animation.
	 * <p>
	 * Returns -1 if no set under the given name was found.
	 * 
	 * @param setName The name of the animation
	 * @return The animation set's game tick update interval
	 */
	public long getSetUpdateInterval(String setName) {
		var set = getSetInternal(setName);
		return (set != null) ? set.getUpdateInterval() : -1;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private boolean changeWorkingSet(String newSet) {
		unloadCurrentWorkingSet();
		return loadAnimationSet(newSet);
	}
	
	private void unloadCurrentWorkingSet() {
		theEntity.setTexture(origTex);
		currentAnimation = null;
		frameIndex = -1;
	}
	
	private boolean loadAnimationSet(String setName) {
		var set = getSetInternal(setName);
		if (set == null) return false;
		
		currentAnimation = set;
		frameIndex = (currentAnimation.isNotEmpty()) ? 0 : -1;
		return true;
	}
	
	private void advanceIndex() {
		//-1 if either null or empty
		if (currentAnimation == null || currentAnimation.isEmpty()) {
			frameIndex = -1;
			return;
		}
		//otherwise attempt to either wrap around back to 0 or increment
		if (frameIndex >= currentAnimation.length() - 1) {
			if (stopAtEnd) {
				unloadAnimation();
				stopAtEnd = false;
			}
			else frameIndex = 0;
		}
		else frameIndex++;
	}
	
	private AnimationSet getSetInternal(String setName) {
		var set = animationSets.get(setName);
		//if the set doesn't already exist, create it and put it in the set map
		if (set == null) {
			set = new AnimationSet(setName);
			animationSets.put(setName, set);
		}
		return set;
	}
	
	private GameTexture getNextTexture() {
		if (currentAnimation == null) return origTex;
		return (currentAnimation.isNotEmpty()) ? currentAnimation.getFrameAtIndex(frameIndex) : null;
	}
	
}
