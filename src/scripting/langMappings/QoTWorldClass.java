package scripting.langMappings;

/** Wrapper Envision class for a QoT GameWorld object. */
public class QoTWorldClass {}/*extends EnvisionClass {
	
	private GameWorld theWorld;
	
	private BoxHolder<String, Object> ifields = new BoxHolder();
	private EArrayList<EnvisionMethod> imethods = new EArrayList();
	
	public QoTWorldClass(GameWorld worldIn) {
		super((worldIn != null) ? worldIn.getName() : "NULL");
		if (worldIn == null) { throw new EnvisionError("The selected world is null!"); }
		theWorld = worldIn;
		createFields();
		createMethods();
	}
	
	private void createFields() {
		ifields.add("name", theWorld.getName());
		ifields.add("width", theWorld.getWidth());
		ifields.add("height", theWorld.getHeight());
		ifields.add("tileWidth", theWorld.getTileWidth());
		ifields.add("tileHeight", theWorld.getTileHeight());
	}
	
	private void createMethods() {
		imethods.add(new GetWorldName());
		imethods.add(new GetWidth());
		imethods.add(new GetHeight());
		imethods.add(new GetZoom());
		imethods.add(new GetTileAt());
	}
	
	//-----------------------------------------
	
	public ClassInstance build(EnvisionInterpreter interpreter) {
		setScope(new Scope(interpreter.scope()));
		ClassInstance inst = buildInstance(interpreter, new EArrayList());
		
		Scope s = inst.getScope();
		
		for (Box2<String, Object> o : ifields) { s.define(o.getA(), ObjectCreator.createObject(o.getB()).setPublic().setFinal()); }
		for (EnvisionMethod m : imethods) { s.define(m.getName(), m.setPublic()); }
		
		return inst;
	}
	
	//-----------------------------------------
	
	private class GetWorldName extends EnvisionMethod {
		public GetWorldName() { super(EnvisionDataType.STRING, "getWorldName"); }
		@Override public int argSize() { return 0; }
		@Override public void call(EnvisionInterpreter interpreter, EArrayList args) { checkArgs(args); ret(theWorld.getName()); }
	}
	
	private class GetWidth extends EnvisionMethod {
		public GetWidth() { super(EnvisionDataType.INT, "getWidth"); }
		@Override public int argSize() { return 0; }
		@Override public void call(EnvisionInterpreter interpreter, EArrayList args) { checkArgs(args); ret(theWorld.getWidth()); }
	}
	
	private class GetHeight extends EnvisionMethod {
		public GetHeight() { super(EnvisionDataType.INT, "getHeight"); }
		@Override public int argSize() { return 0; }
		@Override public void call(EnvisionInterpreter interpreter, EArrayList args) { checkArgs(args); ret(theWorld.getHeight()); }
	}
	
	private class GetZoom extends EnvisionMethod {
		public GetZoom() { super(EnvisionDataType.DOUBLE, "getZoom"); }
		@Override public int argSize() { return 0; }
		@Override public void call(EnvisionInterpreter interpreter, EArrayList args) { checkArgs(args); ret(theWorld.getZoom()); }
	}
	
	private class GetTileAt extends EnvisionMethod {
		public GetTileAt() { super("WorldTile", "getTileAt"); }
		@Override public int argSize() { return 2; }
		@Override public ParameterData argTypes() { return new ParameterData("int", "int"); }
		@Override public void call(EnvisionInterpreter interpreter, EArrayList args) { checkArgs(args); ret(theWorld.getTileAt((int) (long) args.get(0), (int) (long) args.get(1))); }
	}
	
}
*/