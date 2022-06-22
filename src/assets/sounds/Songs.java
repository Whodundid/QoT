package assets.sounds;

import engine.soundEngine.Audio;
import engine.soundEngine.SoundEngine;

/** This contains every song in the game! */
public class Songs {
	
	private static final String base = "resources/sounds/";
	
	public static Audio darkCave = new Audio("dark_cave", base, "Dark Cave.wav");
	public static Audio field = new Audio("field", base, "Fields of Flowers.wav");
	public static Audio larkens = new Audio("larkens", base, "Larkens.wav");
	public static Audio nightfall = new Audio("nightfall", base, "Nightfall.wav");
	public static Audio lithinburg = new Audio("lithinburg", base, "Lithinburg.wav");
	public static Audio theme = new Audio("theme", base, "QuestOfThyrahTheme.wav");
	public static Audio rejuvination = new Audio("rejuvination", base, "Rejuvination.wav");
	public static Audio bossTheme = new Audio("boss_theme", base, "Rising Chords.wav");
	public static Audio battleTheme = new Audio("battle_theme", base, "Uplifting.wav");
	public static Audio varthums = new Audio("varthums", base, "Varthums.wav");
	public static Audio zarus = new Audio("zarus", base, "Zarus.wav");
	
	static {
		SoundEngine.register(darkCave);
		SoundEngine.register(field);
		SoundEngine.register(larkens);
		SoundEngine.register(nightfall);
		SoundEngine.register(lithinburg);
		SoundEngine.register(theme);
		SoundEngine.register(rejuvination);
		SoundEngine.register(bossTheme);
		SoundEngine.register(battleTheme);
		SoundEngine.register(varthums);
		SoundEngine.register(zarus);
	}
	
}