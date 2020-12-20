package gameSystems.scriptingSystem.util;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.ScriptInterpreter;
import java.io.File;
import java.io.FileNotFoundException;

public class ScriptRunner {
	
	File dataFile;
	ScriptInterpreter interpreter;
	EScript script;
	long interval = 1000;
	boolean loaded = false;
	boolean running = false;
	private long lastUpdate = 0;
	
	public ScriptRunner(File fileIn) { this(fileIn, 1000); }
	public ScriptRunner(File fileIn, long updateInterval) {
		dataFile = fileIn;
		
		try {
			interpreter = new ScriptInterpreter(this, dataFile);
			script = interpreter.getScriptInstance();
			loaded = interpreter.canRun();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (ScriptError e) {
			e.printStackTrace();
		}
		
		interval = updateInterval;
	}
	
	public void update() {
		if (running) {
			if (System.currentTimeMillis() - lastUpdate >= interval) {
				lastUpdate = System.currentTimeMillis();
				
				//attempt to run the next line of code
				
				try {
					if (!script.isDying() && interpreter.canRun()) {
						interpreter.runNextLine();
					}
					else {
						setRunning(false);
						ScriptManager.unregister(this);
					}
				}
				catch (ScriptError e) {
					e.printStackTrace();
					setRunning(false);
					ScriptManager.unregister(this);
				}
				catch (Exception e) {
					e.printStackTrace();
					setRunning(false);
					ScriptManager.unregister(this);
				}
			}
		}
	}
	
	public ScriptRunner setRunning(boolean val) {
		if (val && !running) {
			running = interpreter.rebuild();
		}
		else { running = false; }
		return this;
	}
	
	public boolean isRunning() { return running; }
	
	public ScriptInterpreter getInterpreter() { return interpreter; }

}
