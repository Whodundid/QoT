package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
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
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			boolean val = termIn.isPinned();
			termIn.setPinned(!val);
			
			if (!val) {
				termIn.setHidden(false);
				termIn.bringToFront();
			}
			
			termIn.writeln(String.format("Window: [%s] | [%d] %spinned",
					 					 termIn.getObjectName(),
					 					 termIn.getObjectID(),
					 					 val ? "un" : ""), 
						   EColors.green);
			return;
		}

		try {
			long pid = Long.parseLong(args.get(0));
			EList<IWindowParent> windows = termIn.getTopParent().getAllActiveWindows();
			
			IWindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
			if (theWindow != null) {
				boolean val = theWindow.isPinned();
				theWindow.setPinned(!val);
				if (!val) {
					theWindow.setHidden(false);
					theWindow.bringToFront();
				}
				termIn.writeln(String.format("Window: [%s] | [%d] %spinned",
											 theWindow.getObjectName(),
											 theWindow.getObjectID(),
											 val ? "un" : ""), 
							   EColors.green);
			}
			else termIn.error("No window with that pid currently exists!");
			
		}
		catch (Exception e) {
			try {
				String name = EStringUtil.combineAll(args, " ").trim();
				
				EList<IWindowParent> windows = termIn.getTopParent().getAllActiveWindows();
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
					termIn.writeln(String.format("Window: [%s] | [%d] %spinned",
							 					 theWindow.getObjectName(),
							 					 theWindow.getObjectID(),
							 					 val ? "un" : ""), 
								   EColors.green);
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
