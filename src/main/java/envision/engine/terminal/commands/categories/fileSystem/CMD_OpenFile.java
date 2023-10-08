package envision.engine.terminal.commands.categories.fileSystem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.bundledWindows.TextureDisplayer;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.file.FileOpener;

public class CMD_OpenFile extends AbstractFileCommand {
	
	public CMD_OpenFile() {
		expectedArgLength = 1;
		setAcceptedModifiers("-a");
	}
	
	@Override public String getName() { return "open"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to open or run a file or application. Add -a to end to always open on computer."; }
	@Override public String getUsage() { return "ex: open 'file'"; }
	
	@Override
	public Object runCommand() throws Exception {
		expectExactly(1);
		
		boolean openA = hasModifier("-a");
		
		String toOpen = firstArg();
		File theFileToOpen = parseFilePath(dir(), toOpen);
		
		determineFileType(theFileToOpen, openA);
		return null;
	}
	
	private void determineFileType(File path, boolean openA) throws IOException {
		if (!path.isDirectory()) {
			open(path, openA);
			return;
		}
		
		String link = path.getCanonicalPath();
		String colorPath = "" + EColors.mc_aqua + link;
		
		if (openA) {
			writeLink("Opening Dir: " + colorPath, link, new File(link), false, EColors.yellow);
			FileOpener.openFile(path);
		}
		else {
			setDir(new File(path.getCanonicalPath()));
			writeLink("Current Dir: " + colorPath, link, new File(link), false, EColors.yellow);
		}
	}
	
	private void open(File dir, boolean openA) throws IOException {
		if (!dir.exists()) {
			error("No file/directory with that name found in the current directory!");
			return;
		}
		
		if (openA) {
			writeln("Opening the file on computer...", EColors.lgreen);
			FileOpener.openFile(dir);
			return;
		}
		
		String path = dir.getCanonicalPath().trim();
		String extenstion = FilenameUtils.getExtension(path);
		
		if (EUtil.anyMatch(extenstion, "png", "jpg", "gif", "tga", "bmp")) {
			try {
				BufferedImage img = ImageIO.read(dir);
				if (img != null) {
					writeln("Opening...", EColors.green);
					term().getTopParent().displayWindow(new TextureDisplayer(dir), ObjectPosition.SCREEN_CENTER);
				}
			}
			catch (IIOException e) {
				File[] files = dir.listFiles();
				
				writeln("File Directory: " + EColors.mc_aqua + dir + "\n", EColors.yellow);
				
				for (File f : files) {
					boolean d = f.isDirectory();
					writeln(f + (d ? "\\" : ""), d ? 0xff2265f0 : EColors.green.intVal);
				}
				
				error(e);
			}
		}
		else if (EUtil.anyMatch(extenstion, "txt", "cfg")) {
			info("Opening edit window..");
			
			TextEditorWindow window = new TextEditorWindow(dir);
			
			window.setFocusedObjectOnClose(term());
			term().getTopParent().displayWindow(window, ObjectPosition.SCREEN_CENTER);
			window.setFocusToLineIfEmpty();
		}
		else if (dir.canExecute()) {
			FileOpener.openFile(dir);
		}
	}

}
