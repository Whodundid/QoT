package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_ShowWindow extends TerminalCommand {
	
	public CMD_ShowWindow() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "display"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the visibility of a window"; }
	@Override public String getUsage() { return "ex: display 4 (where 4 is the window id)"; }
	
	@Override
	public void runCommand() {
	    expectExactly(1);
		
		try {
			long pid = Long.parseLong(firstArg());
			EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
			
			IWindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
			if (theWindow != null) {
				boolean val = theWindow.isHidden();
				theWindow.setHidden(!val);
				if (!val) theWindow.bringToFront();
				writeln(EColors.green, "Window: [", theWindow.getObjectName(), " | ", theWindow.getObjectID(), "] made ", (!val ? "in" : ""), "visible.");
			}
			else error("No window with that id currently exists!");
			
		}
		catch (Exception e) {
			try {
				String name = EStringUtil.combineAll(args(), " ").trim();
				
				EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
				IWindowParent theWindow = null;
				
				for (IWindowParent p : windows) {
					if (p.getObjectName().toLowerCase().equals(name)) {
						theWindow = p;
						break;
					}
				}
				
				if (theWindow != null) {
					boolean val = theWindow.isHidden();
					theWindow.setHidden(!val);
					if (!val) theWindow.bringToFront();
					writeln(EColors.green, "Window: [", theWindow.getObjectName(), " | ", theWindow.getObjectID(), "] made ", (!val ? "in" : ""), "visible.");
				}
				else error("No window with that name currently exists!");
			}
			catch (Exception q) {
				error("Failed to parse input!");
				error(q);
			}
		}
	}
	
}
