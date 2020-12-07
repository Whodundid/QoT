package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import java.io.File;
import util.miscUtil.ESystemInfo;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class SystemCMD extends TerminalCommand {
	
	public SystemCMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "system"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("sys"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on the system"; }
	@Override public String getUsage() { return "ex: sys"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command does not take any arguments"); }
		else {
			
			//os
			termIn.writeln("OS:", EColors.orange);
			
			try {
				termIn.writeln("  Brand: " + EColors.mc_green + ESystemInfo.getOS_Brand(), EColors.cyan);
				//termIn.writeln("  Manufacturer: " + EColors.mc_green + ESystemInfo.getOS_Manufacturer(), EColors.cyan);
				termIn.writeln("  Version: " + EColors.mc_green + ESystemInfo.getOS_Version(), EColors.cyan);
				termIn.writeln("  Architecture: " + EColors.mc_green + ESystemInfo.getOS_Architecture() + "-bit", EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching operating system values..");
				error(termIn, e);
			}
			
			//cpu
			termIn.writeln("\nCPU:", EColors.orange);
			
			try {
				//termIn.writeln("  CPU Name: " + EColors.mc_green + ESystemInfo.getCPU_Name(), EColors.cyan);
				termIn.writeln("  Brand: " + EColors.mc_green + ESystemInfo.getCPU_Brand(), EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching CPU values..");
				error(termIn, e);
			}
			
			//gpu
			termIn.writeln("\nGPU:", EColors.orange);
			
			try {
				termIn.writeln("  Model: " + EColors.mc_green + ESystemInfo.getGPU_Model(), EColors.cyan);
				termIn.writeln("  Vendor: " + EColors.mc_green + ESystemInfo.getGPU_Vendor(), EColors.cyan);
				termIn.writeln("  Version: " + EColors.mc_green + ESystemInfo.getGPU_Version(), EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching GPU values..");
				error(termIn, e);
			}
			
			//memory
			termIn.writeln("\nRAM: (gb)", EColors.orange);
			
			try {
				double memTotal = ((double) ESystemInfo.getRAM_Total() / 1024d / 1024d / 1024d);
				double memFree =  ((double) ESystemInfo.getRAM_JVM_Free() / 1024d / 1024d / 1024d);
				double memUsed = memTotal - memFree;
				
				String memTotalString = String.format("%.2f", memTotal);
				String memFreeString = String.format("%.2f", memFree);
				String memUsedString = String.format("%.2f", memUsed);
				
				termIn.writeln("  Total: " + EColors.mc_green + memTotalString, EColors.cyan);
				termIn.writeln("  Available: " + EColors.mc_green + memFreeString, EColors.cyan);
				termIn.writeln("  Used: " + EColors.mc_green + memUsedString, EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching system RAM values..");
				error(termIn, e);
			}
			
			//jvm memory
			termIn.writeln("\nJVM Memory: (gb)", EColors.orange);
			
			try {
				double memJVMTotal = (double) ((double) ESystemInfo.getRAM_JVM_Total() / 1024d / 1024d / 1024d);
				double memJVMUsed = (double) ((double) ESystemInfo.getRAM_JVM_Used() / 1024d / 1024d / 1024d);
				double memJVMFree = (double) ((double) ESystemInfo.getRAM_JVM_Free() / 1024d / 1024d / 1024d);
				
				String memJVMTotalString = String.format("%.2f", memJVMTotal);
				String memJVMUsedString = String.format("%.2f", memJVMUsed);
				String memJVMFreeString = String.format("%.2f", memJVMFree);
				
				termIn.writeln("  Total: " + EColors.mc_green + memJVMTotalString, EColors.cyan);
				termIn.writeln("  Used: " + EColors.mc_green + memJVMUsedString, EColors.cyan);
				termIn.writeln("  Available: " + EColors.mc_green + memJVMFreeString, EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching JVM Memory values..");
				error(termIn, e);
			}
			
			//drives
			termIn.writeln("\nDrives: (gb)", EColors.orange);
			
			try {
				File[] drives = File.listRoots();
				for (File d : drives) {
					String driveName = d.getAbsolutePath();
					boolean primary = false;
					
					try {
						primary = driveName.substring(0, driveName.length() - 1).equals(System.getenv("SystemDrive"));
					}
					catch (Exception e) { error(termIn, e); }
					
					termIn.writeln("  " + d.getAbsolutePath() + (primary ? EColors.mc_lightpurple + " (primary)" : ""), EColors.blue);
					
					double total = (double) ((double) d.getTotalSpace() / 1024d / 1024d / 1024d);
					double free = (double) ((double) d.getFreeSpace() / 1024d / 1024d / 1024d);
					
					String totalString = String.format("%.2f", total);
					String freeString = String.format("%.2f", free);
					
					termIn.writeln("    Total: " + EColors.mc_green + totalString, EColors.cyan);
					termIn.writeln("    Free: " + EColors.mc_green + freeString, EColors.cyan);
				}
			}
			catch (Exception q) {
				termIn.error("Error fetching system drive values..");
				error(termIn, q);
			}
		} //else
	}
	
}
