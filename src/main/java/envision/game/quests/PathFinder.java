package envision.game.quests;

import envision.engine.loader.built.game.Quest;

public class PathFinder {
    
    private Quest quest;
    private boolean isComplete;
    
    public PathFinder(Quest questIn) {
        quest = questIn;
    }
    
    public Quest getCurrentQuest() {
        return quest;
    }
    
    public void setCurrentQuest(Quest questIn) { 
        quest = questIn;
    }
    
}
