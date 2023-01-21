package envisionEngine.terminal.terminalCommand.commands.windows;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowLib.windowTypes.WindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Close extends TerminalCommand {
	
	public Close() {
		setCategory("Windows");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "close"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to close a specific window"; }
	@Override public String getUsage() { return "ex: close 23 (where 23 is the window pid)"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.close();
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
				
				WindowParent theWindow = EUtil.getFirst(windows, w -> w.getWindowID() == pid);
				if (EUtil.nullDo(theWindow, w -> w.close())) {
					termIn.writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getObjectID() + "' closed", EColors.green);
				}
				else termIn.error("No window with that pid currently exists!");
				
			}
			catch (Exception e) {
				try {
					String name = EStringUtil.combineAll(args, " ").toLowerCase().trim();
					EList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
					
					if (name.equals("all") ) {
						for (WindowParent p : windows) {
							if (p != termIn) {
								p.close();
								termIn.writeln("Window: '" + p.getObjectName() + " ; " + p.getObjectID() + "' closed", EColors.green);
							}
						}
					}
					else if (name.equals("all+")) {
						for (WindowParent p : windows) {
							p.close();
						}
						termIn.getTopParent().displayWindow(null);
					}
					else {
						WindowParent theWindow = null;
						for (WindowParent p : windows) {
							if (p.getObjectName().toLowerCase().equals(name)) {
								theWindow = p;
								break;
							}
						}
						
						if (EUtil.nullDo(theWindow, w -> w.close())) {
							termIn.writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getObjectID() + "' closed", EColors.green);
						}
						else termIn.error("No window with that name currently exists!");
					}
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