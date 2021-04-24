package scripting.langMappings;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.objects.EnvisionMethod;
import envision.lang.objects.util.EnvisionDataType;
import storageUtil.EArrayList;

public class GameWait extends EnvisionMethod {
	
	public GameWait() {
		super(EnvisionDataType.VOID, "wait");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, EArrayList args) {
		
	}
	
}
