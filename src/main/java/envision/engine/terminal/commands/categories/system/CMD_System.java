package envision.engine.terminal.commands.categories.system;

import java.io.File;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.terminalUtil.ESystemInfo;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_System extends TerminalCommand {
	
	public CMD_System() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "system"; }
	@Override public EList<String> getAliases() { return EList.of("sys"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on the system"; }
	@Override public String getUsage() { return "ex: sys"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
	    
        //os
        writeln("OS:", EColors.orange);
        
        try {
            writeln("  Brand: " + EColors.mc_green + ESystemInfo.getOS_Brand(), EColors.cyan);
            //writeln("  Manufacturer: " + EColors.mc_green + ESystemInfo.getOS_Manufacturer(), EColors.cyan);
            writeln("  Version: " + EColors.mc_green + ESystemInfo.getOS_Version(), EColors.cyan);
            writeln("  Architecture: " + EColors.mc_green + ESystemInfo.getOS_Architecture() + "-bit", EColors.cyan);
        }
        catch (Exception e) {
            error("Error fetching operating system values..");
            error(e);
        }
        
        //cpu
        writeln("\nCPU:", EColors.orange);
        
        try {
            writeln("  CPU Name: " + EColors.mc_green + ESystemInfo.getCPU_Name(), EColors.cyan);
            writeln("  Brand: " + EColors.mc_green + ESystemInfo.getCPU_Brand(), EColors.cyan);
        }
        catch (Exception e) {
            error("Error fetching CPU values..");
            error(e);
        }
        
        //gpu
        writeln("\nGPU:", EColors.orange);
        
        try {
            //writeln("  Model: " + EColors.mc_green + ESystemInfo.getGPU_Model(), EColors.cyan);
            //writeln("  Vendor: " + EColors.mc_green + ESystemInfo.getGPU_Vendor(), EColors.cyan);
            //writeln("  Version: " + EColors.mc_green + ESystemInfo.getGPU_Version(), EColors.cyan);
        }
        catch (Exception e) {
            error("Error fetching GPU values..");
            error(e);
        }
        
        //memory
        writeln("\nRAM: (gb)", EColors.orange);
        
        try {
            double memTotal = (ESystemInfo.getRAM_Total() / 1024D / 1024D / 1024D);
            double memFree =  (ESystemInfo.getRAM_JVM_Free() / 1024D / 1024D / 1024D);
            double memUsed = memTotal - memFree;
            
            String memTotalString = String.format("%.2f", memTotal);
            String memFreeString = String.format("%.2f", memFree);
            String memUsedString = String.format("%.2f", memUsed);
            
            writeln("  Total: " + EColors.mc_green + memTotalString, EColors.cyan);
            writeln("  Available: " + EColors.mc_green + memFreeString, EColors.cyan);
            writeln("  Used: " + EColors.mc_green + memUsedString, EColors.cyan);
        }
        catch (Exception e) {
            error("Error fetching system RAM values..");
            error(e);
        }
        
        //jvm memory
        writeln("\nJVM Memory: (gb)", EColors.orange);
        
        try {
            double memJVMTotal = (ESystemInfo.getRAM_JVM_Total() / 1024D / 1024D / 1024D);
            double memJVMUsed = (ESystemInfo.getRAM_JVM_Used() / 1024D / 1024D / 1024D);
            double memJVMFree = (ESystemInfo.getRAM_JVM_Free() / 1024D / 1024D / 1024D);
            
            String memJVMTotalString = String.format("%.2f", memJVMTotal);
            String memJVMUsedString = String.format("%.2f", memJVMUsed);
            String memJVMFreeString = String.format("%.2f", memJVMFree);
            
            writeln("  Total: " + EColors.mc_green + memJVMTotalString, EColors.cyan);
            writeln("  Used: " + EColors.mc_green + memJVMUsedString, EColors.cyan);
            writeln("  Available: " + EColors.mc_green + memJVMFreeString, EColors.cyan);
        }
        catch (Exception e) {
            error("Error fetching JVM Memory values..");
            error(e);
        }
        
        //drives
        writeln("\nDrives: (gb)", EColors.orange);
        
        try {
            File[] drives = File.listRoots();
            for (File d : drives) {
                String driveName = d.getAbsolutePath();
                boolean primary = false;
                
                try {
                    primary = driveName.substring(0, driveName.length() - 1).equals(System.getenv("SystemDrive"));
                }
                catch (Exception e) { error(e); }
                
                writeln("  " + d.getAbsolutePath() + (primary ? EColors.mc_lightpurple + " (primary)" : ""), EColors.blue);
                
                double total = (d.getTotalSpace() / 1024D / 1024D / 1024D);
                double free = (d.getFreeSpace() / 1024D / 1024D/ 1024D);
                
                String totalString = String.format("%.2f", total);
                String freeString = String.format("%.2f", free);
                
                writeln("    Total: " + EColors.mc_green + totalString, EColors.cyan);
                writeln("    Free: " + EColors.mc_green + freeString, EColors.cyan);
            }
        }
        catch (Exception q) {
            error("Error fetching system drive values..");
            error(q);
        }
	}
	
}
