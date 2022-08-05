package engine.scriptingEngine.envisionMappings.qot_package.methods;

import engine.terminal.window.ETerminal;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import main.QoT;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermWriteln_ENV extends EnvisionFunction {
	
	public TermWriteln_ENV() {
		super(Primitives.VOID, "writeln");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term != null) {
			String s = EnvisionStringFormatter.formatPrint(interpreter, args);
			//int c = (args.length > 1) ? Integer.parseInt(String.valueOf(args[1])) : 0xffffffff;
			//term.writeln(s, c);
			term.writeln(s);
		}
	}
	
}
