package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_LockCam extends TerminalCommand {
    
    public CMD_LockCam() {
        setCategory("Engine");
        expectedArgLength = 0;
    }
    
    @Override public String getName() { return "cameraedgelock"; }
    @Override public EList<String> getAliases() { return EList.of("edgelock"); }
    @Override
    public String getHelpInfo(boolean runVisually) {
        return "Specifies whether or not the camera is locked to the edge of the world.";
    }
    @Override public String getUsage() { return "ex: edgelock"; }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(1);
        
        if (Envision.theWorld == null) {
            error("There is no world!");
            return;
        }
        
        boolean val = false;
        
        if (noArgs()) {
            val = !Envision.theWorld.getCamera().isEdgeLocked();
        }
        else {
            String input = firstArg().toLowerCase();
            
            switch (input) {
            case "t", "true": val = true; break;
            case "f", "false": val = false; break;
            default:
                errorUsage("Expected either a [true|false|t|f] input!");
                return;
            }
        }
        
        Envision.theWorld.getCamera().setEdgeLocked(val);
        
        String displayString = "";
        if (val) displayString = EColors.lgreen + "Enabled";
        else displayString = EColors.lred + "Disabled";
        writeln(EColors.yellow, "Camera Edge Locking: " + displayString);
    }
    
}
