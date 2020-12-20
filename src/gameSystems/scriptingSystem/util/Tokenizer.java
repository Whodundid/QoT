package gameSystems.scriptingSystem.util;

import gameSystems.scriptingSystem.util.enums.Keyword;
import util.storageUtil.EArrayList;

public class Tokenizer {

	int prevLine = -1;
	boolean comment = false;
	boolean multi = false; //trying to think of how to handle still
	boolean func = false;
	
	/** Parses out individual statement arguments into tokens. */
	public EArrayList<String> tokenize(String line, int lineNum) {
		EArrayList<String> tokens = new EArrayList();
		
		if (lineNum != prevLine) { comment = false; }
		prevLine = lineNum;
		
		String cur = "";
		String test = "";
		boolean inStr = false; //flag to indicate if the current token is a string or not
		boolean keywordCheck = false; //flag to indicate if the tokenizer is currently parsing a potential keyword
		boolean append = true; //flag to indicate if the current character should be appended to the current token in progress
		boolean inNumber = false; //flag to indicate if the current token is a number
		boolean atStart = true; //flag to indicate if there was a space
		int commentStart = -1; //position marker to show where
		
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			append = true;
			
			//debug out
			//System.out.printf("'%s' [%-20s] [%-20s] InStr: %b\n", c, cur, test, inStr);
			
			//first check to see if the rest of the line should just be made into a comment
			if (comment) { break; }
			else {
				//first check to see if this is the start of a new token
				if (atStart) {
					
					atStart = false;
					if (Character.isDigit(c)) { inNumber = true; }
				}
				
				//next check if there are strings to deal with
				if (c == '"') {
					if (inStr) {
						inStr = false;
						tokens.addIf(!cur.isEmpty(), cur + c);
						cur = "";
						test = "";
						keywordCheck = false;
						inNumber = false;
						continue;
					}
					else {
						inStr = true;
						tokens.addIf(!cur.trim().isEmpty(), cur.trim());
						cur = "" + c;
						test = "";
						keywordCheck = false;
						inNumber = false;
						atStart = true;
						continue;
					}
				}
				
				//next check to see if the current character is the start of a keyword
				if (!func && !keywordCheck) {
					
					//keep going until the test string is no longer still a keyword
					if (!Keyword.isStillKeyword(test, c)) {
						
						//attempt to grab a keyword out of the current test string
						Keyword keyword = Keyword.getKeyword(test);
						if (keyword != null) {
							
							if (!inStr) {
								//get the cur string as it was before keyword checking began
								String curPre = cur.substring(0, cur.length() - test.length());
								
								//only add the pre cur as a token if it isn't empty
								if (!curPre.isEmpty()) { tokens.add(curPre); }
								
								//then add the found keyword as a token
								tokens.add(test);
								
								//test to see if the keyword was a comment
								if (keyword == Keyword.COMMENT) {
									comment = true;
									commentStart = i;
									break;
								}
								else if (keyword.isComment()) { multi = true; }
								
								//set the start of cur to the newest character found
								cur = "";
								test = "";
								keywordCheck = false;
								atStart = true;
							}

						}
						//attempt to find the start of a different keyword if it was found that the first keyword ended
						else if (Keyword.isKeywordStart(c)) {
							test = "" + c;
						}
						//otherwise, there was no keyword here, stop checking and continue as normal
						else {
							test = "";
							keywordCheck = false;
						}
					}
					//check to make sure that this isn't the end of the input line so that keywords in progress can be preserved
					else if ((i + 1 == line.length() && Keyword.isKeyword(test + c))) {
						test = test + c;
						
						Keyword keyword = Keyword.getKeyword(test);
						if (keyword != null) {
							
							//get the cur string as it was before keyword checking began
							String curPre = cur.substring(0, cur.length() - (test.length() - 1));
							
							//only add the pre cur as a token if it isn't empty
							if (!curPre.isEmpty()) { tokens.add(curPre); }
							
							//then add the found keyword as a token
							tokens.add(test);
							
							//test to see if the keyword was a comment
							if (keyword == Keyword.COMMENT) {
								comment = true;
								commentStart = i;
								break;
							}
							else if (keyword.isComment()) { multi = true; }
							
							cur = "";
							test = "";
							atStart = true;
							break;
						}
					}
					//continue to append the next character in the string to test
					else { test += c; }
				}
				//check to see if there could be the start of a keyword
				else if (Keyword.isKeywordStart(c)) {
					test = "" + c;
					keywordCheck = true;
				}
				
				//next check if there are any characters that can be removed and if it's not currently in a string
				if (!inStr && removeChar(c)) {
					tokens.addIf(!cur.trim().isEmpty(), cur.trim());
					cur = "";
					test = "";
					keywordCheck = false;
					inNumber = false;
					atStart = true;
					continue;
				}
				
				//isolate specific characters if it's not currently in a string
				if (!inStr && (isolateChar(c) || c == '.')) {
					if (c == '.') {
						if (inNumber) { cur += c; continue; }
						func = true;
					}
					else { func = false; }
					
					tokens.addIf(!cur.isEmpty(), cur);
					if (!func) { tokens.add("" + c); }
					cur = "";
					test = "";
					keywordCheck = false;
					inNumber = false;
					atStart = true;
					continue;
				}
				
			} //else -- is comment
			
			//append the next read char onto cur
			if (append) { cur += c; }
			
		} //main for loop end
		
		if (comment && commentStart > 0) {
			tokens.add(line.substring(commentStart).trim());
		}
		else {
			tokens.addIf(!cur.trim().isEmpty(), cur.trim());
		}
		
		//remove the ';' from the list of parsed tokens
		if (";".equals(tokens.getLast())) { tokens.removeLast(); }
		
		//System.out.println("THE TOKENS: " + tokens);
		
		return tokens;
	}
	
	public static boolean isolateChar(char c) {
		switch (c) {
		case '\'': case ';': case ',': case '{': case '}': case '[': case ']': case '(': case ')': return true;
		default: return false;
		}
	}
	
	public static boolean removeChar(char c) {
		switch (c) {
		case ' ': return true;
		default: return false;
		}
	}

}
