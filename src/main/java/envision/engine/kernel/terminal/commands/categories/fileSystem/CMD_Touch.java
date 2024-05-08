package envision.engine.kernel.terminal.commands.categories.fileSystem;

public class CMD_Touch extends AbstractFileCommand {
	
	public CMD_Touch() {
		expectedArgLength = -1;
	}
	
	@Override public String getName() { return "touch"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Creates a new file(s) with the given name(s)."; }
	@Override public String getUsage() { return "ex: touch file"; }
	
	@Override
	public void runCommand() {
	    expectAtLeast(1, "touch: missing file operand");
	    for (String a : args()) {
            //Files.createFile(Path.of, attrs)
        }
	}
	
}

