package assets.sounds;

import engine.soundEngine.Audio;
import engine.soundEngine.SoundEngine;

/** This contains every song in the game! */
public class Songs {
	
	public static Audio darkCave = new Audio("dark_cave", "bin/sounds/Dark Cave.wav");
	public static Audio field = new Audio("field", "bin/sounds/Fields of Flowers.wav");
	public static Audio larkens = new Audio("larkens", "bin/sounds/Larkens.wav");
	public static Audio nightfall = new Audio("nightfall", "bin/sounds/Nightfall.wav");
	public static Audio lithinburg = new Audio("lithinburg", "bin/sounds/Lithinburg.wav");
	public static Audio theme = new Audio("theme", "bin/sounds/QuestOfThyrahTheme.wav");
	public static Audio rejuvination = new Audio("rejuvination", "bin/sounds/Rejuvination.wav");
	public static Audio bossTheme = new Audio("boss_theme", "bin/sounds/Rising Chords.wav");
	public static Audio battleTheme = new Audio("battle_theme", "bin/sounds/Uplifting.wav");
	public static Audio varthums = new Audio("varthums", "bin/sounds/Varthums.wav");
	public static Audio zarus = new Audio("zarus", "bin/sounds/Zarus.wav");
	
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