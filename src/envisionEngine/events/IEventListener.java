package envisionEngine.events;

@FunctionalInterface
public interface IEventListener {
	
	void onEvent(GameEvent e);
	
}
