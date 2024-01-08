package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.DebugScriptRunner;
import envision.engine.scripting.LangAPI;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;

@SuppressWarnings("unused")
public class Deb8 extends DebugCommand {

    private static EnvisionProgram program;
    private static EnvisionProgramRunner runner;
    
    //================================================================================
    
    @Override
    public void run(ETerminalWindow termIn, Object... args) throws Exception {
        long start = System.nanoTime();
        if (program == null || runner == null || runner.hasFinished()) {
            build(termIn, args);
            runner.start();
        }
        else {
            runner.executeNextInstruction();
        }
        System.out.println(System.nanoTime() - start);
    }
    
    private static void build(ETerminalWindow termIn, Object... args) throws Exception {
        // add a '#' in front of envision functions to add a breakpoint
        // this breakpoint breaks out of current script execution and
        // preserves the last executed statement position in the script
        String script = ("""
                         
                         //engine.loadWorld("new")
                         playerID = api.getPlayerID()
                         
                         //worldDims = api.getWorldDims()
                         //tileDims = api.getTileDims()
                         
                         //worldWidth = worldDims[0]
                         //worldHeight = worldDims[1]
                         //tileWidth = tileDims[0]
                         //tileHeight = tileDims[1]
                         
                         //println(worldWidth, worldHeight, tileWidth, tileHeight)
                         //println(worldHeight)
                         //println(tileWidth)
                         //println(tileHeight)
                         
                         //api.tpEntity(playerID, 32 * 50, 20)
                         
                         """);
        
        program = new EnvisionProgram("ETerminal_Script", script);
        program.setEnableBlockStatements(true);
        program.setEnableBlockStatementParsing(true);
        
        program.setConsoleReceiver(termIn);
        program.setErrorCallback(termIn);
        
        // add the engine's API to the script
        LangAPI langAPI = LangAPI.getInstance();
        program.addJavaObjectToProgram("api", langAPI);
        program.addJavaObjectToProgram("term", termIn);
        program.addJavaObjectToProgram("engine", Envision.getInstance());
        
        // build the runner
        runner = new EnvisionProgramRunner(program);
    }

}