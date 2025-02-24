package envision.engine.internal.kernel.terminal.commands.categories.engine;

import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import envision.engine.internal.kernel.terminal.window.ETerminalWindow;
import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Font extends TerminalCommand {
    
    public CMD_Font() {
        setCategory("Engine");
        expectedArgLength = 1;
    }
    
    @Override public String getName() { return "font"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Displays or sets the current engine font."; }
    @Override public String getUsage() { return "ex: font smooth"; }
    
    @Override
    public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
        basicTabComplete(termIn, args, "8bit", "new", "default", "courier", "smooth");
    }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(1);
        
        if (noArgs()) {
            writeln(EColors.lgreen, "current Font: ",
                    EColors.lgreen, "'",
                    EColors.aquamarine, FontRenderer.getCurrentFont().getFontName(),
                    EColors.lgreen, "'");
            return;
        }
        else if (firstArg() instanceof String s) {
            if (s.equals("8bit")) FontRenderer.setCurrentFont(FontRenderer.font8);
            if (s.equals("new")) FontRenderer.setCurrentFont(FontRenderer.newFont);
            if (s.equals("default")) FontRenderer.setCurrentFont(FontRenderer.defaultFont);
            if (s.equals("courier")) FontRenderer.setCurrentFont(FontRenderer.courier);
            if (s.equals("smooth")) FontRenderer.setCurrentFont(FontRenderer.smooth);
        }
    }
    
}
