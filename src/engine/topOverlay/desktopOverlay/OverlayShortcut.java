package engine.windowLib.desktopOverlay;

import java.io.File;

import engine.windowLib.windowTypes.WindowObject;

public class DesktopShortcut extends WindowObject {

	 private ShortcutType type;
	 private File fileTarget;
	 private String terminalCommandTarget;
	 private String envisionProgramTarget;
	 private WindowObject<?> windowTarget;
	 
	 //--------------
	 // Constructors
	 //--------------
	 
	 public DesktopShortcut() {}
	 public DesktopShortcut(File fileIn) {
		 type = ShortcutType.FILE;
		 fileTarget = fileIn;
	 }
	 
	 public DesktopShortcut(WindowObject<?> windowIn) {
		 type = ShortcutType.PROGRAM;
		 windowTarget = windowIn;
	 }
	 
	 public DesktopShortcut(ShortcutType typeIn, String target) {
		 if (typeIn == ShortcutType.COMMAND) terminalCommandTarget = target;
		 else if (typeIn == ShortcutType.SCRIPT) envisionProgramTarget = target;
		 else throw new IllegalArgumentException("Invalid shortcut type! '" + typeIn + "'!");
		 type = typeIn;
	 }
	 
	 //---------
	 // Methods
	 //---------
	 
	 public void openShortcut() {
		 switch (type) {
		 case FILE:
		 case COMMAND:
		 case SCRIPT:
		 case PROGRAM:
		 }
	 }
	
}
