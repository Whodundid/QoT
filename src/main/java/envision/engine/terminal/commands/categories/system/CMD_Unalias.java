package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Unalias extends TerminalCommand {
	
	public CMD_Unalias() {
		setCategory("System");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "unalias"; }
	@Override public String getUsage() { return "ex: unalias banana (where 'banana' was an existing alias)"; }
	
	@Override
    public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
	    super.basicTabComplete(termIn, args, TerminalCommandHandler.getInstance().getCommandAliases().keySet());
	}
	
	@Override
	public String getHelpInfo(boolean runVisually) {
	    return "Removes an existing alias under the given name";
	}
	
	@Override
	public Object runCommand() {
	    expectAtLeast(1);
	    
	    // attempt to unalias each argument
	    for (String input : args()) {
	        // make sure input argument isn't empty
	        if (input.isBlank()) {
	            errorUsage("Error! Alias name cannot be empty!");
	            continue;
	        }
	        
	        // ensure that the input starts with a letter
	        if (!input.matches("^[a-zA-Z].*$")) {
	            errorUsage("Alias name must start with a letter!");
	            continue;
	        }
	        
	        unalias(input.trim());
	    }
	    
	    return null;
	}
	
	private void unalias(String alias) {
	    // check if we even have an alias registered under the given input
	    var aliases = TerminalCommandHandler.getInstance().getCommandAliases();
	    
	    String foundAlias = aliases.get(alias);
	    if (foundAlias == null) {
	        error("No matching alias found for input '" + alias + "'");
	        return;
	    }
	    
	    // if it exists, unregister it
	    TerminalCommandHandler.getInstance().removeCommandAlias(alias);
	    
	    writeln(EColors.lgreen, "Removed alias '", EColors.mc_lightpurple, alias, EColors.lgreen, "'");
	}
	
}
