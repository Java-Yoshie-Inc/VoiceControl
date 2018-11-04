package tools;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
	
	public static void play(String file) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(file).toURI().toURL());
				    Clip audio = AudioSystem.getClip();
				    audio.open(audioStream);
				    audio.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
}
