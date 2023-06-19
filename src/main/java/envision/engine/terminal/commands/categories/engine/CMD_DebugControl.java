package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.debug.DebugFunctions;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_DebugControl extends TerminalCommand {
	
	public CMD_DebugControl() {
		setCategory("System");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "debug"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EList<String> getAliases() { return new EArrayList<>("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb init"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
	    basicTabComplete(termIn, args, "init", "pid", "dims", "drawinfo");
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
	    if (args.size() == 0) {
            Envision.setDebugMode(!Envision.isDebugMode());
            termIn.writeln(((Envision.isDebugMode()) ? "Enabled" : "Disabled") + " debug mode.", EColors.yellow);
            return;
        }
	    
	    try {
            String arg = args.get(0).toLowerCase();
            
            if (arg.equals("init")) {
                if (args.size() == 1) {
                    boolean val = DebugFunctions.drawWindowInit = !DebugFunctions.drawWindowInit;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    termIn.writeln("Draw window initialization times in debug: " + c + val, EColors.lgray);
                }
                else if (args.size() == 2) {
                    try {
                        boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowInit = val;
                        termIn.writeln("Set draw init: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        termIn.error("Error parsing input!");
                        error(termIn, e);
                    }
                }
            }
            else if (arg.equals("pid")) {
                if (args.size() == 1) {
                    boolean val = DebugFunctions.drawWindowPID = !DebugFunctions.drawWindowPID;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    termIn.writeln("Draw window process id in debug: " + c + val, EColors.lgray);
                }
                else if (args.size() == 2) {
                    try {
                        boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowPID = val;
                        termIn.writeln("Set draw PID: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        termIn.error("Error parsing input!");
                        error(termIn, e);
                    }
                }
            }
            else if (arg.equals("dims") || arg.equals("pos")) {
                if (args.size() == 1) {
                    boolean val = DebugFunctions.drawWindowDimensions = !DebugFunctions.drawWindowDimensions;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    termIn.writeln("Draw window dimensions in debug: " + c + val, EColors.lgray);
                }
                else if (args.size() == 2) {
                    try {
                        boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowDimensions = val;
                        termIn.writeln("Set draw dimensions: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        termIn.error("Error parsing input!");
                        error(termIn, e);
                    }
                }
            }
            else if (arg.equals("drawinfo")) {
                if (args.size() == 1) {
                    boolean val = DebugFunctions.drawInfo = !DebugFunctions.drawInfo;
                    EColors c = !val ? EColors.mc_green : EColors.mc_red;
                    termIn.writeln("Draw EMC debug info: " + c + val, EColors.lgray);
                }
                else if (args.size() == 2) {
                    try {
                        boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawInfo = val;
                        termIn.writeln("Set draw EMC debug info: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        termIn.error("Error parsing input!");
                        error(termIn, e);
                    }
                }
            }
            else {
                try {
                    int i = Integer.parseInt(args.get(0));
                    if (i >= 0 && i < DebugFunctions.getTotal()) {
                        Object[] passArgs = (args.size() > 1) ? args.subList(1, args.length()).toArray() : new Object[0];
                        DebugFunctions.runDebugFunction(i, termIn, passArgs);
                    }
                }
                catch (Exception e) {
                    termIn.writeln("Tried to run debug command by number but failed!", EColors.orange);
                    termIn.error("Value out of range (0-" + DebugFunctions.getTotal() + ")");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            error(termIn, e);
        }
	}
	
}