package debug.debugCommands;

import storageUtil.EArrayList;
import terminal.window.ETerminal;

public abstract class DebugCommand {
	
	public EArrayList<Object> list = new EArrayList();
	public double dVal0 = 0;
	public double dVal1 = 0;
	public double dVal2 = 0;
	public boolean bool0 = false;
	public boolean bool1 = false;
	public boolean bool2 = false;
	
	public abstract void run(ETerminal termIn, Object... args);
	
	public void onRendererUpdate() {}
	
}
