package engine.soundEngine;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.settings.QoT_Settings;

public class Audio {
	
	private String name;
	private Clip clip;
	
	public Audio(String nameIn, String musicFile) { this(nameIn, null, musicFile); }
	public Audio(String nameIn, String basePath, String fileName) {
		if (basePath != null) fileName = basePath + fileName;
		name = nameIn;
		
		try {
			File file = new File(fileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public void start() {
		try {
			clip.setFramePosition(0);
			clip.start();
			
			setVolume(1f);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loop() {
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		setVolume(QoT_Settings.musicVolume.get() * 0.0009f);
	}
		
	public void stop() {
		clip.stop();
	}
	
	public void close() {
		clip.close();
	}
	
	public float getVolume() {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}
	
	public void setVolume(double volumeIn) {
		float volume = (float) volumeIn;
		
		if (volume < 0 || volume > 1) {
			throw new IllegalArgumentException("Volume not valid: (0,1) '" + volume + "'");
		}
		
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}
	
	public String getName() {
		return name;
	}
	
}
