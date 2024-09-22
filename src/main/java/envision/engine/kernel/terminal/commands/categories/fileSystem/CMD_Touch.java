package envision.engine.kernel.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.OffsetDateTime;

import eutil.file.EFileUtil;

public class CMD_Touch extends AbstractFileCommand {
    
    public CMD_Touch() {
        expectedArgLength = -1;
    }
    
    @Override public String getName() { return "touch"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Creates a new file(s) with the given name(s)."; }
    @Override public String getUsage() { return "ex: touch file"; }
    
    @Override
    public void runCommand() {
        expectAtLeast(1, "touch: missing file operand");
        
        for (String name : args()) {
            File f = null;
            if (name.contains(SEPARATOR)) f = new File(name);
            else { f = new File(dir(), name); }
            
            // check if we are just updating an already existing file or not
            if (EFileUtil.fileExists(f)) {
                Instant instant = OffsetDateTime.now().toInstant();
                try {
                    Files.setLastModifiedTime(f.toPath(), FileTime.from(instant));
                }
                catch (IOException e) {
                    e.printStackTrace();
                    javaError(e);
                }
                continue;
            }
            
            // if the file path didn't exist, create it
            try {
                Files.createFile(f.toPath());
            }
            catch (IOException e) {
                e.printStackTrace();
                javaError(e);
            }
        }
    }
    
}

