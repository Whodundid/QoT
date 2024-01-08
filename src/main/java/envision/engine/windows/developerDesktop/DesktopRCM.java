package envision.engine.windows.developerDesktop;

import envision.Envision;
import envision.engine.assets.WindowTextures;
import envision.engine.inputHandlers.Mouse;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.developerDesktop.shortcuts.CommandShortcutEditorWindow;
import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut_Command;
import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut_File;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowUtil.ObjectPosition;

public class DesktopRCM extends RightClickMenu {
    
    public DesktopRCM() {
        setTitle("Desktop");
        addOption("File Explorer", WindowTextures.file_folder, this::handle_openFileExplorer);
        addOption("New File", WindowTextures.file_txt, this::handle_newFile);
        addOption("New Folder", WindowTextures.new_folder, this::handle_newFolder);
        addOption("New Command", WindowTextures.forward, this::handle_newCommand);
        addOption("Open Terminal", WindowTextures.terminal, this::handle_terminal);
        addOption("Open Desktop Directory", WindowTextures.file_folder, this::openDesktopDir);
    }
    
    public static void checkOpen(int action, int button) {
        var top = Envision.getDeveloperDesktop();
        if (action == 1 && button == 1) {
            var highest = top.getHighestZObjectUnderMouse();
            if (highest == null || !highest.isVisible()) {
                var old = top.getWindowInstance(DesktopRCM.class);
                if (old != null) old.close();
                top.displayWindow(new DesktopRCM(), ObjectPosition.CURSOR_CORNER);
            }
        }
    }
    
    public void handle_openFileExplorer() {
        Envision.getDeveloperDesktop().displayWindow(new FileExplorerWindow(), ObjectPosition.CURSOR_CENTER);
    }
    
    public void handle_newFile() {
        var shortcut = new DesktopShortcut_File();
        shortcut.setPosition(Mouse.getMx(), Mouse.getMy());
        shortcut.setIcon(WindowTextures.file_txt);
        shortcut.setFile(DeveloperDesktop.createFileOnDesktop("New File.txt"));
        DeveloperDesktop.addShortcut(shortcut);
    }
    
    public void handle_newFolder() {
        var shortcut = new DesktopShortcut_File();
        shortcut.setPosition(Mouse.getMx(), Mouse.getMy());
        shortcut.setIcon(WindowTextures.file_folder);
        shortcut.setFile(DeveloperDesktop.createDirectoryOnDesktop("New Folder"));
        DeveloperDesktop.addShortcut(shortcut);
    }
    
    public void handle_newCommand() {
        var shortcut = new DesktopShortcut_Command("New Command");
        shortcut.setPosition(Mouse.getMx(), Mouse.getMy());
        DeveloperDesktop.openWindow(new CommandShortcutEditorWindow(shortcut, true));
    }
    
    public void handle_terminal() {
        Envision.getDeveloperDesktop().displayWindow(new ETerminalWindow(), ObjectPosition.CURSOR_CENTER);
    }
    
    public void openDesktopDir() {
        DesktopUtil.openFile(DeveloperDesktop.DESKTOP_DIR);
    }
    
}
