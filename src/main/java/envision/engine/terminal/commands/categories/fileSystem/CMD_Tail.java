package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReversedLinesFileReader;

import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CMD_Tail extends AbstractFileCommand {
	
	public CMD_Tail() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "tail"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the last few specified lines of a file. By default it displays 10 lines."; }
	@Override public String getUsage() { return "ex: tail 'file path' 10"; }
	
    @Override
    public Object runCommand() throws IOException {
        expectBetween(1, 2);
        
        File theFile = parseFilePath(dir(), firstArg());
        int len = 10;
        
        if (twoArgs()) len = ENumUtil.parseInt(arg(1), 10);
        
        expectFileNotNull(theFile);
        expectFileExists(theFile);
        expectFile(theFile);
        
        try (var reader = new ReversedLinesFileReader(theFile, Charset.defaultCharset())) {            
            info("Displaying content:\n");
            if (len <= 0) return null;
            
            EList<String> lines = EList.newList();
            String line = "";
            int num = 0;
            while ((line = reader.readLine()) != null && num < len) {
                lines.add(line);
                num++;
            }
            
            for (int i = lines.size() - 1; i >= 0; i--) {
                writeln(lines.get(i), EColors.lgray);
            }
        }
        
        return null;
    }

}
