package envision.engine.terminal.commands.categories.engine;

import java.io.File;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.file.FileOpener;
import qot.settings.QoTSettings;

public class CMD_Config extends TerminalCommand {
	
	public CMD_Config() {
		setCategory("Engine");
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "config"; }
	@Override public EList<String> getAliases() { return EList.of("conf"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides a means for interfacing with the game's config file"; }
	@Override public String getUsage() { return "ex: conf    -OR-    conf 'edit|reload|save|reset'"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termin, EList<String> args) {
		if (args.size() <= 1) basicTabComplete(termin, args, "edit", "reload", "save", "reset");
	}

	@Override
	public void runCommand() {
	    expectNoMoreThan(1);
	    
		if (noArgs()) {
			displayConfig();
			if (hasModifier("-a")) FileOpener.openFile(QoTSettings.getGameConfig().getFile());
			return;
		}
		
		if (oneArg()) {
            switch (firstArg()) {
            case "edit": editConfig(); break;
            case "rel":
            case "reload":
            case "l":
            case "load": reloadConfig(); break;
            case "s":
            case "save": saveConfig(); break;
            case "res":
            case "reset": resetConfig(); break;
            default:
                errorUsage("Invalid action argument!");
            }
        }
	}
	
	private void displayConfig() {
		writeln(EColors.aquamarine, Envision.getGameName(), " config:");
		
		var config = QoTSettings.getGameConfig().getConfigContents();
		for (var setting : config) {
			writeln(EColors.yellow, "  ", setting.getA(), " ", EColors.seafoam, setting.getB());
		}
	}
	
	private void editConfig() {
		writeln();
		File configFile = QoTSettings.getGameConfig().getFile();
		if (configFile == null) {
			error("Failed to read config file!");
			return;
		}
		
		TextEditorWindow texteditor = new TextEditorWindow(configFile);
		texteditor.setFocusedObjectOnClose(term());
		
		getTopParent().displayWindow(texteditor);
		texteditor.setFocusToLineIfEmpty();
	}
	
	private void reloadConfig() {
		info("Reloading ", Envision.getGameName(), " config..");
		if (QoTSettings.getGameConfig().tryLoad()) writeln(EColors.lgreen, "Success");
		else error("Failed to reload config!");
	}
	
	private void saveConfig() {
		info("Saving ", Envision.getGameName(), " config..");
		if (QoTSettings.getGameConfig().trySave()) writeln(EColors.lgreen, "Success");
		else error("Failed to save config!");
	}
	
	private void resetConfig() {
		info("Reseting ", Envision.getGameName(), " config to default values..");
		if (QoTSettings.getGameConfig().tryReset()) writeln(EColors.lgreen, "Success");
		else error("Failed to reset config!");
	}
	
}
