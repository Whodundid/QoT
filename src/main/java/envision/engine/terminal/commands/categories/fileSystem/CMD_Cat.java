package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import eutil.colors.EColors;
import eutil.file.LineReader;

public class CMD_Cat extends AbstractFileCommand {
	
	public CMD_Cat() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "cat"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the content of a file."; }
	@Override public String getUsage() { return "ex: cat '.txt'"; }
	
	@Override
	public void runCommand() throws IOException {
	    expectAtLeast(1);
	    
	    File f = parseFilePath(dir(), firstArg());
	    
	    displayFile(f);
	}
	
	private void displayFile(File fileIn) throws IOException {
	    expectFileNotNull(fileIn);
	    expectFileExists(fileIn);
	    expectFile(fileIn);
		
		try (var reader = new LineReader(fileIn)) {
			if (reader.hasNextLine()) info("Displaying content:\n");
			reader.forEach(l -> writeln(l, EColors.lgray));
		}
	}
	
}
