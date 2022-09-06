package envision.game.scripts.envisionMappings.qot_package.methods;

import envision.terminal.TerminalHandler;
import envision.terminal.window.ETerminal;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.strings.EStringUtil;
import game.QoT;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermCall_ENV extends EnvisionFunction {
	
	public TermCall_ENV() {
		super(Primitives.VAR, "term");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term != null && args.length > 0) {
			String s = EStringUtil.combineAll(args, " ");
			TerminalHandler.getInstance().executeCommand(term, s, false, false);
		}
	}
	
}
