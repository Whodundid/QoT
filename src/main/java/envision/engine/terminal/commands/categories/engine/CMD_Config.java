package envision.engine.terminal.commands.categories.engine;

import java.io.File;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
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
	@Override public EList<String> getAliases() { return new EArrayList<>("conf"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides a means for interfacing with the game's config file"; }
	@Override public String getUsage() { return "ex: conf    -OR-    conf 'edit|reload|save|reset'"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termin, EList<String> args) {
		if (args.size() <= 1) basicTabComplete(termin, args, "edit", "reload", "save", "reset");
	}

	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			displayConfig(termIn);
			if (hasModifier("-a")) FileOpener.openFile(QoTSettings.getGameConfig().getFile());
		}
		else if (args.size() == 1) {
			switch (args.get(0)) {
			case "edit": editConfig(termIn); break;
			case "rel":
			case "reload":
			case "l":
			case "load": reloadConfig(termIn); break;
			case "s":
			case "save": saveConfig(termIn); break;
			case "res":
			case "reset": resetConfig(termIn); break;
			default:
				errorUsage(termIn, "Invalid action argument!");
			}
		}
	}
	
	private void displayConfig(ETerminalWindow termIn) {
		termIn.writeln(EColors.aquamarine, Envision.getGameName(), " config:");
		
		var config = QoTSettings.getGameConfig().getConfigContents();
		for (var setting : config) {
			termIn.writeln(EColors.yellow, "  ", setting.getA(), " ", EColors.seafoam, setting.getB());
		}
	}
	
	private void editConfig(ETerminalWindow termIn) {
		termIn.writeln();
		File configFile = QoTSettings.getGameConfig().getFile();
		if (configFile == null) {
			termIn.error("Failed to read config file!");
			return;
		}
		
		TextEditorWindow texteditor = new TextEditorWindow(configFile);
		texteditor.setFocusedObjectOnClose(termIn);
		
		termIn.getTopParent().displayWindow(texteditor);
		texteditor.setFocusToLineIfEmpty();
	}
	
	private void reloadConfig(ETerminalWindow termIn) {
		termIn.info("Reloading " + Envision.getGameName() + " config..");
		if (QoTSettings.getGameConfig().tryLoad()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to reload config!");
	}
	
	private void saveConfig(ETerminalWindow termIn) {
		termIn.info("Saving " + Envision.getGameName() + " config..");
		if (QoTSettings.getGameConfig().trySave()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to save config!");
	}
	
	private void resetConfig(ETerminalWindow termIn) {
		termIn.info("Reseting " + Envision.getGameName() + " config to default values..");
		if (QoTSettings.getGameConfig().tryReset()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to reset config!");
	}
	
}
