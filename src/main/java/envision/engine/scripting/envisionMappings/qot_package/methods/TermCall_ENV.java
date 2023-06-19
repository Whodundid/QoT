package envision.engine.scripting.envisionMappings.qot_package.methods;

import envision.Envision;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.strings.EStringUtil;

/** Creates a mapping between Envision and the QoT terminal. */
public class TermCall_ENV extends EnvisionFunction {
	
	public TermCall_ENV() {
		super(Primitives.VAR, "term");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ETerminalWindow term = (ETerminalWindow) Envision.getTopScreen().getWindowInstance(ETerminalWindow.class);
		if (term != null && args.length > 0) {
			String s = EStringUtil.combineAll(args, " ");
			TerminalCommandHandler.getInstance().executeCommand(term, s, false);
		}
	}
	
}