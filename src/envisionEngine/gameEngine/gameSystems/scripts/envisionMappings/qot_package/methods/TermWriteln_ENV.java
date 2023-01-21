package envision.gameEngine.gameSystems.scripts.envisionMappings.qot_package.methods;

import envision.terminal.window.ETerminal;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import game.QoT;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermWriteln_ENV extends EnvisionFunction {
	
	public TermWriteln_ENV() {
		super(Primitives.VOID, "writeln");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term == null) return;
		
		if (args.length == 0) {
			term.writeln();
			return;
		}
		
		term.writeln(EnvisionStringFormatter.formatPrint(interpreter, args, true));
	}
	
}
