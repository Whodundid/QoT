package engine.soundEngine;

import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import main.QoT;
import main.settings.QoTSettings;

/**
 * A static utility class which manages all playable sounds/songs
 * within QoT.
 * 
 * @author Hunter Bragg
 */
public class SoundEngine {
	
	/** A list containing all audio tracks currently being played. */
	private static EArrayList<Audio> currentlyPlaying = new EArrayList();
	/** A collection of every single known audio track in one location. */
	private static final EArrayList<Audio> allSongs = new EArrayList();
	
	//private to prevent instance instantiation
	private SoundEngine() {}
	
	/**
	 * Registers a new Audio track within this sound engine.
	 * Note: Duplicate audio tracks are ignored.
	 * 
	 * @param in
	 */
	public static void register(Audio in) {
		if (in == null) log("Attempted to register NULL track!");
		if (allSongs.contains(in)) log("Track already registered! '" + in.getName() + "'");
		allSongs.add(in);
	}
	
	/**
	 * Stops every audio track currently being played within QoT.
	 */
	public static void stopAll() {
		currentlyPlaying.forEach(s -> s.stop());
		currentlyPlaying.clear();
	}
	
	/**
	 * Returns the collection of all audio tracks currently being played.
	 * 
	 * @return List of currently played audio tracks
	 */
	public static EArrayList<Audio> getAllPlaying() {
		return new EArrayList(currentlyPlaying);
	}
	
	/**
	 * Starts playing the given audio track.
	 * 
	 * @param in
	 * @return
	 */
	public static Audio play(Audio in) {
		if (in != null) {
			if (QoTSettings.musicVolume.get() > 0) in.start();
			currentlyPlaying.add(in);
		}
		return in;
	}
	
	/**
	 * Sets a specific audio track to loop.
	 * 
	 * @param in The Audio track to be looped
	 * @return The given audio track
	 */
	public static Audio loop(Audio in) {
		if (in != null) {
			if (QoTSettings.musicVolume.get() > 0) in.loop();
			currentlyPlaying.add(in);
		}
		return in;
	}
	
	/**
	 * Stops a specific song that is currently playing.
	 */
	public static void stop(Audio in) {
		if (in != null) {
			if (currentlyPlaying.contains(in)) {
				in.stop();
				currentlyPlaying.remove(in);
			}
		}
	}
	
	/*
	 * Attempts to play the given audio track only if it isn't already
	 * being played.
	 */
	public static void playIfNotPlaying(Audio in) {
		if (isNotPlaying(in)) play(in);
	}
	
	/*
	 * Attempts to loop the given audio track only if it isn't already
	 * being played.
	 */
	public static void loopIfNotPlaying(Audio in) {
		if (isNotPlaying(in)) loop(in);
	}
	
	/** Returns true if the current audio track is actively being played. */
	public static boolean isPlaying(Audio in) { return currentlyPlaying.contains(in); }
	/** Returns true if the current audio track is not actively being played. */
	public static boolean isNotPlaying(Audio in) { return currentlyPlaying.notContains(in); }
	/** Returns true if there are any tracks playing. */
	public static boolean anyPlaying() { return currentlyPlaying.isNotEmpty(); }
	
	/**
	 * Sets a specific track to play with the given volume level.
	 * 
	 * @param in
	 * @param volume
	 * @return
	 */
	public static Audio setTrackVolume(Audio in, double volume) {
		if (in == null) {
			log("Attempted to modify the volume of a NULL track!");
			return null;
		}
		
		in.setVolume(volume * 0.0009);
		return in;
	}
	
	/**
	 * Sets the playing volume of all currently playing tracks to the
	 * specified level.
	 * 
	 * @param volume
	 */
	public static void setCurrentTrackVolume(double volume) {
		currentlyPlaying.forEach(s -> s.setVolume(volume * 0.0009));
	}
	
	/** Returns the list of all currently registered audio tracks. */
	public static EArrayList<Audio> getTrackList() { return allSongs; }
	
	/** Returns a registered audio track that matches the given name. */
	public static Audio getTrackByName(String name) {
		for (var s : allSongs) if (s.getName().equals(name)) return s;
		return null;
	}
	
	public static void setMusicVolume(Number amount) { setMusicVolume(amount.intValue()); }
	public static void setMusicVolume(int amount) {
		int val = NumberUtil.clamp(amount, 0, 100);
		int old = QoTSettings.musicVolume.get();
		QoTSettings.musicVolume.set(val);
		//only update config if the volume actually changed
		if (QoTSettings.musicVolume.get() != old) QoTSettings.saveConfig();
	}
	
	public static int getMusicVolume() {
		return QoTSettings.musicVolume.get();
	}
	
	/** Utility logging function. */
	private static void log(String msg) {
		QoT.info("[Sound_Engine] " + msg);
	}
	
}
