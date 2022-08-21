package envision.game.scripts.scriptBuilder.conditions;

public enum ConditionType {
	//general conditions
	EQUALS,
	DOES_NOT_EQUAL,
	IS_AT_LEAST,
	IS_AT_MOST,
	CONTAINS,
	DOES_NOT_CONTAIN,
	IS_EMPTY,
	IS_NOT_EMPTY,
	
	//entity specifc
	IS_ALIVE,
	IS_DEAD,
	IS_BEING_SPAWNED,
	IS_DYING,
	IS_MOVING,
	IS_FLYING,
	IS_NOCLIPPING,
	IS_INVINCIBLE,
	IS_IN_WATER,
	IS_ON_LAND,
	HAS_MANA,
	IS_HOLDING_ITEM,
	CAN_MOVE,
	CANT_MOVE,
	IS_ENEMY,
	IS_FRIEND,
	IS_NEUTRAL,
	IS_ATTACKING,
	
	//world specific
	IS_LOADED,
	IS_UNLOADED,
	IS_PAUSED,
	IS_DAY,
	IS_NIGHT,
	
}
