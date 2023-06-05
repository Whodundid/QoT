package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ClearObjects extends TerminalCommand {
	
	public CMD_ClearObjects() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "clearobj"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		EList<IWindowObject> objs = termIn.getTopParent().getCombinedChildren();
		if (objs.isEmpty()) return;
		
		if (objs.contains(termIn)) objs.remove(termIn);
		
		termIn.writeln("Closing Renderer Objects..", 0xff00ffff);
		for (var o : objs) {
			if (o.isClosable()) {
				if (runVisually) termIn.writeln("Closing: " + o, 0xffffff00);
				o.close();
			}
		}
		
		if (runVisually) termIn.writeln(objs.size() + " closed.", 0xffffaa00);
	}
	
}
