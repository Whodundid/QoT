package envision.engine.terminal.commands.categories.engine;

import envision.engine.terminal.commands.TerminalCommand;
import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionEnvironmnetSettings;
import envision_lang._launch.EnvisionProgram;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Envision extends TerminalCommand {
    
    public CMD_Envision() {
        setCategory("System");
        allowAnyModifier = true;
    }
    
    @Override public String getName() { return "envision"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Mappings to the Envision Scripting Language"; }
    @Override public String getUsage() { return "ex: envision 'file'"; }
    
    @Override
    public Object runCommand() {
        if (noArgs()) {
            writeln(EnvisionLang.getVersionString(), EColors.seafoam);
            info("""
                 To run an Envision script, add an Envision program directory
                 along with any of its launch arguments to the end of this command.
                 """);
            usage();
            return null;
        }
        
        String fileName = firstArg();
        EnvisionProgram program = new EnvisionProgram(fileName);
        
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
            //QoT.getEnvision().runProgram(program);
            writeln(EColors.lgreen, "END ", EColors.yellow, (System.currentTimeMillis() - start), "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
            error(e);
        }
        
        return null;
    }
    
}
