package engine.scripting.langMappings.qot_package.objects;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionStringClass;
import eutil.datatypes.Box2;
import eutil.datatypes.util.BoxList;
import world.GameWorld;

/** Wrapper Envision class for a QoT GameWorld object. */
public class QoTWorldClass extends EnvisionClass {
	
	public QoTWorldClass() {
		super("World");
		
		//classScope = new Scope(interpreter.scope());
		//addConstructor(new EnvisionFunction(new ParameterData(STRING)));
		//addConstructor(new EnvisionFunction(new ParameterData(EnvFile.FILE_DATATYPE)));
	}
	
	//-----------------------------------------
	
	public static ClassInstance buildWorldInstance(EnvisionInterpreter interpreter, String worldNameIn) {
		QoTWorldClass world = new QoTWorldClass();
		var str = EnvisionStringClass.newString(worldNameIn);
		return world.buildInstance(interpreter, new EnvisionObject[] {str});
	}
	
	private static GameWorld createWrapWorld(Object worldInstance) {
		//Object wObject = convert(worldInstance);
		//String n = null;
		
		//if (wObject instanceof ClassInstance) n = EnvFile.getFilePath((ClassInstance) worldInstance);
		//else if (wObject instanceof String) n = (String) wObject;
		
		//if (n == null) return null;
		
		//n = (n.endsWith(".twld")) ? n : n + ".twld";
		//File f = new File(QotGameSettings.getEditorWorldsDir(), n);
		//return new GameWorld(f);
		return null;
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		// standard envision class creation
		//Scope instanceScope = new Scope(classScope);
		//ClassInstance inst = new ClassInstance(this, instanceScope);
		//instanceScope.define("this", inst);
		
		// handle java wrapping
		//GameWorld w = createWrapWorld(args[0]);
		//EnvisionObject wrappedWorld = EnvisionObject.javaObjectWrapper("_iWorld_", w);
		// restrict access
		//wrappedWorld.setRestricted();
		//wrappedWorld.setFinal();
		// define the internal field
		//instanceScope.define("_iWorld_", wrappedWorld);
		
		// create field and method instances
		//createFields(instanceScope, w);
		//createMethods(instanceScope, inst);
		
		return null;
	}
	
	//-----------------------------------------
	
	private void createFields(Scope scope, GameWorld w) {
		BoxList<String, Object> ifields = new BoxList();
		
		ifields.add("name", w.getName());
		ifields.add("width", w.getWidth());
		ifields.add("height", w.getHeight());
		ifields.add("tileWidth", w.getTileWidth());
		ifields.add("tileHeight", w.getTileHeight());
		
		for (Box2<String, Object> o : ifields) {
			EnvisionObject nfield = ObjectCreator.wrap(o.getB());
			//nfield.setName(o.getA());
			//nfield.setPublic();
			nfield.setFinal();
			scope.define(o.getA(), nfield);
		}
	}
	
	private void createMethods(Scope scope, ClassInstance inst) {
		//EArrayList<WorldMeth> imethods = new EArrayList();
		
		//imethods.add(new GetWorldName());
		//imethods.add(new GetWidth());
		//imethods.add(new GetHeight());
		//imethods.add(new GetZoom());
		//imethods.add(new GetTileAt());
		
		//for (WorldMeth m : imethods) {
		//	m.setInst(inst);
		//	scope.define(m.getName(), m);
		//}
	}
	
	//-----------------------------------------
	
	/** A specialized type of method wrapper that deals with mapping Java File objects to Envision. */
	/*
	private abstract class WorldMeth extends EnvisionFunction {
		
		private ClassInstance inst;
		
		public WorldMeth(Primitives returnTypeIn, String nameIn) { super(returnTypeIn, nameIn, new ParameterData()); }
		public WorldMeth(EnvisionDatatype returnTypeIn, String nameIn) { super(returnTypeIn, nameIn, new ParameterData()); }
		public WorldMeth(Primitives returnTypeIn, String nameIn, ParameterData paramsIn) { super(returnTypeIn, nameIn, paramsIn); }
		public WorldMeth(EnvisionDatatype returnTypeIn, String nameIn, ParameterData paramsIn) { super(returnTypeIn, nameIn, paramsIn); }
		
		public void setInst(ClassInstance instIn) { inst = instIn; }
		
		// Returns the wrapped Java GameWorld object. Does not guarantee the file location actually exists however.
		protected GameWorld getW() {
			EnvisionObject obj = inst.get("_iWorld_");
			GameWorld w = (GameWorld) obj.getJavaObject();
			return w;
		}
	}
	*/
	//-----------------------------------------
	/*
	private class GetWorldName extends WorldMeth {
		public GetWorldName() { super(Primitives.STRING, "getWorldName"); }
		@Override public int argLength() { return 0; }
		@Override public void invoke(EnvisionInterpreter interpreter, Object[] args) { checkArgs(args); ret(getW().getName()); }
	}
	
	private class GetWidth extends WorldMeth {
		public GetWidth() { super(Primitives.INT, "getWidth"); }
		@Override public int argLength() { return 0; }
		@Override public void invoke(EnvisionInterpreter interpreter, Object[] args) { checkArgs(args); ret(getW().getWidth()); }
	}
	
	private class GetHeight extends WorldMeth {
		public GetHeight() { super(Primitives.INT, "getHeight"); }
		@Override public int argLength() { return 0; }
		@Override public void invoke(EnvisionInterpreter interpreter, Object[] args) { checkArgs(args); ret(getW().getHeight()); }
	}
	
	private class GetZoom extends WorldMeth {
		public GetZoom() { super(Primitives.DOUBLE, "getZoom"); }
		@Override public int argLength() { return 0; }
		@Override public void invoke(EnvisionInterpreter interpreter, Object[] args) { checkArgs(args); ret(getW().getZoom()); }
	}
	
	private class GetTileAt extends WorldMeth {
		public GetTileAt() { super(new EnvisionDatatype("WorldTile"), "getTileAt"); }
		@Override public int argLength() { return 2; }
		@Override public ParameterData argTypes() { return new ParameterData(Primitives.INT, Primitives.INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			checkArgs(args); ret(getW().getTileAt((int) (long) args[0], (int) (long) args[1]));
		}
	}
	*/
}
