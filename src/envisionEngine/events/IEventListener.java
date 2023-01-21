package envision.events;

@FunctionalInterface
public interface IEventListener {
	
	void onEvent(GameEvent e);
	
}
