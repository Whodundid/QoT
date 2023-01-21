package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class ClearObjects extends TerminalCommand {
	
	public ClearObjects() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "clearobj"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		EList<IWindowObject> objs = termIn.getTopParent().getCombinedChildren();
		if (objs.contains(termIn)) objs.remove(termIn);
		if (objs.isNotEmpty()) {
			termIn.writeln("Closing Renderer Objects..", 0xff00ffff);
			for (IWindowObject o : objs) {
				if (o.isClosable()) {
					if (runVisually) termIn.writeln("Closing: " + o, 0xffffff00);
					o.close();
				}
			}
			if (runVisually) termIn.writeln(objs.size() + " closed.", 0xffffaa00);
		}
	}
	
}
