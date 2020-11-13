package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
		clip.setFramePosition(0);
		clip.start();
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
	
}
