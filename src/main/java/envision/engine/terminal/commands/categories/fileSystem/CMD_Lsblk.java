package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import eutil.strings.EStringUtil;

public class CMD_Lsblk extends AbstractFileCommand {
	
	public CMD_Lsblk() {
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "lsblk"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current drives on the system."; }
	@Override public String getUsage() { return "ex: lsblk"; }
	
	@Override
	public void runCommand() throws IOException {
	    expectNoArgs();
	    
	    File[] drives = File.listRoots();
	    if (drives == null || drives.length == 0) {
	        error("There appear to be no drives available on the system!");
	        return;
	    }
	    
	    String title = String.format("%-10s%-22s%-30s", "Drive", "Total size (gb)", "Free Space (gb)");
        writeln(title, 0xff2265f0);
        writeln(EStringUtil.repeatString("-", title.length() - 22), 0xff2265f0);
        
        for (File f : drives) {
            double total = ((double) f.getTotalSpace() / 1024d / 1024d / 1024d);
            double free = ((double) f.getFreeSpace() / 1024d / 1024d / 1024d);
            
            String out = String.format("%-11s%-23.2f%-30.2f", f.getCanonicalPath(), total, free);
            writeln(out, 0xff2265f0);
        }
	}
	
}
