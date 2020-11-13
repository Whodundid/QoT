package sound;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SFX {
	public void playSFX(String SFXLocation) {
		try {
			File SFXPath = new File(SFXLocation);
			if (SFXPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(SFXPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
			}
			else {
				System.out.println("Could not located the File.");
			}
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
