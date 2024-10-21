package envision.engine.creation.history;

import envision.engine.creation.BlockWorkingArea;
import envision.engine.creation.block.CreatorBlock;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;

public class EditAction_AddBlock extends BaseEditAction<CreatorBlock, BlockWorkingArea> {
    
    //========
    // Fields
    //========
    
    private BoxList<Integer, Integer> blockPositions;
    
    //==============
    // Constructors
    //==============
    
    public EditAction_AddBlock(BlockWorkingArea target, EList<CreatorBlock> items, BoxList<Integer, Integer> blockPositionsIn) {
        super("Add Block" + (items.hasOne() ? "" : "s"), target, items);
        
        blockPositions = new BoxList<>(blockPositionsIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void doAction() {
        BlockWorkingArea target = getTarget();
        EList<CreatorBlock> items = getItems();
        
        for (int i = 0; i < items.size(); i++) {
            CreatorBlock block = items.get(i);
            var pos = blockPositions.get(i);
            int x = pos.getA();
            int y = pos.getB();
            target.addBlock(block, x, y);
        }
    }

    @Override
    public void undoAction() {
        BlockWorkingArea target = getTarget();
        EList<CreatorBlock> items = getItems();
        
        target.removeBlocks(items);
    }
    
}
