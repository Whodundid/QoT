package scripting;

public class WorldInit {
	
	public static final String worldInit =
	""" 
	boolean running = true
	int startTime = millis()
	
	w = world
	name = w.name
	dims = [w.width, w.height]
	
	print name
	print dims
	
	""";
	
}
