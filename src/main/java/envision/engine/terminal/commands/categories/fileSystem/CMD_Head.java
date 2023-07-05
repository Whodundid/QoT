package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import eutil.colors.EColors;
import eutil.file.LineReader;
import eutil.math.ENumUtil;

public class CMD_Head extends AbstractFileCommand {
	
	public CMD_Head() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "head"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the first few specified lines of a file. By default it displays 10 lines."; }
	@Override public String getUsage() { return "ex: head 'file path' 10"; }
	
	@Override
	public void runCommand() throws IOException {
	    expectBetween(1, 2);
	    
	    File theFile = parseFilePath(dir(), firstArg());
	    int amount = 10;
	    
	    if (twoArgs()) amount = ENumUtil.parseInt(arg(1), 10);
	    
	    display(theFile, amount);
	}
	
	private void display(File fileIn, int len) throws IOException {
	    expectFileNotNull(fileIn);
	    expectFileExists(fileIn);
	    expectFile(fileIn);
	    
	    try (var reader = new LineReader(fileIn)) {
	        info("Displaying content:\n");
            if (len <= 0) return;
            
            int i = 0;
            while (reader.hasNextLine() && i < len) {
                String s = reader.nextLine();
                writeln(s, EColors.lgray);
                i++;
            }
        }
	}

}
