package envision.engine.internal.kernel.terminal.commands.categories.engine;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import envision.engine.internal.kernel.terminal.commands.categories.fileSystem.AbstractFileCommand;
import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionEnvironmnetSettings;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Envision extends AbstractFileCommand {
    
    public CMD_Envision() {
        setCategory("System");
        allowAnyModifier = true;
    }
    
    @Override public String getName() { return "envision"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Mappings to the Envision Scripting Language"; }
    @Override public String getUsage() { return "ex: envision 'file'"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void runCommand() throws Exception {
        if (noArgs()) {
            writeln(EnvisionLang.getVersionString(), EColors.seafoam);
            info("""
                 To run an Envision script, add an Envision program directory
                 along with any of its launch arguments to the end of this command.
                 """);
            usage();
            return;
        }
        
        String fileName = firstArg();
        // build from relative dir
        File f = parseFilePath(dir(), fileName);
        // if relative didn't exist, try using full path
        if (!f.exists()) f = new File(firstArg());
        
        List<String> lines = Files.readAllLines(f.toPath());
        EnvisionProgram program = new EnvisionProgram(f.getAbsolutePath(), lines);
        program.addJavaObjectToProgram("term", term);
        program.setConsoleReceiver(term);
        program.setErrorCallback(term);
        
        //arguments to be passed to the Envision Language
        EList<String> toParse = EList.newList();
        
        EList<String> modifiers = getParsedModifiers();
        if (modifiers.isNotEmpty()) {
            for (int i = 0; i < modifiers.size(); i++) { toParse.add(modifiers.get(i)); }
            var launchArgs = EnvisionEnvironmnetSettings.of(toParse);
            program.setLaunchArgs(launchArgs);
        }
        
        try {
            long start = System.currentTimeMillis();
            var runner = new EnvisionProgramRunner(program);
            runner.start(subArgs(1));
            //QoT.getEnvision().runProgram(program);
            writeln(EColors.lgreen, "END ", EColors.yellow, (System.currentTimeMillis() - start), "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
            error(e);
        }
    }
    
}
