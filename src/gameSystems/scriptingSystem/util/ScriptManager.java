package gameSystems.scriptingSystem.util;

import util.storageUtil.EArrayList;

public class ScriptManager {
	
	static EArrayList<ScriptRunner> running = new EArrayList();
	static EArrayList<ScriptRunner> adding = new EArrayList();
	static EArrayList<ScriptRunner> dying = new EArrayList();
	
	public static void register(ScriptRunner in) {
		adding.add(in);
	}
	
	public static void unregister(ScriptRunner in) {
		dying.add(in);
	}
	
	public static void update() {
		if (dying.isNotEmpty()) { running.removeAll(dying); dying.clear(); }
		if (adding.isNotEmpty()) { running.addAll(adding); adding.clear(); }
		
		updateScripts();
	}
	
	private static void updateScripts() {
		for (ScriptRunner runner : running) {
			runner.update();
		}
	}
	
	public static int getTotal() { return adding.size() + running.size(); }
	public static boolean hasScripts() { return getTotal() > 0; }

}
