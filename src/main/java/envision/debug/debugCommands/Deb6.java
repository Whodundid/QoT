package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.internal.kernel.terminal.window.ETerminalWindow;
import envision.engine.internal.windows.windowBuilder.WindowBuilderScreen;

@SuppressWarnings("unused")
public class Deb6 extends DebugCommand {

    @Override
    public void run(ETerminalWindow termIn, Object... args) {
        Envision.displayScreen(new WindowBuilderScreen());
    }

}