package engine.scriptingEngine.envisionMappings.qot_package;

import engine.scriptingEngine.envisionMappings.qot_package.methods.LoadWorld_ENV;
import engine.scriptingEngine.envisionMappings.qot_package.methods.TermCall_ENV;
import engine.scriptingEngine.envisionMappings.qot_package.methods.TermWriteln_ENV;
import engine.scriptingEngine.envisionMappings.qot_package.objects.QoTWorldClass;
import envision.packages.EnvisionLangPackage;

public class Envision_QoT_Package extends EnvisionLangPackage {

	public static final String packageName = "qot";
	
	public Envision_QoT_Package() {
		super(packageName);
	}
	
	@Override
	public void buildFunctions() {
		packageScope.defineFunction(new TermCall_ENV());
		packageScope.defineFunction(new TermWriteln_ENV());
		packageScope.defineFunction(new LoadWorld_ENV());
	}
	
	@Override
	public void buildFields() {
	}
	
	@Override
	public void buildClasses() {
		packageScope.defineClass(new QoTWorldClass());
	}
	
	@Override
	public void buildPackages() {
	}
	
}
