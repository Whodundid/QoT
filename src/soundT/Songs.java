package soundT;

import util.storageUtil.EArrayList;

/** This contains every song in the game! */
public class Songs {
	
	private static EArrayList<Audio> currentlyPlaying = new EArrayList();
	
	public static Audio darkCave = new Audio("Dark Cave.wav");
	public static Audio field = new Audio("Fields of Flowers.wav");
	public static Audio larkens = new Audio("Larkens.wav");
	public static Audio nightfall = new Audio("Nightfall.wav");
	public static Audio lithinburg = new Audio("Lithinburg.wav");
	public static Audio theme = new Audio("QuestOfThyrahTheme.wav");
	public static Audio rejuvination = new Audio("Rejuvination.wav");
	public static Audio bossTheme = new Audio("Rising Chords.wav");
	public static Audio battleTheme = new Audio("Uplifting.wav");
	public static Audio varthums = new Audio("Varthums.wav");
	public static Audio zarus = new Audio("Zarus.wav");
	
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