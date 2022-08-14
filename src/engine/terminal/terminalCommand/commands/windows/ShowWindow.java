package engine.terminal.terminalCommand.commands.windows;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EList;
import eutil.strings.StringUtil;

public class ShowWindow extends TerminalCommand {
	
	public ShowWindow() {
		super(CommandType.NORMAL);
		setCategory("Windows");
		numArgs = 1;
	}

	@Override public String getName() { return "show"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the visibility of a window"; }
	@Override public String getUsage() { return "ex: show 4 (where 4 is the window pid)"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.error("Not enough arguments!");
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EList<IWindowParent<?>> windows = termIn.getTopParent().getAllActiveWindows();
				
				IWindowParent<?> theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
				if (theWindow != null) {
					boolean val = theWindow.isHidden();
					theWindow.setHidden(!val);
					if (!val) theWindow.bringToFront();
					termIn.writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] made " + (!val ? "in" : "") + "visible.", EColors.green);
				}
				else termIn.error("No window with that pid currently exists!");
				
			}
			catch (Exception e) {
				try {
					String name = StringUtil.combineAll(args, " ").trim();
					
					EList<IWindowParent<?>> windows = termIn.getTopParent().getAllActiveWindows();
					IWindowParent<?> theWindow = null;
					
					for (var p : windows) {
						if (p.getObjectName().toLowerCase().equals(name)) {
							theWindow = p;
							break;
						}
					}
					
					if (theWindow != null) {
						boolean val = theWindow.isHidden();
						theWindow.setHidden(!val);
						if (!val) theWindow.bringToFront();
						termIn.writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] made " + (!val ? "in" : "") + "visible.", EColors.green);
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
