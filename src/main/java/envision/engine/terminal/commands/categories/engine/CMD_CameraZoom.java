package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_CameraZoom extends TerminalCommand {
    
    public CMD_CameraZoom() {
        setCategory("Engine");
        expectedArgLength = 0;
    }
    
    @Override public String getName() { return "camerazoom"; }
    @Override public EList<String> getAliases() { return EList.of("zoom"); }
    @Override
    public String getHelpInfo(boolean runVisually) {
        return "Used to specify the zoom of the camera or its limits.";
    }
    @Override public String getUsage() { return "ex: zoom [{empty}|min|max] 5"; }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(2);
        
        if (Envision.theWorld == null) {
            error("There is no world!");
            return;
        }
        
        final var cam = Envision.theWorld.getCamera();
        
        // if no args -- display current zoom
        if (noArgs()) {
            writeln(EColors.yellow, "Current zoom: ", EColors.lgreen, cam.getZoom());
            writeln(EColors.yellow, "Zoom limits: ", EColors.lgreen, "[", cam.getMinZoom(), ", ", cam.getMaxZoom(), "]");
            return;
        }
        
        // if one arg, it should be a number to set the current zoom
        if (oneArg()) {
            double zoom = parseDouble(firstArg());
            cam.setZoom(zoom);
            writeln(EColors.yellow, "Set camera zoom to: ", EColors.lgreen, cam.getZoom());
            return;
        }
        
        // if two args, parse for either [min|max], then number to set
        String target = firstArg();
        if (!target.equals("min") && !target.equals("max")) {
            error("Expected either 'min' or 'max' for first argument!");
            return;
        }
        
        double zoom = parseDouble(secondArg());
        if (target.equals("min")) cam.setMinZoom(zoom);
        else cam.setMaxZoom(zoom);
        writeln(EColors.yellow, "Set ", target, " camera zoom to: ", EColors.lgreen, zoom);
    }
    
}
