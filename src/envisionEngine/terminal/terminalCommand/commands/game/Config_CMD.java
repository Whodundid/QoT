package envision.terminal.terminalCommand.commands.game;

import java.io.File;

import envision.Envision;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import envision.windowLib.bundledWindows.TextEditorWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.file.FileOpener;
import game.settings.QoTSettings;

public class Config_CMD extends TerminalCommand {
	
	public Config_CMD() {
		setCategory("game");
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "config"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("conf"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides an interface to the game's config file"; }
	@Override public String getUsage() { return "ex: conf    -OR-    conf 'edit|reload|save|reset'"; }
	
	@Override
	public void handleTabComplete(ETerminal termin, EArrayList<String> args) {
		if (args.size() <= 1) basicTabComplete(termin, args, "edit", "reload", "save", "reset");
	}

	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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
	
	private void displayConfig(ETerminal termIn) {
		termIn.writeln(EColors.aquamarine, Envision.getGameName(), " config:");
		
		var config = QoTSettings.getGameConfig().getConfigContents();
		for (var setting : config) {
			termIn.writeln(EColors.yellow, "  ", setting.getA(), " ", EColors.seafoam, setting.getB());
		}
	}
	
	private void editConfig(ETerminal termIn) {
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
	
	private void reloadConfig(ETerminal termIn) {
		termIn.info("Reloading " + Envision.getGameName() + " config..");
		if (QoTSettings.getGameConfig().tryLoad()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to reload config!");
	}
	
	private void saveConfig(ETerminal termIn) {
		termIn.info("Saving " + Envision.getGameName() + " config..");
		if (QoTSettings.getGameConfig().trySave()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to save config!");
	}
	
	private void resetConfig(ETerminal termIn) {
		termIn.info("Reseting " + Envision.getGameName() + " config to default values..");
		if (QoTSettings.getGameConfig().tryReset()) termIn.writeln(EColors.lgreen, "Success");
		else termIn.writeln(EColors.lred, "Failed to reset config!");
	}
	
}
