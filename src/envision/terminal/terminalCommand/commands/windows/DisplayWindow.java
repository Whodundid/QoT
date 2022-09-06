package envision.terminal.terminalCommand.commands.windows;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import envision.windowLib.windowTypes.WindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

public class DisplayWindow extends TerminalCommand {
	
	public DisplayWindow() {
		super(CommandType.NORMAL);
		setCategory("Windows");
		numArgs = 1;
	}

	@Override public String getName() { return "display"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the visibility of a window"; }
	@Override public String getUsage() { return "ex: display 4 (where 4 is the window id)"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.error("Not enough arguments!");
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EArrayList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
				
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
					
					EArrayList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
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
		else {
			termIn.error("Not enough arguments!");
		}
	}
	
}
