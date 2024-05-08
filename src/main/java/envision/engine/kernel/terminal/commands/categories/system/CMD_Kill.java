package envision.engine.kernel.terminal.commands.categories.system;

import envision.engine.kernel.EnvisionKernel;
import envision.engine.kernel.process.IEnvisionProcess;
import envision.engine.kernel.terminal.commands.TerminalCommand;

//Author: Hunter Bragg

public class CMD_Kill extends TerminalCommand {
	
	public CMD_Kill() {
		setCategory("System");
	}

	@Override public String getName() { return "kill"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Kills a process by PID"; }
	@Override public String getUsage() { return "ex: kill 14"; }
	@Override public byte requiredPermissionLevel() { return 2; }
	
	@Override
	public void runCommand() {
		expectAtLeast(1, "Expected a process ID");
		
		int pid = parseInt(0);
		IEnvisionProcess process = EnvisionKernel.getInstance().getProcessByPid(pid);
		
		if (process == null) {
		    error("Invalid process ID -- There is no active process under that PID!");
		}
		else {
		    process.kill();
		    writeln("Killing: " + pid);
		}
	}
	
}
