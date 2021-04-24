package debug.terminal.terminalCommand.commands.system;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import renderUtil.EColors;
import storageUtil.EArrayList;

public class RuntimeCMD extends TerminalCommand {
	
	public RuntimeCMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "runtime"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("rt"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Gets info on the current system run time"; }
	@Override public String getUsage() { return "ex: runtime"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command does not take any arguments"); }
		else {
			Runtime rt = Runtime.getRuntime();
			
			double memJVMTotal = (double) ((double) rt.maxMemory() / 1024d / 1024d / 1024d);
			double memJVMUsed = (double) ((double) rt.totalMemory() / 1024d / 1024d / 1024d);
			double memJVMFree = (double) ((double) rt.freeMemory() / 1024d / 1024d / 1024d);
			
			String memJVMTotalString = String.format("%.2f gb", memJVMTotal);
			String memJVMUsedString = String.format("%.2f gb", memJVMUsed);
			String memJVMFreeString = String.format("%.2f gb", memJVMFree);
			
			String javaVer = "Java Version: " + EColors.mc_green + Runtime.getRuntime().version();
			String totMem = "Total Memory: " + EColors.mc_green + memJVMTotalString;
			String usedMem = "Used Memory: " + EColors.mc_green + memJVMUsedString;
			String freeMem = "Free Memory: " + EColors.mc_green + memJVMFreeString;
			
			//String longest = EArrayList.of(String.class, javaVer, totMem, usedMem).stream()..max(Comparator.comparingInt(String::length)).get();
			//int len = (longest != null) ? longest.length() : 0;
			
			//String divider = EUtil.repeatString("-", len - 3);
			
			//java version
			termIn.writeln(javaVer, EColors.cyan);
			//termIn.writeln(divider, EColors.lgray);
			
			//memory
			termIn.writeln(totMem, EColors.cyan);
			termIn.writeln(usedMem, EColors.cyan);
			termIn.writeln(freeMem, EColors.cyan);
			//termIn.writeln(divider, EColors.lgray);
		}
		
	}
	
}
