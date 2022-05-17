package engine.scripting.langMappings.qot_package.methods;

import engine.QoT;
import engine.terminal.TerminalHandler;
import engine.terminal.window.ETerminal;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.strings.StringUtil;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermCall_ENV extends EnvisionFunction {
	
	public TermCall_ENV() {
		super(Primitives.VAR, "term");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term != null && args.length > 0) {
			String s = StringUtil.combineAll(args, " ");
			TerminalHandler.getInstance().executeCommand(term, s, false, false);
		}
	}
	
}
