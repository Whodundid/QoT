package engine.settings.config;

import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;

import java.util.List;

//Author: Hunter Bragg

/** Allows comment blocks to be inserted within config data. */
public class CommentConfigBlock extends ConfigBlock {
	
	public CommentConfigBlock() { super(); }
	public CommentConfigBlock(BoxList<String, List<String>> elementsIn) { super(elementsIn); }
	
	public CommentConfigBlock(String... comments) {
		super();
		addLine(comments);
	}
	
	public CommentConfigBlock addLine(String... comments) {
		for (String s : comments) { addLine(s); }
		return this;
	}
	
	public CommentConfigBlock addLine(String commentIn) {
		blockContents.add("**", new EArrayList<String>(commentIn));
		return this;
	}
	
}
