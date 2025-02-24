package envision.engine.internal.kernel.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_KillEntity extends TerminalCommand {
    
    public CMD_KillEntity() {
        setCategory("Game");
        expectedArgLength = 0;
    }

    @Override public String getName() { return "killEntity"; }
    @Override public EList<String> getAliases() { return EList.of("ke"); }
    @Override public String getHelpInfo(boolean runVisually) { return "Kills an Entity Based on Entity ID"; }
    @Override public String getUsage() { return "ex: ke 0"; }
    
    @Override
    public void runCommand() {
        expectExactly(1);
        
        if (Envision.theWorld == null) {
            error("Current World is Null");
            return;
        }
        
        var entities = Envision.theWorld.getEntitiesInWorld();
        
        String id = firstArg();
        for (var e : entities) {
            if (EUtil.isEqual(e.getWorldID(), id)) {
                Envision.theWorld.removeEntity(e);
                writeln("Killed entity! " + id, EColors.lgreen);
                break;
            }
        }
    }
    
}
