package sound;

import util.storageUtil.EArrayList;

/** This contains every song in the game! */
public class Songs {
	
	private static EArrayList<Audio> currentlyPlaying = new EArrayList();
	
	public static Audio darkCave = new Audio("bin/sounds/Dark Cave.wav");
	public static Audio field = new Audio("bin/sounds/Fields of Flowers.wav");
	public static Audio larkens = new Audio("bin/sounds/Larkens.wav");
	public static Audio nightfall = new Audio("bin/sounds/Nightfall.wav");
	public static Audio lithinburg = new Audio("bin/sounds/Lithinburg.wav");
	public static Audio theme = new Audio("bin/sounds/QuestOfThyrahTheme.wav");
	public static Audio rejuvination = new Audio("bin/sounds/Rejuvination.wav");
	public static Audio bossTheme = new Audio("bin/sounds/Rising Chords.wav");
	public static Audio battleTheme = new Audio("bin/sounds/Uplifting.wav");
	public static Audio varthums = new Audio("bin/sounds/Varthums.wav");
	public static Audio zarus = new Audio("bin/sounds/Zarus.wav");
	
	public static void stopAllMusic() {
		currentlyPlaying.forEach(s -> s.stop());
	}
	
	public static EArrayList<Audio> getAllCurrentlyPlaying() {
		return new EArrayList(currentlyPlaying);
	}
	
	public static void playSong(Audio in) {
		if (in != null) {
			in.start();
			currentlyPlaying.add(in);
		}
	}
	
	/** Stops a specific song that is currently playing. */
	public static void stopSong(Audio in) {
		if (currentlyPlaying.contains(in)) {
			in.stop();
			currentlyPlaying.remove(in);
		}
	}
	
}