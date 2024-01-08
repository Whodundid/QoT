package envision.engine.creation.block;

import eutil.datatypes.util.EList;

public abstract class CreatorBlock {
    
    //========
    // Fields
    //========
    
    protected String name;
    protected EList<CreatorBlock> components = EList.newList();
    protected CreatorBlockValue value = new CreatorBlockValue();
    
    //==============
    // Constructors
    //==============
    
    public CreatorBlock() {}
    
    public CreatorBlock(String blockNameIn) {
        name = blockNameIn;
    }
    
    //===========
    // Abstracts
    //===========
    
    public abstract void evaluate();
    
    //=========
    // Methods
    //=========
    
    public void addComponent(CreatorBlock block) {
        if (block == null) return;
        components.addIfNotContains(block);
    }
    
    public void removeComponent(CreatorBlock block) {
        if (block == null) return;
        components.remove(block);
    }
    
    //=========
    // Getters
    //=========
    
    public String getName() { return name; }
    public CreatorBlockValue getValue() { return value; }
    public EList<CreatorBlock> getComponents() { return components; }
    
    //=========
    // Setters
    //=========
    
    public void setName(String nameIn) { name = nameIn; }
    public void setValue(Object valueIn) { value.value = valueIn; }
    
}
