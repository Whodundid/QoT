package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.file.FileOpener;

public class CMD_Edit extends AbstractFileCommand {
	
	public CMD_Edit() {
	    setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "edit"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to edit the contents of a file."; }
	@Override public String getUsage() { return "ex: edit 'file'"; }
	
	@Override
	public Object runCommand() {
	    expectExactly(1);
	    
        String fileToEdit = firstArg();
        
        File f = new File(dir(), fileToEdit);
        
        if (fileToEdit.startsWith("..")) f = new File(dir(), fileToEdit);
        if (fileToEdit.equals("~")) f = USER_DIR;
        
        if (f.exists()) {
            check(f);
            return null;
        }
        
        f = new File(fileToEdit);
        
        if (f.exists()) check(f);
        else if (fileToEdit.startsWith("..")) f = new File(dir(), fileToEdit);
        else f = new File(fileToEdit);
        
        if (f.exists()) {
            check(f);
            return null;
        }
        
        f = new File(dir(), fileToEdit);
        check(f);
        return null;
	}
	
	private void check(File path) {
		if (path.isDirectory()) {
		    error("Error: " + path.getName() + " is a directory!");
		    return;
		}
		
		if (hasModifier("-a")) FileOpener.openFile(path);
		else openEditWindow(path);
	}
	
	private void openEditWindow(File path) {
	    if (path == null) return;
	    info("Opening edit window..");
        
        TextEditorWindow window = new TextEditorWindow(path);
        window.setFocusedObjectOnClose(term());
        
        displayWindow(window, ObjectPosition.SCREEN_CENTER);
        
        window.setFocusToLineIfEmpty();
	}

}
