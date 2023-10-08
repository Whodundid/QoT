package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.ListableCommand;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class CMD_Alias extends TerminalCommand implements ListableCommand {
    
    public CMD_Alias() {
        setCategory("System");
    }
    
    @Override public String getName() { return "alias"; }
    @Override public String getUsage() { return "ex: alias banana='cat'"; }
    
    @Override
    public String getHelpInfo(boolean runVisually) {
        return "Creates a string that can act as a substitue for another word or command when" +
               "it is used at the beginning of user input";
    }
    
    @Override
    public Object runCommand() {
        expectAtLeast(1);
        
        // combine all args in the event that they type "alias banana = 'cat'"
        String input = EStringUtil.combineAll(args(), " ");
        // trim leading whitespace
        input = input.replaceFirst("^\\s*", "");
        
        // make sure input does not actually start with the 
        if (input.startsWith("=")) {
            errorUsage("Error! Alias name must not be empty!");
            return null;
        }
        
        // make sure input has a '='
        if (!input.contains("=")) {
            errorUsage("Expected a '=' to be present for an alias target!");
            return null;
        }
        
        String[] inputParts = input.split("=");
        
        // ensure there are at least 2 values "banana | 'cat'"
        if (inputParts.length < 2) {
            errorUsage("Expected a valid alias replacement string!");
            return null;
        }
        
        // extract alias name
        String aliasName = inputParts[0];
        String aliasValue;
        
        // ensure that the alias name starts with a letter
        if (!aliasName.matches("^[a-zA-Z].*$")) {
            errorUsage("Alias name must start with a letter!");
            return null;
        }
        
        // join all remaining parts to be the aliases value
        var aliasValueBuilder = new EStringBuilder();
        for (int i = 1; i < inputParts.length; i++) {
            aliasValueBuilder.a(inputParts[i]);
        }
        aliasValue = aliasValueBuilder.toString();
        
        // trim out ' and " characters
        if (EStringUtil.startsAndEndsWithAny(aliasValue, "'", "\"")) {
            aliasValue = aliasValue.substring(1, aliasValue.length() - 1);
        }
        
        // register the alias command (it's ok if we step on existing aliases)
        TerminalCommandHandler.getInstance().addCommandAlias(aliasName, aliasValue);
        
        writeln(EColors.lgreen, "Added Alias '", EColors.mc_lightpurple, aliasName, EColors.lgreen, "'");
        return null;
    }
    
    @Override
    public EList<String> getList() {
        return TerminalCommandHandler.getInstance().getCommandAliasNames();
    }
    
}
