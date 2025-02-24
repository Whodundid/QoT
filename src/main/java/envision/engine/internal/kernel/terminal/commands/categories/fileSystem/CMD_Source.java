package envision.engine.internal.kernel.terminal.commands.categories.fileSystem;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import envision.engine.EngineSettings;
import envision_lang._launch.EnvisionEnvironmnetSettings;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_Source extends AbstractFileCommand {
    
    public CMD_Source() {
        setCategory("System");
        expectedArgLength = 1;
    }

    @Override public String getName() { return "source"; }
    @Override
    public String getHelpInfo(boolean runVisually) {
        return "Used to execute commands within a given script file directly in this terminal";
    }
    @Override public String getUsage() { return "ex: source .termrc [arguments]"; }
    
    @Override
    public void runCommand() {
        expectAtLeast(1);
        
        String path = firstArg();
        File script = parseFilePath(path);
        
        EList<String> args = args();
        EList<String> scriptArgs = EList.newList();
        if (args.size() > 1) {
            for (int i = 1; i < args.size(); i++) {
                scriptArgs.add(args.get(i));
            }
        }
        
        if (!script.exists()) {
            error("Given file path does not exist!");
            return;
        }
        
        try {
            List<String> lines = Files.readAllLines(script.toPath());
            EnvisionProgram scriptProgram = new EnvisionProgram("termrc", lines);
            scriptProgram.setEnableBlockStatements(false);
            scriptProgram.setEnableBlockStatementParsing(false);
            scriptProgram.setConsoleReceiver(term());
            scriptProgram.setErrorCallback(term());
            scriptProgram.addJavaObjectToProgram("term", term());
            
            EnvisionEnvironmnetSettings settings = new EnvisionEnvironmnetSettings(scriptArgs);
            scriptProgram.setLaunchArgs(settings);
            
            EnvisionProgramRunner scriptRunner = new EnvisionProgramRunner(scriptProgram);
            boolean preNewLines = EngineSettings.termCmdNewLines.getBoolean();
            EngineSettings.termCmdNewLines.set(false);
            scriptRunner.start();
            EngineSettings.termCmdNewLines.set(preNewLines);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
