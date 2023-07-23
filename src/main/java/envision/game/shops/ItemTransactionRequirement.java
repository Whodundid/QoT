package envision.game.shops;

/**
 * These can be combined on an item in a shop as a means for how the item
 * can be bought.
 * <p>
 * For example, if an item being sold has the values: [GOLD, HAS_COMPLETED_QUESTS],
 * this means that the item can only be bought if the player (or buying entity) has
 * A. at least the given gold amount __ AND __ B. has completed the given quest(s).
 * 
 * @author Hunter Bragg
 */
public enum ItemTransactionRequirement {
	/** Item can be bought with gold. */
	GOLD_AMOUNT,
	/** Item can be traded for with specified item(s). */
	TRADE_FOR_ITEM,
	/** Item can only be bought if buying entity has completed the specified quest(s). */
	HAS_COMPLETED_QUEST,
	/** Item can only be bought if buying entity has completed the specified quest objective(s). */
	HAS_COMPLETED_QUEST_OBJECTIVE,
	/** Item can only be bought if some variable [global/script] requirement has been met. */
	VARIABLE_CHECK,
}
