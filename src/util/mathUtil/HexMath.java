package util.mathUtil;

import java.awt.Color;
import java.util.HashMap;

//Author: Hunter Bragg

public class HexMath {
	
	
	static HashMap<String, String> hexVals = new HashMap();
	
	static {
		hexVals.put("0", "0000");
		hexVals.put("1", "0001");
		hexVals.put("2", "0010");
		hexVals.put("3", "0011");
		hexVals.put("4", "0100");
		hexVals.put("5", "0101");
		hexVals.put("6", "0110");
		hexVals.put("7", "0111");
		hexVals.put("8", "1000");
		hexVals.put("9", "1001");
		hexVals.put("a", "1010");
		hexVals.put("b", "1011");
		hexVals.put("c", "1100");
		hexVals.put("d", "1101");
		hexVals.put("e", "1110");
		hexVals.put("f", "1111");
	}
	
	public static String getHexChar(int in) {
		if (in >= 0) {
			if (in <= 9) { return String.valueOf(in); }
			else if (in <= 15) {
				switch (in) {
				case 10: return "a";
				case 11: return "b";
				case 12: return "c";
				case 13: return "d";
				case 14: return "e";
				case 15: return "f";
				}
			}
		}
		return null;
	}
	
	public static String hexToBinary(String in) {
		String binary = "";
		
		for (int i = 0; i < in.length(); i++) {
			char c = Character.toLowerCase(in.charAt(i));
			binary += hexVals.get(new String() + c);
		}
		
		return binary;
	}
	
	public static String binaryToHex(String in) {
		String hex = "0x";
		
		int i = 0;
		int x = 0;
		String cur = "";
		
		while (i < in.length()) {
			x++;
			
			cur += in.charAt(i);
			
			if (x % 4 == 0 || (i == in.length() - 1)) {
				hex += getHexChar(Integer.parseInt(cur, 2));
				x = 0;
				cur = "";
			}
			
			i++;
		}
		
		return hex;
	}
	
	public static String twosComplement(String in) {
		int n = in.length();
		int i;
		
		String ones = "", twos = "";
		ones = twos = "";
		
		for (i = 0; i < n; i++) {
			ones += (in.charAt(i) == '0') ? '1' : '0';
		}
		
		twos = ones;
		
		for (i = n - 1; i >= 0; i--) {
			if (ones.charAt(i) == '1') {
				twos = twos.substring(0, i) + '0' + twos.substring(i + 1);
			}
			else {
				twos = twos.substring(0, i) + '1' + twos.substring(i + 1);
				break; 
			}
		}
		if (i == -1) { twos = '1' + twos; }
		
		return twos;
	}
	
	public static int getTwosCompValue(String in) {
		if (in != null && !in.isEmpty()) {
			boolean neg = in.startsWith("1");
			
			int val = (neg) ? Integer.parseInt(twosComplement(in), 2) : Integer.parseInt(in, 2);
			
			return (neg) ? val * -1 : val;
		}
		return 0;
	}
	
	public static int RGBtoHEX(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            if (hex.length() == 5) { hex = "0" + hex; }
            if (hex.length() == 4) { hex = "00" + hex; }
            if (hex.length() == 3) { hex = "000" + hex; }
        }
        hex = "#" + hex;
        return Integer.decode(hex);
    }
	
	/** converts a floating point value between 0.0 and 1.0 to a corresponding hexadecimal value. */
	public static int floatToHex(float valIn) {
		int val = (int) (valIn * 255);
		String sVal = Integer.toHexString(val);
		return val;
	}
	
}
