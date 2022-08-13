package main.settings.config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eutil.EUtil;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.file.LineReader;

public class QotConfigFile {
	
	protected File configPath;
	protected String configName = "";
	protected String configTitleLine = null;
	protected BoxList<String, EArrayList<String>> configValues;
	
	/** temp list used for saving */
	private EArrayList<ConfigBlock> config = new EArrayList();
	
	public QotConfigFile(File path, String name) { this(path, name, null); }
	public QotConfigFile(File path, String name, String configTitleIn) {
		configPath = path;
		configName = name;
		configTitleLine = configTitleIn;
		configValues = new BoxList<String, EArrayList<String>>();
	}
	
	public boolean saveConfig() { return trySave(); }
	public boolean loadConfig() { return tryLoad(); }
	public boolean resetConfig() { return tryReset(); }
	
	/** Attempts to parse through a potentially existing config file and match identifier-value pairs.
	 *  Returns false if the file cannot be found. */
	public BoxList getConfigContents() {
		if (configValues == null) { configValues = new BoxList<String, EArrayList<String>>(); }
		configValues.clear();
		parseFile();
		return configValues;
	}
	
	public boolean load() { return getConfigContents().size() > 0; }
	
	/** Attempts to create an identifier-value pair based on a given keyWord and a specific Class type to parse the value as. */
	protected <Val extends Object> Val getConfigVal(String keyWord, Class<Val> asType) throws Exception { return getConfigVal(keyWord, asType, null); }
	
	/** Attempts to create an identifier-value pair based on a given keyWord and a specific Class type to parse the value as with a
	 *  default value to fall back on in case the parsing fails. */
	protected <Val extends Object> Val getConfigVal(String keyWord, Class<Val> asType, Val defaultVal) throws Exception {
		BoxList holder = configValues;
		Box2<String, EArrayList<String>> box = holder.getBoxWithA(keyWord);
		if (box != null) {
			String sVal = box.getB().get(0);
			Val returnVal = null;
			
			try {
				if (asType.isAssignableFrom(Boolean.class)) 	returnVal = asType.cast(Boolean.parseBoolean(sVal));
				if (asType.isAssignableFrom(Byte.class)) 		returnVal = asType.cast(Byte.parseByte(sVal));
				if (asType.isAssignableFrom(Short.class)) 		returnVal = asType.cast(Short.parseShort(sVal));
				if (asType.isAssignableFrom(Integer.class)) 	returnVal = asType.cast(Integer.parseInt(sVal));
				if (asType.isAssignableFrom(Long.class)) 		returnVal = asType.cast(Long.parseLong(sVal));
				if (asType.isAssignableFrom(Float.class)) 		returnVal = asType.cast(Float.parseFloat(sVal));
				if (asType.isAssignableFrom(Double.class)) 		returnVal = asType.cast(Double.parseDouble(sVal));
				if (asType.isAssignableFrom(String.class)) 		returnVal = asType.cast(sVal);
			}
			catch (Exception e) {
				e.printStackTrace();
				return defaultVal; //fallback to defaultVal since parse failed
			}
			
			if (returnVal == null && defaultVal != null) returnVal = defaultVal;
			
			return returnVal;
		}
		return defaultVal;
	}
	
	protected <Val> void setConfigVal(String keyWord, ConfigSetting<Val> settingIn) { setConfigVal(keyWord, settingIn, settingIn.getDefault()); }
	protected <Val> void setConfigVal(String keyWord, ConfigSetting<Val> settingIn, Val defaultVal) {
		if (settingIn != null) {
			Val get = null;
			try {
				get = getConfigVal(keyWord, settingIn.getClassType(), defaultVal);
				
				settingIn.set(get);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if (get == null && defaultVal != null) settingIn.set(defaultVal);
		}
	}
	
	protected <Val> void setConfigVal(ConfigSetting<Val> settingIn) { setConfigVal(settingIn, settingIn.getDefault()); }
	protected <Val> void setConfigVal(ConfigSetting<Val> settingIn, Val defaultVal) {
		if (settingIn != null) {
			Val get = null;
			try {
				get = getConfigVal(settingIn.getDescription() + ":", settingIn.getClassType(), defaultVal);
				
				settingIn.set(get);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if (get == null && defaultVal != null) settingIn.set(defaultVal);
		}
	}
	
	private void parseFile() {
		if (configPath.exists()) {
			try (var reader = new LineReader(configPath)) {
				while (reader.hasNext()) {
					String line = reader.nextLine();
					if (line.startsWith("#")) continue; //comment identifier
					if (line.length() == 1) break; //ignore one character long lines
					
					String identifier = "", restOfLine = "";
					if (line.length() > 0) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ':') {
								identifier = line.substring(0, i + 1);
								restOfLine = line.substring(i + 1, line.length());
								break;
							}
						}
						if (!identifier.isEmpty()) {
							if (restOfLine.isEmpty()) configValues.add(identifier, new EArrayList<String>());
							else {
								String[] lineContents = restOfLine.split(" ");
								if (lineContents.length > 1) {
									EArrayList<String> values = new EArrayList();
									for (int i = 1; i < lineContents.length; i++) values.add(lineContents[i]);
									configValues.add(identifier, values);
								}
							}
						} //if
					}
				} //while
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected boolean doesFileContainIdentifier(String identifierIn) {
		if (configPath.exists()) {
			
			try (var reader = new LineReader(configPath)) {
				while (reader.hasNext()) {
					String line = reader.nextLine();
					if (line.startsWith("#")) continue; //comment identifier
					if (line.length() == 1) break; //ignore one character long lines
					
					String identifier = "";
					if (line.length() > 0) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ':') {
								identifier = line.substring(0, i + 1);
								//if (EUtil.findMatch(identifier, identifiers)) total++;
								if (identifier == identifierIn) return true;
								break;
							}
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	protected boolean createConfig(EArrayList<ConfigBlock> configContentsIn) {
		try (var saver = new PrintWriter(configPath, "UTF-8")) {
			//check for any special config blocks
			EArrayList<CreateIfExistsConfigBlock> special = new EArrayList(configContentsIn.stream().filter(b -> b instanceof CreateIfExistsConfigBlock).collect(Collectors.toList()));
			special.stream().filter(b -> !doesFileContainIdentifier(b.getStringToCheckFor())).forEach(b -> configContentsIn.remove(b));
			
			/*
			for (ConfigBlock block : new EArrayList<ConfigBlock>(configContentsIn)) {
				if (block instanceof CreateIfExistsConfigBlock) {
					//if the block is a 'CreateIfExistsConfigBlock', check if the config file exists -- if it does, check if the specific config line identifier exists in the file.
					//if the file exists, but the file doesn't contain the identifier, remove this block from the lines that will be saved.
					if (!doesFileContainIdentifier(((CreateIfExistsConfigBlock) block).getStringToCheckFor())) { configContentsIn.remove(block); }
				}
			}
			*/
			
			//move on to creating/overwriting the original file
			for (ConfigBlock block : configContentsIn) {
				BoxList<String, List<String>> blockContents = block.getBlockContents();
				
				for (Box2<String, List<String>> line : blockContents) {
					saver.print(line.getA() + " ");
					List<String> values = line.getB();
					for (int i = 0; i < values.size(); i++) {
						if (i != values.size() - 1) saver.print(values.get(i) + " ");
						else saver.print(values.get(i));
					}
					saver.println();
				}
				
				if (block.createsEmptyLineAfterBlock()) saver.println();
			}
			saver.println();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getConfigName() { return configName; }
	public boolean exists() { return configPath.exists(); }
	
	//----------------
	// Saving methods
	//----------------
	
	public QotConfigFile addLine(ConfigBlock blockIn) {
		config.addIfNotNull(blockIn);
		return this;
	}
	
	public QotConfigFile addLine(String keyWord, ConfigSetting setting) { return addLine(keyWord, setting, false); }
	public QotConfigFile addLine(String keyWord, ConfigSetting setting, boolean useDefault) {
		config.add(new ConfigBlock(keyWord, setting, useDefault));
		return this;
	}
	
	public QotConfigFile addLine(ConfigSetting setting) { return addLine(setting, false); }
	public QotConfigFile addLine(ConfigSetting setting, boolean useDefault) {
		config.add(new ConfigBlock(setting.getDescription() + ":", setting, useDefault));
		return this;
	}
	
	public <T> QotConfigFile addLine(Class<T> type, String keyWord, T... vals) {
		ConfigBlock b = null;
		
		try {
			if (type.isAssignableFrom(Boolean.class)) 	b = new ConfigBlock(keyWord, (Boolean[]) Arrays.copyOf(vals, vals.length, Boolean[].class));
			if (type.isAssignableFrom(Character.class)) b = new ConfigBlock(keyWord, (Character[]) Arrays.copyOf(vals, vals.length, Character[].class));
			if (type.isAssignableFrom(Byte.class)) 		b = new ConfigBlock(keyWord, (Byte[]) Arrays.copyOf(vals, vals.length, Byte[].class));
			if (type.isAssignableFrom(Short.class)) 	b = new ConfigBlock(keyWord, (Short[]) Arrays.copyOf(vals, vals.length, Short[].class));
			if (type.isAssignableFrom(Integer.class)) 	b = new ConfigBlock(keyWord, (Integer[]) Arrays.copyOf(vals, vals.length, Integer[].class));
			if (type.isAssignableFrom(Long.class)) 		b = new ConfigBlock(keyWord, (Long[]) Arrays.copyOf(vals, vals.length, Long[].class));
			if (type.isAssignableFrom(Float.class)) 	b = new ConfigBlock(keyWord, (Float[]) Arrays.copyOf(vals, vals.length, Float[].class));
			if (type.isAssignableFrom(Double.class)) 	b = new ConfigBlock(keyWord, (Double[]) Arrays.copyOf(vals, vals.length, Double[].class));
			if (type.isAssignableFrom(String.class)) 	b = new ConfigBlock(keyWord, (String[]) Arrays.copyOf(vals, vals.length, String[].class));
			if (type.isAssignableFrom(Enum.class)) 		b = new ConfigBlock(keyWord, (Enum[]) Arrays.copyOf(vals, vals.length, Enum[].class));
		}
		catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		config.addIfNotNull(b);
		return this;
	}
	
	public QotConfigFile addLine(String comment) {
		config.add(new CommentConfigBlock(comment));
		return this;
	}
	
	public void nl() {
		if (config.isNotEmpty()) {
			ConfigBlock b = config.getLast();
			if (b != null) b.nl();
		}
	}
	
	public boolean save() {
		boolean val = createConfig(config);
		config.clear();
		return val;
	}
	
	public boolean trySave() { return trySave(null); }
	public boolean trySave(EArrayList<ConfigSetting> settings) {
		addLine(configTitleLine != null ? configTitleLine : configName + " Config").nl();
		if (settings != null) {
			EUtil.filterForEach(settings, s -> !s.getIgnoreConfigWrite(), s -> addLine(s));
		}
		return save();
	}
	
	public boolean tryLoad() { return tryLoad(null); }
	public boolean tryLoad(EArrayList<ConfigSetting> settings) {
		try {
			if (load()) {
				if (settings != null) {
					EUtil.filterForEach(settings, s -> !s.getIgnoreConfigRead(), s -> setConfigVal(s));
				}
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean tryReset() { return tryReset(null); }
	public boolean tryReset(EArrayList<ConfigSetting> settings) {
		addLine(configTitleLine != null ? configTitleLine : configName + " Config").nl();
		if (settings != null) {
			settings.forEach(c -> addLine(c, true));
		}
		return save();
	}
	
}