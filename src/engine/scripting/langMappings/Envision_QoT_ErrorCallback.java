package engine.scripting.langMappings;

import engine.terminal.window.ETerminal;
import envision.EnvisionErrorCallback;
import envision.exceptions.EnvisionError;
import main.QoT;

public class Envision_QoT_ErrorCallback extends EnvisionErrorCallback {

	@Override
	public void handleError(EnvisionError e) {
		handleException(e);
	}

	@Override
	public void handleException(Exception e) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term != null) {
			term.javaError(e.toString());
		}
	}
	
}
