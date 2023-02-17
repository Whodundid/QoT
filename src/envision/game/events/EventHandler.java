package envision.game.events;

import java.util.HashMap;

import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.BoxList;

public class EventHandler {
	
	//------------------
	// Static Singleton
	//------------------
	
	private static final EventHandler instance = new EventHandler();
	public static EventHandler getInstance() { return instance; }
	// Constructor -- private
	private EventHandler() {}
	
	//--------
	// Fields
	//--------
	
	/**
	 * The queue of events that will be distributed to subscribers.
	 */
	private final EArrayList<GameEvent> eventQueue = new EArrayList<>();
	/**
	 * Maps event types to the subscriber(s) who are interested in them.
	 */
	private final HashMap<EventType, EArrayList<IEventListener>> subscriberMap = new HashMap<>();
	
	private final BoxList<IEventListener, EventType> toSubscribe = new BoxList<>();
	private final BoxList<IEventListener, EventType> toUnsubscribe = new BoxList<>();
	
	//---------
	// Methods
	//---------
	
	/**
	 * Processes events in the order in which they were received.
	 */
	public synchronized void onGameTick() {
		//process any subscribers scheduled to be added
		if (toSubscribe.isNotEmpty()) addSubscribers();
		//process any subscribers scheduled to be removed
		if (toUnsubscribe.isNotEmpty()) removeSubscribers();
		
		//ignore if there are no events to process
		if (eventQueue.isEmpty()) return;
		
		//process next event on event queue
		GameEvent e = eventQueue.removeFirst();
		EventType type = e.getType();
		
		//grab subscriber list for the current event
		EArrayList<IEventListener> subscribers = subscriberMap.get(type);
		if (subscribers == null) return;
		
		//distribute event to subscribers
		if (subscribers.isNotEmpty()) {
			for (var s : subscribers) {
				//if the subscriber is in the process of unsubscribing from
				//this exact event type, don't notify them
				if (toUnsubscribe.containsBoth(s, type)) continue;
				//otherwise, notify subscriber of the new event
				s.onEvent(e);
			}
		}
	}
	
	public void postEvent(GameEvent e) { eventQueue.offer(e); }
	public static void postGameEvent(GameEvent e) { instance.postEvent(e); }
	
	public void clearEventQueue() { eventQueue.clear(); }
	public static void clearEvents() { instance.clearEventQueue(); }
	
	/**
	 * Schedules the given object to be added as a subscriber to the given event type.
	 * <p>
	 * Any time a new event matching the given event type is posted, all
	 * subscribers for that specific event type are notified.
	 * 
	 * @param subscriber The object subscribing to the given event type
	 * @param eventToSubscribeOn The event type the object wants to listen for
	 */
	public synchronized void addSubscriber(IEventListener subscriber, EventType eventToSubscribeOn) {
		toSubscribe.add(subscriber, eventToSubscribeOn);
	}
	
	/**
	 * Schedules the given object to no longer receive updates on the given event type.
	 * <p>
	 * Any time a new event matching the given event type is posted, all
	 * subscribers for that specific event type are notified.
	 * 
	 * @param subscriber The object subscribing to the given event type
	 * @param eventToSubscribeOn The event type the object wants to listen for
	 */
	public synchronized void unsubscribe(IEventListener subscriber, EventType eventToUnsubscribeOn) {
		toUnsubscribe.add(subscriber, eventToUnsubscribeOn);
	}
	
	/**
	 * Marks this subscriber to be removed from any and all event type's they
	 * are currently listening in on.
	 * 
	 * @param subscriber The object unsubscribing from all event types
	 */
	public synchronized void unsubscribeFromAll(IEventListener subscriber) {
		//iterate across subscriber map to determine which events the given subscriber listens in on
		var it = subscriberMap.entrySet().iterator();
		while (it.hasNext()) {
			var eventList = it.next();
			var eventType = eventList.getKey();
			var subscribers = eventList.getValue();
			//if the given subscriber exists on the current event's
			//subscriber list -- schedule for removal
			if (subscribers.contains(subscriber)) {
				toUnsubscribe.add(subscriber, eventType);
			}
		}
	}
	
	/**
	 * Returns the list of active subscribers for the given event type.
	 * <p>
	 * Note this does not account for subscribers who are currently scheduled
	 * to be unsubscribed.
	 * 
	 * @param type The event type
	 * @return The list of subscribers listening in on the given event type.
	 */
	public EArrayList<IEventListener> getSubscriberList(EventType type) {
		return subscriberMap.getOrDefault(type, new EArrayList<IEventListener>());
	}
	
	/**
	 * Returns this EventHandler's entire subscriber map.
	 */
	public HashMap<EventType, EArrayList<IEventListener>> getSubscriberMap() {
		return subscriberMap;
	}
	
	//--------------------------
	// Internal Handler Methods
	//--------------------------
	
	private void addSubscribers() {
		for (var toSub : toSubscribe) {
			var sub = toSub.getA();
			var event = toSub.getB();
			
			//grab current subscriber list for the given event type
			var subList = subscriberMap.get(event);
			//if there are not any subscribers currently -- create new list and add subscriber to it
			if (subList == null) {
				subList = new EArrayList<IEventListener>();
				subList.add(sub);
				subscriberMap.put(event, subList);
			}
			//otherwise, add the subscriber to the existing list if they aren't already in it
			else {
				subList.addIfNotContains(sub);
			}
		}
		
		toSubscribe.clear();
	}
	
	private void removeSubscribers() {
		for (var toUnsub : toUnsubscribe) {
			var sub = toUnsub.getA();
			var event = toUnsub.getB();
			
			var subList = subscriberMap.get(event);
			
			if (subList == null) return;
			else subList.remove(sub);
		}
		
		toUnsubscribe.clear();
	}
	
}
