package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_CloseWindow extends TerminalCommand {
	
	public CMD_CloseWindow() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "close"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to close a specific window"; }
	@Override public String getUsage() { return "ex: close 23 (where 23 is the window pid)"; }
	
	@Override
	public void runCommand() {
	    if (noArgs()) {
	        term().close();
	        return;
	    }
	    
	    try {
            long pid = Long.parseLong(firstArg());
            EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
            
            IWindowParent theWindow = EUtil.getFirst(windows, w -> w.getWindowID() == pid);
            if (EUtil.nullDo(theWindow, w -> w.close())) {
                writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getWindowID() + "' closed", EColors.green);
            }
            else error("No window with that pid currently exists!");
            
        }
        catch (Exception e) {
            try {
                String name = EStringUtil.combineAll(args(), " ").toLowerCase().trim();
                EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
                
                if (name.equals("all") ) {
                    for (IWindowParent p : windows) {
                        if (p != term()) {
                            p.close();
                            writeln("Window: '" + p.getObjectName() + " ; " + p.getWindowID() + "' closed", EColors.green);
                        }
                    }
                }
                else if (name.equals("all+")) {
                    for (IWindowParent p : windows) {
                        p.close();
                    }
                    getTopParent().displayWindow(null);
                }
                else {
                    IWindowParent theWindow = null;
                    for (IWindowParent p : windows) {
                        if (p.getObjectName().toLowerCase().equals(name)) {
                            theWindow = p;
                            break;
                        }
                    }
                    
                    if (EUtil.nullDo(theWindow, w -> w.close())) {
                        writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getWindowID() + "' closed", EColors.green);
                    }
                    else error("No window with that name currently exists!");
                }
            }
            catch (Exception q) {
                error("Failed to parse input!");
                error(q);
            }
        }
	}
	
}