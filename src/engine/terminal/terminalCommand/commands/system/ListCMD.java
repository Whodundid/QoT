package engine.terminal.terminalCommand.commands.system;

import engine.screens.screenUtil.GameScreen;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.IListableCommand;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.util.ClassFinder;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.reflection.EModifier;
import eutil.strings.StringUtil;
import main.QoT;

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
	@Override public String getUsage() { return "ex: list screens"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		String[] types = {"objects, windows, screens, commands"};
		super.basicTabComplete(termIn, args, new EArrayList().addA(types));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) termIn.writeln("objects, windows, screens, commands", EColors.green);
			else termIn.info(getUsage());
		}
		else if (args.size() > 2) termIn.error("Too many arguments!");
		else {
			switch (args.get(0)) {
			case "o":
			case "obj":
			case "objects": listObjects(termIn, args, runVisually); break;
			case "w":
			case "win":
			case "windows": listWindows(termIn, args, runVisually); break;
			case "s":
			case "screen":
			case "screens": listScreens(termIn, args, runVisually); break;
			case "h":
			case "help":
			case "cmd":
			case "cmds":
			case "commands": new Help().runCommand(termIn, new EArrayList(), false); break;
			default:
				boolean found = false;
				
				try {
					//check if the input is a listable command
					for (TerminalCommand c : QoT.getTerminalHandler().getCommandList()) {
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
	
	private void listObjects(ETerminal<?> termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Listing all current objects in this top renderer\n", EColors.lgreen);
		if (runVisually) {
			int grandTotal = 0; //this isn't completely right tree wise, but whatever
			for (IWindowObject<?> obj : termIn.getTopParent().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
				
				//int depth = 3;
				
				EArrayList<IWindowObject<?>> foundObjs = new EArrayList();
				EArrayList<IWindowObject<?>> objsWithChildren = new EArrayList();
				EArrayList<IWindowObject<?>> workList = new EArrayList();
				
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
			for (IWindowObject obj : termIn.getTopParent().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
			}
			termIn.writeln("Total objects: " + termIn.getTopParent().getObjects().size(), 0xffffff00);
		}
	}
	
	private void listWindows(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<WindowParent> windows = termIn.getTopParent().getAllActiveWindows();
		
		String plural = windows.size() > 1 ? "s" : "";
		
		termIn.writeln("Listing " + windows.size() + " active window" + plural + "..\n", EColors.lgreen);
		
		String title = "(Name | PID | Type)";
		termIn.writeln(title, EColors.lime);
		termIn.writeln(StringUtil.repeatString("-", title.length()), EColors.lime);
		
		for (WindowParent p : windows) {
			String out = p.getObjectName() + " | " + p.getObjectID() + " | " + p.getClass().getSimpleName()
						 + (p.isPinned() ? " | " + EColors.mc_lightpurple + "pinned" : ""
						 + (p.isMinimized() ? " | " + EColors.mc_lightpurple + "minimized" : ""));
			termIn.writeln(out, EColors.lime);
		}
	}
	
	private void listScreens(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		String dir = (args.size() >= 2) ? args.get(1) : "assets_screens";
		
		EArrayList<Class<GameScreen>> screenClasses = ClassFinder.findClassesOfType(dir, GameScreen.class);
		EArrayList<String> classNames = new EArrayList();
		
		for (Class<GameScreen> c : screenClasses) {
			EModifier m = EModifier.of(c.getModifiers());
			if (m.isPublic() && !m.isAbstract()) {
				try { if (c.getConstructor() != null) { classNames.add(c.getSimpleName()); } }
				catch (NoSuchMethodException e) {}
				catch (Exception e) { e.printStackTrace(); break; }
			}
		}
		
		termIn.writeln("Listing " + classNames.size() + " available screens..\n", EColors.lgreen);
		for (String s : classNames) {
			termIn.writeln(s, EColors.lime);
		}
	}
	
}