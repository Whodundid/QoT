package game.quests;

import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;

public class Quest {
	
	private String name;
	private String description;
	private QuestDifficulty difficulty;
	private boolean completed = false;
	private boolean ignoreGoals = false;
	private boolean canBeCompleted = false;
	private boolean failed = false;
	private EList<QuestGoal> goals = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public Quest(String nameIn, String descriptionIn) { this(nameIn, descriptionIn, QuestDifficulty.EASY); }
	public Quest(String nameIn, String descriptionIn, QuestDifficulty difficultyIn) {
		name = nameIn;
		description = descriptionIn;
		difficulty = difficultyIn;
	}
	
	//---------
	// Methods
	//---------
	
	public Quest addGoal(String goalName, String goalDescription) { return addGoal(new QuestGoal(goalName, goalDescription)); }
	public Quest addGoal(QuestGoal goalIn) { goals.add(goalIn); return this; }
	
	public Quest removeGoal(String goalName) { goals.removeFirst(g -> g.getName().equals(goalName)); return this; }
	public Quest removeGoal(QuestGoal goalIn) { goals.remove(goalIn); return this; }
	
	/** Clears all goals from this quest but does not alter this quest's overall completion state. */
	public Quest clearGoals() { goals.clear(); return this; }
	
	/** Checks through all of this quest's goals to see if any are incomplete.
	 *  If quest can actually be completed and if it has not already been failed, the quest is set to complete if all of the goals are complete.
	 *  If this quest is set to ignore goals, the only way to complete it is to manually force it so. */
	public void update() {
		if (!failed && canBeCompleted && !ignoreGoals) {
			boolean val = true;
			for (QuestGoal g : goals) {
				if (!g.isComplete()) { val = false; }
			}
			completed = val;
		}
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return name; }
	public String description() { return description; }
	public QuestDifficulty getDifficulty() { return difficulty; }
	public boolean isComplete() { return completed; }
	public boolean canBeCompleted() { return canBeCompleted; }
	public boolean isFailed() { return failed; }
	
	public boolean ignoresGoals() { return ignoreGoals; }
	public EList<QuestGoal> getGoals() { return goals; }
	public QuestGoal getGoal(int num) { return (goals.isNotEmpty()) ? goals.get(NumberUtil.clamp(num, 0, goals.size() - 1)) : null; }
	
	//---------
	// Setters
	//---------
	
	public Quest setName(String nameIn) { name = nameIn; return this; }
	public Quest setDescription(String descriptionIn) { description = descriptionIn; return this; }
	public Quest setDifficulty(QuestDifficulty difficultyIn) { difficulty = difficultyIn; return this; }
	public Quest setComplete(boolean val) { completed = (val && canBeCompleted); return this; }
	public Quest setCanBeCompleted(boolean val) { canBeCompleted = val; return this; }
	public Quest setIgnoreGoals(boolean val) { ignoreGoals = val; return this; }
	public Quest setFailed(boolean val) { failed = val; return this; }
	
}
