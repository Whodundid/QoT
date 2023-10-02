package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.debug.DebugFunctions;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_DebugControl extends TerminalCommand {
	
	public CMD_DebugControl() {
		setCategory("System");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "debug"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EList<String> getAliases() { return EList.of("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb init"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
	    basicTabComplete(termIn, args, "init", "pid", "dims", "drawinfo");
	}
	
	@Override
	public void runCommand() {
	    if (noArgs()) {
            Envision.setDebugMode(!Envision.isDebugMode());
            writeln(((Envision.isDebugMode()) ? "Enabled" : "Disabled") + " debug mode.", EColors.yellow);
            return;
        }
	    
	    try {
            String arg = firstArg().toLowerCase();
            
            if (arg.equals("init")) {
                if (oneArg()) {
                    boolean val = DebugFunctions.drawWindowInit = !DebugFunctions.drawWindowInit;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    writeln("Draw window initialization times in debug: " + c + val, EColors.lgray);
                }
                else if (twoArgs()) {
                    try {
                        boolean val = Boolean.parseBoolean(firstArg().toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowInit = val;
                        writeln("Set draw init: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        error("Error parsing input!");
                        error(term(), e);
                    }
                }
            }
            else if (arg.equals("pid")) {
                if (oneArg()) {
                    boolean val = DebugFunctions.drawWindowPID = !DebugFunctions.drawWindowPID;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    writeln("Draw window process id in debug: " + c + val, EColors.lgray);
                }
                else if (twoArgs()) {
                    try {
                        boolean val = Boolean.parseBoolean(firstArg().toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowPID = val;
                        writeln("Set draw PID: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        error("Error parsing input!");
                        error(term(), e);
                    }
                }
            }
            else if (arg.equals("dims") || arg.equals("pos")) {
                if (oneArg()) {
                    boolean val = DebugFunctions.drawWindowDimensions = !DebugFunctions.drawWindowDimensions;
                    EColors c = val ? EColors.mc_green : EColors.mc_red;
                    writeln("Draw window dimensions in debug: " + c + val, EColors.lgray);
                }
                else if (twoArgs()) {
                    try {
                        boolean val = Boolean.parseBoolean(firstArg().toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawWindowDimensions = val;
                        writeln("Set draw dimensions: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        error("Error parsing input!");
                        error(term(), e);
                    }
                }
            }
            else if (arg.equals("drawinfo")) {
                if (oneArg()) {
                    boolean val = DebugFunctions.drawInfo = !DebugFunctions.drawInfo;
                    EColors c = !val ? EColors.mc_green : EColors.mc_red;
                    writeln("Draw EMC debug info: " + c + val, EColors.lgray);
                }
                else if (twoArgs()) {
                    try {
                        boolean val = Boolean.parseBoolean(firstArg().toLowerCase());
                        EColors c = val ? EColors.mc_green : EColors.mc_red;
                        DebugFunctions.drawInfo = val;
                        writeln("Set draw EMC debug info: " + c + val, EColors.lgray);
                    }
                    catch (Exception e) {
                        error("Error parsing input!");
                        error(term(), e);
                    }
                }
            }
            else {
                try {
                    int i = Integer.parseInt(firstArg());
                    if (i >= 0 && i < DebugFunctions.getTotal()) {
                        Object[] passArgs = (argLength() > 1) ? args().subList(1, argLength()).toArray() : new Object[0];
                        DebugFunctions.runDebugFunction(i, term(), passArgs);
                    }
                }
                catch (Exception e) {
                    writeln("Tried to run debug command by number but failed!", EColors.orange);
                    error("Value out of range (0-" + DebugFunctions.getTotal() + ")");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            error(term(), e);
        }
	}
	
}
