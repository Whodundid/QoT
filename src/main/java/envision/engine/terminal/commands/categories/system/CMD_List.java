package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.screens.ScreenRepository;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.ListableCommand;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

//Author: Hunter Bragg

public class CMD_List extends TerminalCommand {
	
	public CMD_List() {
		setCategory("System");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "list"; }
	@Override public EList<String> getAliases() { return new EArrayList<>(); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to list various things. (mods, players, etc.)"; }
	@Override public String getUsage() { return "ex: list screens"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		String[] types = {"objects, windows, screens, commands, aliases"};
		super.basicTabComplete(termIn, args, new EArrayList().addA(types));
	}
	
	@Override
	public void runCommand() {
		if (args().isEmpty()) {
			if (runVisually()) writeln("objects, windows, screens, commands, aliases", EColors.green);
			else info(getUsage());
			return;
		}
		
		expectNoMoreThan(2);
		
		switch (firstArg()) {
        case "o":
        case "obj":
        case "objects": listObjects(); break;
        case "w":
        case "win":
        case "windows": listWindows(); break;
        case "s":
        case "screen":
        case "screens": listScreens(); break;
//        case "h":
//        case "help":
//        case "cmd":
//        case "cmds":
//        case "commands": new CMD_Help().runCommand_i(term(), new EArrayList(), false); break;
        case "alias":
        case "aliases": listAliases(); break;
        default:
            boolean found = false;
            
            try {
                String name = firstArg().toLowerCase();
                
                if (name == null) {
                    error("list type is somehow null!");
                    return;
                }

                //check if the input is a listable command
                for (TerminalCommand c : Envision.getTerminalHandler().getCommandList()) {
                    
                    if (c == null) continue;
                    if (!(c instanceof ListableCommand)) continue;
                    if (!c.matchesNameOrAlias(name)) return;
                    
                    found = true;
                    
                    // display listable command
                    displayListableCommand((ListableCommand) c);
                    
                    break;
                }
                
                if (!found) error("Unrecognized list type!");
            }
            catch (Exception e) {
                error(e);
            }
        }
	}
	
//	private ListableCommand findListableCommandFromInput(String input) {
//	    
//	}
	
	private void displayListableCommand(ListableCommand listable) {
	    if (listable.hasCustomizedListDisplay()) {
            listable.displayList(term());
        }
        else {
            EList<String> theList = listable.getList();
            for (String s : theList) {
                writeln(s);
            }
        }
	}
	
	private void listObjects() {
		writeln("Listing all current objects in this top renderer\n", EColors.lgreen);
		if (runVisually()) {
			int grandTotal = 0; //this isn't completely right tree wise, but whatever
			for (var obj : term().getTopParent().getChildren()) {
				writeln(String.format("%3d : %s", obj.getObjectID(), obj.toString()), EColors.green);
				
				//int depth = 3;
				
				EList<IWindowObject> foundObjs = EList.newList();
				EList<IWindowObject> objsWithChildren = EList.newList();
				EList<IWindowObject> workList = EList.newList();
				
				//grab all immediate children and add them to foundObjs, then check if any have children of their own
				obj.getChildren().forEach(o -> {
					foundObjs.add(o);
					if (!o.getChildren().isEmpty()) objsWithChildren.add(o);
				});
				//load the workList with every child found on each object
				objsWithChildren.forEach(c -> workList.addAll(c.getChildren()));
				
				for (var o : EList.combineLists(objsWithChildren, workList)) {
				    String id = (o instanceof IWindowParent wp) ? "[" + wp.getWindowID() + ":" + wp.getObjectID() + "]"
				                : "" + o.getObjectID();
					String s = String.format("   %10s : %s", id, o.toString());
					//for (int i = 0; i < depth; i++) { s += " "; }
					writeln(s, EColors.lgray);
				}
				//depth += 3;
				
				//only work as long as there are still child layers to process
				while (workList.isNotEmpty()) {
					//update the foundObjs
					foundObjs.addAll(workList);
					
					//for the current layer, find all objects that have children
					objsWithChildren.clear();
					workList.stream().filter(o -> !o.getChildren().isEmpty()).forEach(objsWithChildren::add);
					
					//put all children on the next layer into the work list
					workList.clear();
					objsWithChildren.forEach(c -> workList.addAll(c.getChildren()));
					
					for (var o : EList.combineLists(objsWithChildren, workList)) {
						String s = String.format("   %3d : %s", o.getObjectID(), o.toString());
						//for (int i = 0; i < depth; i++) { s += " "; }
						writeln(s, EColors.lgray);
					}
					//depth += 3;
				}
				
				writeln("Total objects: " + foundObjs.size(), EColors.yellow);
				
				grandTotal += foundObjs.size();
			}
			
			writeln("Grand total: " + grandTotal, EColors.orange);
		}
		else {
			for (var obj : term().getTopParent().getChildren()) {
				writeln(obj.toString(), EColors.green);
			}
			writeln("Total objects: " + term().getTopParent().getChildren().size(), 0xffffff00);
		}
	}
	
	private void listWindows() {
		EList<IWindowParent> windows = term().getTopParent().getAllActiveWindows();
		
		String plural = windows.size() > 1 ? "s" : "";
		
		writeln("Listing " + windows.size() + " active window" + plural + "..\n", EColors.lgreen);
		
		String title = "(Name | PID | Type)";
		writeln(title, EColors.lime);
		writeln(EStringUtil.repeatString("-", title.length()), EColors.lime);
		
		for (var p : windows) {
			String out = p.getObjectName() + " | " + p.getWindowID() + " | " + p.getClass().getSimpleName()
						 + (p.isPinned() ? " | " + EColors.mc_lightpurple + "pinned" : ""
						 + (p.isMinimized() ? " | " + EColors.mc_lightpurple + "minimized" : ""));
			writeln(out, EColors.lime);
		}
	}
	
	private void listScreens() {
		/**
		String dir = (args.size() >= 2) ? args.get(1) : "engine.screens";
		
		EArrayList<Class<GameScreen>> screenClasses = ClassFinder.findClassesOfType(dir, GameScreen.class);
		EArrayList<String> classNames = new EArrayList();
		
		for (Class<GameScreen> c : screenClasses) {
			EModifier m = EModifier.of(c.getModifiers());
			if (m.isPublic() && !m.isAbstract()) {
				try {
					if (c.getConstructor() != null) classNames.add(c.getSimpleName());
				}
				catch (NoSuchMethodException e) {}
				catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
		
		writeln("Listing " + classNames.size() + " available screens..\n", EColors.lgreen);
		for (String s : classNames) {
			writeln(s, EColors.lime);
		}
		*/
		
		writeln("Listing " + ScreenRepository.getRegisteredScreens().size() + " available screens..\n", EColors.lgreen);
		for (GameScreen s : ScreenRepository.getRegisteredScreens()) {
			if (s.getAliases() == null || s.getAliases().isEmpty()) {
				writeln("  " + s.getClass().getSimpleName(), 0xffb2b2b2);
			}
			else {
				var sb = EStringBuilder.of(EColors.mc_green);
				for (int i = 0; i < s.getAliases().size(); i++) {
					String commandAlias = s.getAliases().get(i);
					if (i == s.getAliases().size() - 1) sb.a(commandAlias);
					else sb.a(commandAlias, ", ");
				}
				
				writeln(EColors.lgray, "  ", s.getClass().getSimpleName(), ": ", sb);
			}
		}
	}
	
	private void listAliases() {
	    var aliases = TerminalCommandHandler.getInstance().getCommandAliases();
	    
	    writeln(EColors.lgreen, "Listing aliases..\n");
	    
	    for (var entry : aliases.entrySet()) {
	        String aliasName = entry.getKey();
	        String aliasValue = entry.getValue();
	        
	        writeln("  ", EColors.mc_lightpurple, aliasName, EColors.white, "=", EColors.yellow, "'", aliasValue, "'");
	    }
	}
	
}
