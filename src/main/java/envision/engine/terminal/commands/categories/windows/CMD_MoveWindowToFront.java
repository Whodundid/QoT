package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_MoveWindowToFront extends TerminalCommand {
	
	public CMD_MoveWindowToFront() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "tofront"; }
	@Override public EList<String> getAliases() { return EList.of("front"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Brings a window to the front"; }
	@Override public String getUsage() { return "ex: tofront 4 (where 4 is the window pid)"; }
	
	@Override
	public Object runCommand() {
	    expectNoMoreThan(1);
	    
	    if (noArgs()) {
	        term().bringToFront();
            writeln("Window: [" + term().getObjectName() + " | " + term().getObjectID() + "] brought to front.", EColors.green);
            return null;
	    }
	    
	    try {
            long pid = Long.parseLong(firstArg());
            EList<IWindowParent> windows = getTopParent().getAllActiveWindows();
            
            IWindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
            if (theWindow != null) {
                theWindow.bringToFront();
                writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] brought to front.", EColors.green);
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
                    theWindow.bringToFront();
                    writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] brought to front.", EColors.green);
                }
                else error("No window with that name currently exists!");
            }
            catch (Exception q) {
                error("Failed to parse input!");
                error(q);
            }
        }
	    
	    return null;
	}
	
}