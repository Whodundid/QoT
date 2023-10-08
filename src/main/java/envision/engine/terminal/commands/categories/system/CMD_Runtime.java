package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Runtime extends TerminalCommand {
	
	public CMD_Runtime() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "runtime"; }
	@Override public EList<String> getAliases() { return EList.of("rt"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Gets info on the current system run time"; }
	@Override public String getUsage() { return "ex: runtime"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
	    
	    Runtime rt = Runtime.getRuntime();
        
        double memJVMTotal = (rt.maxMemory() / 1024D / 1024D / 1024D);
        double memJVMUsed = (rt.totalMemory() / 1024D / 1024D / 1024D);
        double memJVMFree = (rt.freeMemory() / 1024D / 1024D / 1024D);
        
        String memJVMTotalString = String.format("%.2f gb", memJVMTotal);
        String memJVMUsedString = String.format("%.2f gb", memJVMUsed);
        String memJVMFreeString = String.format("%.2f gb", memJVMFree);
        
        String javaVer = "Java Version: " + EColors.mc_green + Runtime.version();
        String totMem = "Total Memory: " + EColors.mc_green + memJVMTotalString;
        String usedMem = "Used Memory: " + EColors.mc_green + memJVMUsedString;
        String freeMem = "Free Memory: " + EColors.mc_green + memJVMFreeString;
        
        //String longest = EArrayList.of(String.class, javaVer, totMem, usedMem).stream()..max(Comparator.comparingInt(String::length)).get();
        //int len = (longest != null) ? longest.length() : 0;
        
        //String divider = EUtil.repeatString("-", len - 3);
        
        //java version
        writeln(javaVer, EColors.cyan);
        //termIn.writeln(divider, EColors.lgray);
        
        //memory
        writeln(totMem, EColors.cyan);
        writeln(usedMem, EColors.cyan);
        writeln(freeMem, EColors.cyan);
        //termIn.writeln(divider, EColors.lgray);
		
        return null;
	}
	
}
