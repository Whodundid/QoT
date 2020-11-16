package events;

public class EventHandler {

	private static EventHandler instance;
	
	public static EventHandler getInstance() {
		return instance = instance != null ? instance : new EventHandler();
	}
	
	private EventHandler() {
		//empty for now
	}
	
	public void onTick() {
		
	}
	
}
