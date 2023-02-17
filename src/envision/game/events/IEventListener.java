package envision.game.events;

@FunctionalInterface
public interface IEventListener {
	
	void onEvent(GameEvent e);
	
}
