package engine.windowLib.windowUtil;

import java.util.concurrent.ConcurrentHashMap;

import eutil.datatypes.EArrayList;

/**
 * Keeps track of a series of tasks that will be executed once the
 * specified 'FutureTaskEventType' is run.
 * <p>
 * This tool provides an interface for being able to selectively
 * specify additional tasks to be executed on specific objects once
 * they reach certain stages of their life-cycle.
 * <p>
 * The following states that can be hooked on are as follows:
 * <ul>
 * 		<li> 
 * </ul>
 * 
 * @author Hunter Bragg
 */
public class FutureTaskManager {
	
	//--------
	// Fields
	//--------
	
	private final ConcurrentHashMap<FutureTaskEventType, EArrayList<Runnable>> futureTasks = new ConcurrentHashMap<>();

	//---------
	// Methods
	//---------
	
	/**
	 * Adds a task to be executed once the specified future task event
	 * type event is run.
	 * 
	 * @param type The type of future tasks to add to
	 * @param task The task to be eventually performed
	 */
	public void addFutureTask(FutureTaskEventType type, Runnable task) {
		var tasks = futureTasks.get(type);
		if (tasks == null) tasks = new EArrayList<Runnable>();
		tasks.put(task);
	}
	
	/**
	 * Runs all of the future tasks associated with the given
	 * FutureTaskEventType.
	 * 
	 * @param type The type of future tasks to be executed
	 */
	public void runTaskType(FutureTaskEventType type) {
		try {
			//check if there are even tasks to run
			var tasks = futureTasks.get(type);
			if (tasks == null) return;
			
			//run each task for the given type
			for (var r : tasks) {
				r.run();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes all future tasks that were scheduled to run for the given
	 * future task event type.
	 * 
	 * @param type The type of future tasks to clear out
	 */
	public void clearTasks(FutureTaskEventType type) {
		var tasks = futureTasks.get(type);
		if (tasks != null) tasks.clear();
	}
	
	/**
	 * Returns all of the tasks that are scheduled to be run once the
	 * given future task event type is fired.
	 * 
	 * @param type The type of future tasks to be executed
	 * @return A list of all tasks that will be run for the given type
	 */
	public EArrayList<Runnable> getFutureTasks(FutureTaskEventType type) {
		return futureTasks.getOrDefault(type, new EArrayList<>());
	}
	
}