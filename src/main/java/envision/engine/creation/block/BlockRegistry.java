package envision.engine.creation.block;

import envision.engine.creation.block.blockTypes.DisplayBlock;
import envision.engine.creation.block.blockTypes.EnvisionCodeBlock;
import envision.engine.creation.block.blockTypes.coding.ArrayGrouper;
import envision.engine.creation.block.blockTypes.coding.ArrayIndexSelector;
import envision.engine.creation.block.blockTypes.constants.ConstBooleanBlock;
import envision.engine.creation.block.blockTypes.constants.ConstNumberBlock;
import envision.engine.creation.block.blockTypes.constants.ConstStringBlock;
import envision.engine.creation.block.blockTypes.logic.AndBlock;
import envision.engine.creation.block.blockTypes.logic.BufferBlock;
import envision.engine.creation.block.blockTypes.logic.EqualityBlock;
import envision.engine.creation.block.blockTypes.logic.InverterBlock;
import envision.engine.creation.block.blockTypes.logic.OrBlock;
import envision.engine.creation.block.blockTypes.logic.XorBlock;
import envision.engine.creation.block.blockTypes.texture.TextureInputBlock;
import envision.engine.creation.block.blockTypes.texture.TextureSplitterBlock;
import eutil.datatypes.util.EList;

public class BlockRegistry {
    
    //========
    // Fields
    //========
    
    private static final EList<Class<? extends CreatorBlock>> blockList = EList.newList();
    
    //==============
    // Constructors
    //==============
    
    private BlockRegistry() {}
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    static {
        blockList.add(BufferBlock.class);
        blockList.add(ConstBooleanBlock.class);
        blockList.add(ConstNumberBlock.class);
        blockList.add(ConstStringBlock.class);
        blockList.add(EqualityBlock.class);
        blockList.add(DisplayBlock.class);
        blockList.add(InverterBlock.class);
        blockList.add(AndBlock.class);
        blockList.add(OrBlock.class);
        blockList.add(XorBlock.class);
        blockList.add(EnvisionCodeBlock.class);
        blockList.add(TextureInputBlock.class);
        blockList.add(TextureSplitterBlock.class);
        blockList.add(ArrayIndexSelector.class);
        blockList.add(ArrayGrouper.class);
    }
    
    public static EList<Class<? extends CreatorBlock>> getBlockTypeList() {
        return blockList.toUnmodifiableList();
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public static BufferBlock buffer() { return new BufferBlock(); }
    public static ConstBooleanBlock bool() { return new ConstBooleanBlock(); }
    public static ConstNumberBlock number() { return new ConstNumberBlock(); }
    public static ConstStringBlock string() { return new ConstStringBlock(); }
    public static EqualityBlock equality() { return new EqualityBlock(); }
    public static DisplayBlock display() { return new DisplayBlock(); }
    public static InverterBlock inverter() { return new InverterBlock(); }
    public static AndBlock and() { return new AndBlock(); }
    public static OrBlock or() { return new OrBlock(); }
    public static XorBlock xor() { return new XorBlock(); }
    public static EnvisionCodeBlock code() { return new EnvisionCodeBlock(); }
    public static TextureInputBlock textureInput() { return new TextureInputBlock(); }
    public static TextureSplitterBlock textureSplitter() { return new TextureSplitterBlock(); }
    public static ArrayIndexSelector arrayIndex() { return new ArrayIndexSelector(); }
    public static ArrayGrouper arrayGrouper() { return new ArrayGrouper(); }
    
}
