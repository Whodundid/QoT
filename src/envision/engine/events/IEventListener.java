package envision.engine.events;

@FunctionalInterface
public interface IEventListener {
	
	void onEvent(GameEvent e);
	
}
