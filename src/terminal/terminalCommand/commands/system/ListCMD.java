package terminal.terminalCommand.commands.system;

import eWindow.windowTypes.WindowParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import main.Game;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.IListableCommand;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.EUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ListCMD extends TerminalCommand {
	
	public ListCMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}

	@Override public String getName() { return "list"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("l"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to list various things. (mods, players, etc.)"; }
	@Override public String getUsage() { return "ex: list guis"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		String[] types = {"objects, guis, windows, commands"};
		super.basicTabComplete(termIn, args, new EArrayList().add(types));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { termIn.writeln("objects, guis, windows, commands", EColors.green); }
			else { termIn.info(getUsage()); }
		}
		else if (args.size() > 1) { termIn.error("Too many arguments!"); }
		else {
			switch (args.get(0)) {
			case "o":
			case "obj":
			case "objects": listObjects(termIn, args, runVisually); break;
			case "g":
			case "gui":
			case "guis": listGuis(termIn, args, runVisually); break;
			case "w":
			case "win":
			case "windows": listWindows(termIn, args, runVisually); break;
			case "h":
			case "help":
			case "cmd":
			case "cmds":
			case "commands": new Help().runCommand(termIn, new EArrayList(), false); break;
			default:
				boolean found = false;
				
				try {
					//check if the input is a listable command
					for (TerminalCommand c : Game.getTerminalHandler().getCommandList()) {
						String name = args.get(0).toLowerCase();
						
						if (name != null && c != null && c.getAliases() != null) {
							if (name.equals(c.getName()) || c.getAliases().contains(name)) {
								if (c instanceof IListableCommand) {
									found = true;
									((IListableCommand) c).list(termIn, args, runVisually);
								}
							}
						}
						
					}
					
					if (!found) { termIn.error("Unrecognized list type!"); }
				}
				catch (Exception e) { error(termIn, e); }
				
			}
		}
	}
	
	private void listObjects(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Listing all current objects in renderer\n", EColors.lgreen);
		if (runVisually) {
			int grandTotal = 0; //this isn't completely right tree wise, but whatever
			for (IWindowObject obj : Game.getGameRenderer().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
				
				//int depth = 3;
				
				EArrayList<IWindowObject> foundObjs = new EArrayList();
				EArrayList<IWindowObject> objsWithChildren = new EArrayList();
				EArrayList<IWindowObject> workList = new EArrayList();
				
				//grab all immediate children and add them to foundObjs, then check if any have children of their own
				obj.getObjects().forEach(o -> { foundObjs.add(o); if (!o.getObjects().isEmpty()) { objsWithChildren.add(o); } });
				//load the workList with every child found on each object
				objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
				
				for (IWindowObject o : EArrayList.combineLists(objsWithChildren, workList)) {
					String s = "   ";
					//for (int i = 0; i < depth; i++) { s += " "; }
					termIn.writeln(s + o.toString(), EColors.lgray);
				}
				//depth += 3;
				
				//only work as long as there are still child layers to process
				while (workList.isNotEmpty()) {
					//update the foundObjs
					foundObjs.addAll(workList);
					
					//for the current layer, find all objects that have children
					objsWithChildren.clear();
					workList.stream().filter(o -> !o.getObjects().isEmpty()).forEach(objsWithChildren::add);
					
					//put all children on the next layer into the work list
					workList.clear();
					objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
					
					for (IWindowObject o : EArrayList.combineLists(objsWithChildren, workList)) {
						String s = "   ";
						//for (int i = 0; i < depth; i++) { s += " "; }
						termIn.writeln(s + o.toString(), EColors.lgray);
					}
					//depth += 3;
				}
				
				termIn.writeln("Total objects: " + foundObjs.size(), EColors.yellow);
				
				grandTotal += foundObjs.size();
			}
			
			termIn.writeln("Grand total: " + grandTotal, EColors.orange);
		}
		else {
			for (IWindowObject obj : Game.getGameRenderer().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
			}
			termIn.writeln("Total objects: " + Game.getGameRenderer().getObjects().size(), 0xffff00);
		}
	}
	
	private void listGuis(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		
	}
	
	private void listWindows(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<WindowParent> windows = Game.getAllActiveWindows();
		
		String plural = windows.size() > 1 ? "s" : "";
		
		termIn.writeln("Listing " + windows.size() + " active window" + plural + "..\n", EColors.lgreen);
		
		termIn.writeln("(Name | PID | Type)", EColors.lime);
		termIn.writeln(EUtil.repeatStr("-", 16), EColors.lime);
		
		for (WindowParent p : windows) {
			String out = p.getObjectName() + " | " + p.getObjectID() + " | " + p.getClass().getSimpleName()
						 + (p.isPinned() ? " | " + EColors.mc_lightpurple + "pinned" : ""
						 + (p.isMinimized() ? " | " + EColors.mc_lightpurple + "minimized" : ""));
			termIn.writeln(out, EColors.lime);
		}
	}
	
}
