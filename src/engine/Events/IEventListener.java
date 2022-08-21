package engine.Events;

@FunctionalInterface
public interface IEventListener {
	
	void onEvent(GameEvent e);
	
}
