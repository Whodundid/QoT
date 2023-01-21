package envisionEngine.gameEngine.effects.sounds;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
	
	Clip clip;
	
	public void setFile(String music) {
		try {
			File file = new File(music);
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}
		catch(Exception e) {
			
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
