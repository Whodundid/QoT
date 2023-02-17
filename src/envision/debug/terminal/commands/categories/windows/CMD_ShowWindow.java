package envision.debug.terminal.commands.categories.windows;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.windows.windowTypes.WindowParent;
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
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.error("Not enough arguments!");
			return;
		}
		
		try {
			long pid = Long.parseLong(args.get(0));
			EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
			
			WindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
			if (theWindow != null) {
				boolean val = theWindow.isHidden();
				theWindow.setHidden(!val);
				if (!val) theWindow.bringToFront();
				termIn.writeln(EColors.green, "Window: [", theWindow.getObjectName(), " | ", theWindow.getObjectID(), "] made ", (!val ? "in" : ""), "visible.");
			}
			else termIn.error("No window with that id currently exists!");
			
		}
		catch (Exception e) {
			try {
				String name = EStringUtil.combineAll(args, " ").trim();
				
				EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
				WindowParent theWindow = null;
				
				for (WindowParent p : windows) {
					if (p.getObjectName().toLowerCase().equals(name)) {
						theWindow = p;
						break;
					}
				}
				
				if (theWindow != null) {
					boolean val = theWindow.isHidden();
					theWindow.setHidden(!val);
					if (!val) theWindow.bringToFront();
					termIn.writeln(EColors.green, "Window: [", theWindow.getObjectName(), " | ", theWindow.getObjectID(), "] made ", (!val ? "in" : ""), "visible.");
				}
				else termIn.error("No window with that name currently exists!");
			}
			catch (Exception q) {
				termIn.error("Failed to parse input!");
				error(termIn, q);
			}
		}
	}
	
}
