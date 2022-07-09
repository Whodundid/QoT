package engine.terminal.terminalCommand.commands.system;

import engine.debug.DebugFunctions;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;

//Author: Hunter Bragg

public class DebugControl extends TerminalCommand {
	
	public DebugControl() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}

	@Override public String getName() { return "debug"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb init"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() >= 1) {
			try {
				String arg = args.get(0).toLowerCase();
				
				if (arg.equals("init")) {
					if (args.size() == 1) {
						boolean val = DebugFunctions.drawWindowInit;
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
						boolean val = DebugFunctions.drawWindowPID;
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
						boolean val = DebugFunctions.drawWindowDimensions;
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
						boolean val = DebugFunctions.drawInfo;
						EColors c = val ? EColors.mc_green : EColors.mc_red;
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
					int i = Integer.parseInt(args.get(0));
					if (i >= 0 && i < DebugFunctions.getTotal()) {
						Object[] passArgs = (args.size() > 1) ? args.subList(1, args.length()).toArray() : new Object[0];
						DebugFunctions.runDebugFunction(i, termIn, passArgs);
					}
					else {
						termIn.error("Value out of range (0-" + DebugFunctions.getTotal() + ")");
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				error(termIn, e);
			}
		}
		else if (args.size() == 0) {
			QoT.setDebugMode(!QoT.isDebugMode());
			termIn.writeln(((QoT.isDebugMode()) ? "Enabled" : "Disabled") + " debug mode.", EColors.yellow);
		}
	}
	
}
