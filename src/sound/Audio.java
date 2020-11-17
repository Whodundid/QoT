package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Audio {
	
	Clip clip;
	
	public Audio(String musicFile) {
		try {
			File file = new File(musicFile);
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
			
			setVolume(0.0125f);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
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
	
	public void setVolume(float volume) {
		if (volume < 0f || volume > 1f) {
			throw new IllegalArgumentException("Volume not valid: " + volume);
		}
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}
	
}
