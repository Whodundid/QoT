package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_PinWindow extends TerminalCommand {
	
	public CMD_PinWindow() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "pin"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pins or unpins a specified window to or from the hud."; }
	@Override public String getUsage() { return "ex: pin 4 (where 4 is the window pid)"; }
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(1);
	    
	    if (noArgs()) {
	        boolean val = term().isPinned();
	        term().setPinned(!val);
            
            if (!val) {
                term().setHidden(false);
                term().bringToFront();
            }
            
            writeln(String.format("Window: [%s] | [%d] %spinned",
                term().getObjectName(),
                term().getObjectID(),
                val ? "un" : ""),
                EColors.green);
            return;
	    }

		try {
			long pid = Long.parseLong(firstArg());
			EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
			
			IWindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
			if (theWindow != null) {
				boolean val = theWindow.isPinned();
				theWindow.setPinned(!val);
				if (!val) {
					theWindow.setHidden(false);
					theWindow.bringToFront();
				}
				writeln(String.format("Window: [%s] | [%d] %spinned",
											 theWindow.getObjectName(),
											 theWindow.getObjectID(),
											 val ? "un" : ""), 
							   EColors.green);
			}
			else error("No window with that pid currently exists!");
			
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
					boolean val = theWindow.isPinned();
					theWindow.setPinned(!val);
					writeln(String.format("Window: [%s] | [%d] %spinned",
							 					 theWindow.getObjectName(),
							 					 theWindow.getObjectID(),
							 					 val ? "un" : ""), 
								   EColors.green);
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
