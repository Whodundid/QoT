package envision.engine.scripting.envisionMappings.qot_package.methods;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.Primitives;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermWriteln_ENV extends EnvisionFunction {
	
	public TermWriteln_ENV() {
		super(Primitives.VOID, "writeln");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminalWindow term = (ETerminalWindow) Envision.getTopScreen().getWindowInstance(ETerminalWindow.class);
		if (term == null) return;
		
		if (args.length == 0) {
			term.writeln();
			return;
		}
		
		term.writeln(EnvisionStringFormatter.formatPrint(interpreter, args, true));
	}
	
}
