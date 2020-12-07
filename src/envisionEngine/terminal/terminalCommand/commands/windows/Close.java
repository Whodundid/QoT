package envisionEngine.terminal.terminalCommand.commands.windows;

import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import main.Game;
import util.EUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class Close extends TerminalCommand {
	
	public Close() {
		super(CommandType.NORMAL);
		setCategory("Windows");
		numArgs = 1;
	}

	@Override public String getName() { return "close"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to close a specific window"; }
	@Override public String getUsage() { return "ex: close 23 (where 23 is the window pid)"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.close(); }
		else if (args.size() >= 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EArrayList<WindowParent> windows = Game.getAllActiveWindows();
				
				WindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
				if (EUtil.nullDo(theWindow, w -> w.close())) {
					termIn.writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getObjectID() + "' closed", EColors.green);
				}
				else { termIn.error("No window with that pid currently exists!"); }
				
			}
			catch (Exception e) {
				try {
					String name = EUtil.combineAll(args, " ").toLowerCase().trim();
					
					EArrayList<WindowParent> windows = Game.getAllActiveWindows();
					
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
						Game.displayWindow(null);
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
						else { termIn.error("No window with that name currently exists!"); }
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