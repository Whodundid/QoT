package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ClearObjects extends TerminalCommand {
	
	public CMD_ClearObjects() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "clearobj"; }
	@Override public EList<String> getAliases() { return EList.of("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
	    
		EList<IWindowObject> objs = getTopParent().getCombinedChildren();
		if (objs.isEmpty()) return null;
		
		if (objs.contains(term())) objs.remove(term());
		
		writeln("Closing Renderer Objects..", 0xff00ffff);
		for (var o : objs) {
			if (o.isClosable()) {
				if (runVisually()) writeln("Closing: " + o, 0xffffff00);
				o.close();
			}
		}
		
		if (runVisually()) writeln(objs.size() + " closed.", 0xffffaa00);
		return null;
	}
	
}
