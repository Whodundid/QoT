package envisionEngine.eWindow.windowObjects.windows;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowTextField;
import envisionEngine.eWindow.windowObjects.advancedObjects.textArea.WindowTextArea;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import util.renderUtil.EColors;

public class CalculatorWindow extends WindowParent {

	WindowTextField outputField;
	WindowButton sin, cos, tan, backspace, clear;
	WindowButton sqrt, square, pow, pi, divide;
	WindowButton log, seven, eight, nine, multiply;
	WindowButton ln, four, five, six, subtract;
	WindowButton e2x, one, two, three, plus;
	WindowButton second, zero, decimal, negate, enter;
	WindowTextArea history;
	
	String val = null;
	String lastVal = null;
	boolean historyDrawn = false;
	boolean toBeCleared = false;
	boolean is2nd = false;
	boolean isInOpearation = false;
	boolean funcSelected = false;
	boolean empty = true;
	Function currentFunction = Function.none;
	Function lastFunction = Function.none;
	Function reference = null;
	
	String input = "";
	
	@Override
	public void initWindow() {
		setDimensions(456, 527);
		setResizeable(true);
		setMinDims(148, 100);
		setMaximizable(true);
		setObjectName("Calculator");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		outputField = new WindowTextField(this, startX + 6, startY + 5, width - 14, (height - 7) / 7);
		
		double bW = (width - 18) / 5.0;
		double bH = (endY - outputField.endY - 19) / 6.0;
		
		sin = new WindowButton(this, startX + 5, outputField.endY + 4, bW, bH, "SIN");
		cos = new WindowButton(this, sin.endX + 2, outputField.endY + 4, bW, bH, "COS");
		tan = new WindowButton(this, cos.endX + 2, outputField.endY + 4, bW, bH, "TAN");
		backspace = new WindowButton(this, tan.endX + 2, outputField.endY + 4, bW, bH, "<-");
		clear = new WindowButton(this, backspace.endX + 2, outputField.endY + 4, bW, bH, "CLR");
		
		sqrt = new WindowButton(this, startX + 5, sin.endY + 2, bW, bH, "SQRT");
		square = new WindowButton(this, sqrt.endX + 2, sin.endY + 2, bW, bH, "x^2");
		pow = new WindowButton(this, square.endX + 2, sin.endY + 2, bW, bH, "^");
		pi = new WindowButton(this, pow.endX + 2, sin.endY + 2, bW, bH, "PI");
		divide = new WindowButton(this, pi.endX + 2, sin.endY + 2, bW, bH, "/");
		
		log = new WindowButton(this, startX + 5, sqrt.endY + 2, bW, bH, "LOG");
		seven = new WindowButton(this, sqrt.endX + 2, sqrt.endY + 2, bW, bH, "7");
		eight = new WindowButton(this, square.endX + 2, sqrt.endY + 2, bW, bH, "8");
		nine = new WindowButton(this, pow.endX + 2, sqrt.endY + 2, bW, bH, "9");
		multiply = new WindowButton(this, pi.endX + 2, sqrt.endY + 2, bW, bH, "X");
		
		ln = new WindowButton(this, startX + 5, log.endY + 2, bW, bH, "LN");
		four = new WindowButton(this, sqrt.endX + 2, log.endY + 2, bW, bH, "4");
		five = new WindowButton(this, square.endX + 2, log.endY + 2, bW, bH, "5");
		six = new WindowButton(this, pow.endX + 2, log.endY + 2, bW, bH, "6");
		subtract = new WindowButton(this, pi.endX + 2, log.endY + 2, bW, bH, "-");
		
		e2x = new WindowButton(this, startX + 5, ln.endY + 2, bW, bH, "e^x");
		one = new WindowButton(this, sqrt.endX + 2, ln.endY + 2, bW, bH, "1");
		two = new WindowButton(this, square.endX + 2, ln.endY + 2, bW, bH, "2");
		three = new WindowButton(this, pow.endX + 2, ln.endY + 2, bW, bH, "3");
		plus = new WindowButton(this, pi.endX + 2, ln.endY + 2, bW, bH, "+");
		
		second = new WindowButton(this, startX + 5, e2x.endY + 2, bW, bH, "2nd");
		zero = new WindowButton(this, sqrt.endX + 2, e2x.endY + 2, bW, bH, "0");
		decimal = new WindowButton(this, square.endX + 2, e2x.endY + 2, bW, bH, ".");
		negate = new WindowButton(this, pow.endX + 2, e2x.endY + 2, bW, bH, "(-)");
		enter = new WindowButton(this, pi.endX + 2, e2x.endY + 2, bW, bH, "=");
		
		history = new WindowTextArea(this, 0, 0, 0, 0);
		
		//disable textures
		WindowButton.setDrawTextures(false, sin, cos, tan, backspace, clear);
		WindowButton.setDrawTextures(false, sqrt, square, pow, pi, divide);
		WindowButton.setDrawTextures(false, log, seven, eight, nine, multiply);
		WindowButton.setDrawTextures(false, ln, four, five, six, subtract);
		WindowButton.setDrawTextures(false, e2x, one, two, three, plus);
		WindowButton.setDrawTextures(false, second, zero, decimal, negate, enter);
		
		//enable backgrounds
		WindowButton.setDrawBackground(true, sin, cos, tan, backspace, clear);
		WindowButton.setDrawBackground(true, sqrt, square, pow, pi, divide);
		WindowButton.setDrawBackground(true, log, seven, eight, nine, multiply);
		WindowButton.setDrawBackground(true, ln, four, five, six, subtract);
		WindowButton.setDrawBackground(true, e2x, one, two, three, plus);
		WindowButton.setDrawBackground(true, second, zero, decimal, negate, enter);
		
		//enable background hover
		WindowButton.setDrawBackgroundHover(true, sin, cos, tan, backspace, clear);
		WindowButton.setDrawBackgroundHover(true, sqrt, square, pow, pi, divide);
		WindowButton.setDrawBackgroundHover(true, log, seven, eight, nine, multiply);
		WindowButton.setDrawBackgroundHover(true, ln, four, five, six, subtract);
		WindowButton.setDrawBackgroundHover(true, e2x, one, two, three, plus);
		WindowButton.setDrawBackgroundHover(true, second, zero, decimal, negate, enter);
		
		//steel
		int c = EColors.steel.intVal;
		WindowButton.setBackgroundColor(c, sin, cos, tan, backspace, clear);
		WindowButton.setBackgroundColor(c, sqrt, square, pow, pi, divide);
		WindowButton.setBackgroundColor(c, log, multiply);
		WindowButton.setBackgroundColor(c, ln, subtract);
		WindowButton.setBackgroundColor(c, e2x, plus);
		WindowButton.setBackgroundColor(c, second);
		
		//very dark gray
		WindowButton.setBackgroundColor(EColors.vdgray, one, two, three, four, five, six, seven, eight, nine, zero, decimal, negate);
		
		WindowButton.setStringColor(EColors.vdgray, enter);
		
		//dark green
		WindowButton.setBackgroundColor(EColors.mgray, enter);
		WindowButton.setBackgroundHoverColor(EColors.gray, enter);
		
		outputField.setEditable(false);
		outputField.setMaxStringLength(40);
		outputField.setTextColor(EColors.lgray);
		outputField.setText("0");
		
		sin.setString(is2nd ? "ASIN" : "SIN");
		cos.setString(is2nd ? "ACOS" : "COS");
		tan.setString(is2nd ? "ATAN" : "TAN");
		ln.setString(is2nd ? "e" : "LN");
		
		WindowButton.setStringColor(is2nd ? EColors.yellow.intVal : 14737632, sin, cos, tan, ln);
		
		addObject(outputField);
		addObject(sin, cos, tan, backspace, clear);
		addObject(sqrt, square, pow, pi, divide);
		addObject(log, seven, eight, nine, multiply);
		addObject(ln, four, five, six, subtract);
		addObject(e2x, one, two, three, plus);
		addObject(second, zero, decimal, negate, enter);
	}
	
	@Override
	public void preReInit() {
		input = outputField.getText();
	}
	
	@Override
	public void postReInit() {
		outputField.setText(input);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		//drawRect(0xffe4f4fc, 1);
		
		if (currentFunction != Function.none) {
			switch (currentFunction) {
			case add: plus.drawRect(EColors.lred, -1); break;
			case sub: subtract.drawRect(EColors.lred, -1); break;
			case mul: multiply.drawRect(EColors.lred, -1); break;
			case div: divide.drawRect(EColors.lred, -1); break;
			case pow: pow.drawRect(EColors.lred, -1); break;
			default: break;
			}
		}
		
		if (is2nd) {
			second.drawRect(EColors.yellow, -1);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
		
		try {
			if (keyCode == 28) { evaluate(); } //enter
			if (keyCode == 14) { backspace(); } //backspace
			if (Character.isDigit(typedChar)) { write("" + typedChar); }
			
			switch (typedChar) {
			case '+': setVal(Function.add); break;
			case '-': setVal(Function.sub); break;
			case '*': setVal(Function.mul); break;
			case '/': setVal(Function.div); break;
			case '^': setVal(Function.pow); break;
			case '.': write("."); break;
			default: break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			outputField.setText("error");
			toBeCleared = true;
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		try {
			if (object == zero) { write("0"); }
			if (object == one) { write("1"); }
			if (object == two) { write("2"); }
			if (object == three) { write("3"); }
			if (object == four) { write("4"); }
			if (object == five) { write("5"); }
			if (object == six) { write("6"); }
			if (object == seven) { write("7"); }
			if (object == eight) { write("8"); }
			if (object == nine) { write("9"); }
			if (object == decimal) { write("."); }
			
			if (object == second) { toggleSecond(); }
			
			if (object == plus) { setVal(Function.add); }
			if (object == subtract) { setVal(Function.sub); }
			if (object == multiply) { setVal(Function.mul); }
			if (object == divide) { setVal(Function.div); }
			
			if (object == negate) { write("NEGATE"); }
			if (object == backspace) { backspace(); }
			if (object == clear) { clear(); }
			if (object == enter) { evaluate(); }
			
			if (object == sqrt) { sqrt(); }
			if (object == square) { square(); }
			if (object == pow) { setVal(Function.pow); }
			if (object == pi) { write(String.valueOf(Math.PI)); }
			if (object == log) { log(); }
			if (object == ln) { ln(); }
			if (object == e2x) { e2x(); }
			if (object == sin) { sin(); }
			if (object == cos) { cos(); }
			if (object == tan) { tan(); }
		}
		catch (Exception e) {
			e.printStackTrace();
			outputField.setText("error");
			toBeCleared = true;
		}
	}
	
	private void write(String in) {
		if (toBeCleared) { outputField.clear(); toBeCleared = false; }
		
		boolean add = true;
		
		if (in.equals(".") && empty) { outputField.text = "0"; empty = false; }
		if (in.equals("NEGATE")) { outputField.text = "-" + outputField.text; add = false; }
		
		if (add) {
			if (empty) {
				outputField.text = in;
				empty = false;
			}
			else { outputField.text += in; }
		}
		
		if (funcSelected) { lastVal = outputField.text; funcSelected = false; }
	}
	
	private void toggleSecond() {
		is2nd = !is2nd;
		
		sin.setString(is2nd ? "ASIN" : "SIN");
		cos.setString(is2nd ? "ACOS" : "COS");
		tan.setString(is2nd ? "ATAN" : "TAN");
		ln.setString(is2nd ? "e" : "LN");
		
		WindowButton.setStringColor(is2nd ? EColors.yellow.intVal : 14737632, sin, cos, tan, ln);
	}
	
	private void setVal(Function in) throws Exception {
		currentFunction = in;
		funcSelected = true;
		val = outputField.text;
		toBeCleared = true;
		outputField.setTextColor(EColors.yellow.intVal);
	}
	
	private void clear() {
		outputField.text = "0";
		val = null;
		empty = true;
		currentFunction = Function.none;
		lastFunction = Function.none;
		reference = null;
		outputField.setTextColor(EColors.lgray.intVal);
	}
	
	private void backspace() {
		if (outputField.isNotEmpty()) {
			
			if (outputField.text.toLowerCase().equals("infinity") || outputField.text.toLowerCase().equals("nan")) { clear(); }
			else {
				if (!toBeCleared) { toBeCleared = false; }
				outputField.text = outputField.text.substring(0, outputField.text.length() - 1);
				
				if (outputField.isEmpty()) {
					outputField.text = "0";
					empty = true;
				}
			}
			
		}
	}
	
	private void sin() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			
			if (is2nd) { outputField.text = format(Math.asin(parsed)); }
			else { outputField.text = format(Math.sin(parsed)); }
		}
	}
	
	private void cos() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			
			if (is2nd) { outputField.text = format(Math.acos(parsed)); }
			else { outputField.text = format(Math.cos(parsed)); }
		}
	}
	
	private void tan() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			
			if (is2nd) { outputField.text = format(Math.atan(parsed)); }
			else { outputField.text = format(Math.tan(parsed)); }
		}
	}
	
	private void sqrt() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			
			outputField.text = format(Math.sqrt(parsed));
		}
	}
	
	private void square() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			outputField.text = format(Math.pow(parsed, 2));
		}
	}
	
	private void log() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			outputField.text = format(Math.log10(parsed));
		}
	}
	
	private void ln() throws Exception {
		if (outputField.isNotEmpty()) {
			if (is2nd) {
				write(format(Math.E));
			}
			else {
				Double parsed = parseExpression(outputField.text);
				outputField.text = format(Math.log(parsed));
			}
		}
	}
	
	private void e2x() throws Exception {
		if (outputField.isNotEmpty()) {
			Double parsed = parseExpression(outputField.text);
			outputField.text = format(Math.pow(Math.E, parsed));
		}
	}
	
	private void evaluate() throws Exception {
		Double parsed = null;
		Double parsedVal = null;
		
		if (!outputField.text.equals("error")) {
			if (currentFunction != Function.none) { lastFunction = currentFunction; }
			
			if (currentFunction != Function.none && val != null) {
				parsed = parseExpression(outputField.text);
				parsedVal = parseExpression(val);
				
				switch (currentFunction) {
				case add: outputField.text = format(parsedVal + parsed); break;
				case sub: outputField.text = format(parsedVal - parsed); break;
				case mul: outputField.text = format(parsedVal * parsed); break;
				case div:
					if (parsed == 0) { outputField.text = "error"; toBeCleared = true; }
					else { outputField.text = format(parsedVal / parsed); }
					break;
				case pow:
					if (parsedVal == 0.0 && parsed == 0.0) { outputField.text = "error"; toBeCleared = true; }
					else { outputField.text = format(Math.pow(parsedVal, parsed)); }
					break;
				default: break;
				}
			}
			else if (lastFunction != Function.none && lastVal != null) {
				parsed = parseExpression(outputField.text);
				parsedVal = parseExpression(lastVal);
				
				switch (lastFunction) {
				case add: outputField.text = format(parsedVal + parsed); break;
				case sub: outputField.text = format(parsed - parsedVal); break; //switches for repeated
				case mul: outputField.text = format(parsedVal * parsed); break;
				case div:
					if (parsed == 0) { outputField.text = "error"; }
					else { outputField.text = format(parsed / parsedVal); } //switches for repeated
					break;
				case pow:
					if (parsedVal == 0 && parsed == 0) { outputField.text = "error"; toBeCleared = true; }
					else { outputField.text = format(Math.pow(parsedVal, parsed)); }
					break;
				default: break;
				}
			}
		}
		
		val = null;
		toBeCleared = true;
		currentFunction = Function.none;
		reference = lastFunction;
		
		outputField.setTextColor(EColors.lgray.intVal);
	}
	
	private void parseFunction(Function in, Double parsed, Double parsedVal) {
		switch (in) {
		case add: outputField.text = format(parsedVal + parsed); break;
		case sub: outputField.text = format(parsedVal - parsed); break;
		case mul: outputField.text = format(parsedVal * parsed); break;
		case div:
			if (parsed == 0) { outputField.text = "error"; }
			else { outputField.text = format(parsedVal / parsed); }
			break;
		case pow: outputField.text = format(Math.pow(parsedVal, parsed)); break;
		default: break;
		}
	}
	
	private Double parseExpression(String in) {
		if (in != null && !in.isEmpty()) {
			try {
				Double testVal = Double.parseDouble(in);
				return testVal;
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return null;
	}
	
	private String format(double valIn) {
		String sVal = String.valueOf(valIn);
		String theVal = "";
		
		if (sVal.contains(".")) {
			for (int i = sVal.length() - 1; i >= 0; i--) {
				char c = sVal.charAt(i);
				if (c == '.') { theVal = sVal.substring(0, i); break; }
				if (c != '0') {
					int pos = (i + 1 <= sVal.length()) ? i + 1 : i;
					theVal = sVal.substring(0, pos);
					break;
				}
			}
		}
		else { theVal = sVal; }
		
		return theVal;
	}
	
	private enum Function { add, sub, mul, div, pow, none; }
	
}
