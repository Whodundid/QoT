package envision.debug.debugCommands;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public abstract class DebugCommand {
	
	public EList<Object> list = new EArrayList<>();
	public double dVal0 = 0;
	public double dVal1 = 0;
	public double dVal2 = 0;
	public boolean bool0 = false;
	public boolean bool1 = false;
	public boolean bool2 = false;
	
	public abstract void run(ETerminalWindow termIn, Object... args) throws Exception;
	
	public void onRendererUpdate() {}
	
}
