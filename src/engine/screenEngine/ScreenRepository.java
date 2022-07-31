package engine.screenEngine;

import eutil.datatypes.EArrayList;
import game.screens.primary.MainMenuScreen;
import game.screens.primary.OptionsScreen;
import game.screens.primary.WorldSelectScreen;
import world.mapEditor.MapMenuScreen;

import java.lang.reflect.Constructor;

/**
 * A static collection of GameScreens intended for up-front use in one
 * location so that they are accessible through the
 * terminal::OpenScreen command.
 * 
 * @author Hunter Bragg
 */
public class ScreenRepository {
	
	//------------------------------------
	
	/** Hidden Constructor */
	private ScreenRepository() {}
	
	//------------------------------------
	
	private static final EArrayList<GameScreen<?>> screens = new EArrayList();
	
	//------------------------------------

	static {
		screens.add(new MainMenuScreen());
		screens.add(new OptionsScreen());
		screens.add(new WorldSelectScreen());
		screens.add(new MapMenuScreen());
	}
	
	//------------------------------------
	
	/**
	 * Adds a new GameScreen to this repository. Duplicate or null entries
	 * are ignored.
	 * 
	 * @param screen The GameScreen to be registered
	 */
	public static void registerScreen(GameScreen screen) {
		if (screen == null) return;
		screens.addIfNotContains(screen);
	}
	
	/**
	 * Returns a registered GameScreen that either matches by name or by
	 * alias.
	 * 
	 * @param nameOrAlias
	 * @return Matching registered GameScreen
	 */
	public static GameScreen getScreen(String nameOrAlias) {
		Class<? extends GameScreen> c = getScreenClass(nameOrAlias);
		if (c == null) return null;
		
		try {
			Constructor<? extends GameScreen> con = c.getConstructor();
			return con.newInstance();
		}
		catch (NoSuchMethodException e) {}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns the matching screen class for which a new instance will be
	 * created from.
	 * 
	 * @param nameOrAlias
	 * @return
	 */
	private static Class<? extends GameScreen> getScreenClass(String nameOrAlias) {
		for (GameScreen<?> s : screens) {
			String name = s.getClass().getSimpleName();
			if (matchAlias(s, nameOrAlias) || name.equalsIgnoreCase(nameOrAlias)) {
				return s.getClass();
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the given GameScreen has an alias that matches the
	 * one provided.
	 * 
	 * @param s The GameScreen
	 * @param alias The alias to check for
	 * @return True if the GameScreen contains alias
	 */
	private static boolean matchAlias(GameScreen<?> s, String alias) {
		for (String a : s.getAliases()) {
			if (a.equalsIgnoreCase(alias)) return true;
		}
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * @return A copy of the registered screens list
	 */
	public static EArrayList<GameScreen> getRegisteredScreens() {
		return new EArrayList(screens);
	}
	
}
