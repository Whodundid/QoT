package envision.engine.registry;

public class RegistryID {
    
    private int ID;
    private String valueName;
    
    public RegistryID(int idIn, String valueNameIn) {
        ID = idIn;
        valueName = valueNameIn;
    }
    
    public void updateEntry(int newID, String newValueName) {
        ID = newID;
        valueName = newValueName;
    }
    
}
