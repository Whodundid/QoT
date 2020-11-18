package terminal.terminalCommand.commands.windows;

import eWindow.windowTypes.WindowParent;
import main.Game;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.EUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class ShowWindow extends TerminalCommand {
	
	public ShowWindow() {
		super(CommandType.NORMAL);
		setCategory("Windows");
		numArgs = 1;
	}

	@Override public String getName() { return "show"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the visibility of a window"; }
	@Override public String getUsage() { return "ex: show 4 (where 4 is the window pid)"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EArrayList<WindowParent> windows = Game.getAllActiveWindows();
				
				WindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
				if (theWindow != null) {
					boolean val = theWindow.isHidden();
					theWindow.setHidden(!val);
					if (!val) { theWindow.bringToFront(); }
					termIn.writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] made " + (!val ? "in" : "") + "visible.", EColors.green);
				}
				else { termIn.error("No window with that pid currently exists!"); }
				
			}
			catch (Exception e) {
				try {
					String name = EUtil.combineAll(args, " ").trim();
					
					EArrayList<WindowParent> windows = Game.getAllActiveWindows();
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
						if (!val) { theWindow.bringToFront(); }
						termIn.writeln("Window: [" + theWindow.getObjectName() + " | " + theWindow.getObjectID() + "] made " + (!val ? "in" : "") + "visible.", EColors.green);
					}
					else { termIn.error("No window with that name currently exists!"); }
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
