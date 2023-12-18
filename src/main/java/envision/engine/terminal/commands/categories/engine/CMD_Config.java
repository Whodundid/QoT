package envision.engine.terminal.commands.categories.engine;

import java.io.File;

import envision.Envision;
import envision.engine.settings.config.EnvisionConfigFile;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.file.FileOpener;

public class CMD_Config extends TerminalCommand {
	
	public CMD_Config() {
		setCategory("Engine");
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "config"; }
	@Override public EList<String> getAliases() { return EList.of("conf"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides a means for interfacing with the game's config file"; }
	
	@Override
	public String getUsage() {
	    return "ex: conf {config_name}   -OR-   conf {config_name} 'edit|reload|save|reset'   ---   Where {config_name} == 'engine' | 'game'";
	}
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
	    if (args.size() <= 1) basicTabComplete(termIn, args, "engine", "game");
	    else if (args.size() <= 2) basicTabComplete(termIn, args, "edit", "reload", "save", "reset");
	}

	@Override
	public void runCommand() {
	    expectNoMoreThan(2);
	    
		if (noArgs()) {
			displayConfig(Envision.getEngineConfig());
			if (hasModifier("-a")) FileOpener.openFile(Envision.getEngineConfig().getFile());
			return;
		}
		
		EnvisionConfigFile config = null;
		
		switch (firstArg().toLowerCase()) {
		case "engine": config = Envision.getEngineConfig(); break;
		case "game": config = Envision.getGameConfig(); break;
		default:
		    errorUsage("Invalid config target argument!");
		    return;
        }

		expectNotNull(config, "Error! The config file for '" + firstArg() + "' is null!");
		
		if (oneArg()) {
		    displayConfig(config);
            if (hasModifier("-a")) FileOpener.openFile(Envision.getEngineConfig().getFile());
            return;
        }
		else if (twoArgs()) {
            switch (secondArg().toLowerCase()) {
            case "edit": editConfig(config); break;
            case "rel":
            case "reload":
            case "l":
            case "load": reloadConfig(config); break;
            case "s":
            case "save": saveConfig(config); break;
            case "res":
            case "reset": resetConfig(config); break;
            default:
                errorUsage("Invalid action argument!");
            }
		}
	}
	
	private void displayConfig(EnvisionConfigFile config) {
		writeln(EColors.aquamarine, config.getConfigName(), ":");
		
		var contents = config.getConfigContents();
		for (var setting : contents) {
			writeln(EColors.yellow, "  ", setting.getA(), " ", EColors.seafoam, setting.getB());
		}
	}
	
	private void editConfig(EnvisionConfigFile config) {
		writeln();
		File configFile = config.getFile();
		if (configFile == null) {
			error("Failed to read config file!");
			return;
		}
		
		TextEditorWindow texteditor = new TextEditorWindow(configFile);
		texteditor.setFocusedObjectOnClose(term());
		
		getTopParent().displayWindow(texteditor);
		texteditor.setFocusToLineIfEmpty();
	}
	
	private void reloadConfig(EnvisionConfigFile config) {
		info("Reloading ", Envision.getGameName(), " config..");
		if (config.tryLoad()) writeln(EColors.lgreen, "Success");
		else error("Failed to reload config!");
	}
	
	private void saveConfig(EnvisionConfigFile config) {
		info("Saving ", Envision.getGameName(), " config..");
		if (config.trySave()) writeln(EColors.lgreen, "Success");
		else error("Failed to save config!");
	}
	
	private void resetConfig(EnvisionConfigFile config) {
		info("Reseting ", Envision.getGameName(), " config to default values..");
		if (config.tryReset()) writeln(EColors.lgreen, "Success");
		else error("Failed to reset config!");
	}
	
}
