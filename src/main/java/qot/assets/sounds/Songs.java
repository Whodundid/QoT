package qot.assets.sounds;

import envision.game.effects.sounds.Audio;
import envision.game.effects.sounds.SoundEngine;
import eutil.datatypes.EArrayList;
import qot.settings.QoTSettings;

/** This contains every song in the game! */
public class Songs {
	
	private static final String soundsDir = QoTSettings.getResourcesDir().toString() + "\\sounds\\";
	public static final EArrayList<Audio> songList = new EArrayList<>();
	
	public static Audio darkCave = new Audio("dark_cave", soundsDir, "Dark Cave.wav");
	public static Audio field = new Audio("field", soundsDir, "Fields of Flowers.wav");
	public static Audio larkens = new Audio("larkens", soundsDir, "Larkens.wav");
	public static Audio nightfall = new Audio("nightfall", soundsDir, "Nightfall.wav");
	public static Audio lithinburg = new Audio("lithinburg", soundsDir, "Lithinburg.wav");
	public static Audio theme = new Audio("theme", soundsDir, "QuestOfThyrahTheme.wav");
	public static Audio rejuvination = new Audio("rejuvination", soundsDir, "Rejuvination.wav");
	public static Audio bossTheme = new Audio("boss_theme", soundsDir, "Rising Chords.wav");
	public static Audio battleTheme = new Audio("battle_theme", soundsDir, "Uplifting.wav");
	public static Audio varthums = new Audio("varthums", soundsDir, "Varthums.wav");
	public static Audio zarus = new Audio("zarus", soundsDir, "Zarus.wav");
	public static Audio menu = new Audio("menu", soundsDir, "q3452.mid");
	
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
		SoundEngine.register(menu);
		
		songList.add(darkCave);
		songList.add(field);
		songList.add(larkens);
		songList.add(nightfall);
		songList.add(lithinburg);
		songList.add(theme);
		songList.add(rejuvination);
		songList.add(bossTheme);
		songList.add(battleTheme);
		songList.add(varthums);
		songList.add(zarus);
		songList.add(menu);
	}
	
}