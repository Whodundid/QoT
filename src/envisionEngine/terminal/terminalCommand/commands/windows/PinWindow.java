package envisionEngine.terminal.terminalCommand.commands.windows;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowLib.windowTypes.WindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class PinWindow extends TerminalCommand {
	
	public PinWindow() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "pin"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pins or unpins a specified window to or from the hud."; }
	@Override public String getUsage() { return "ex: pin 4 (where 4 is the window pid)"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
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
		}
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
				
				WindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
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
					
					EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
					WindowParent theWindow = null;
					
					for (WindowParent p : windows) {
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
		else {
			termIn.error("Not enough arguments!");
		}
	}
	
}
