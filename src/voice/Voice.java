package voice;

import main.Chat;
import main.Chat.Sender;

public class Voice {
	
	public static TextToSpeech tts = new TextToSpeech();
	private static float volume = 1f;
	
	private static Thread thread;
	
	public static void say(String text, boolean print, boolean newThread) {
		tts.stopSpeaking();
		if(print) {
			Chat.send(Sender.Bot, text);
		}
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				tts.speak(text, volume, true, true);
			}
		});
		if(newThread) {
			thread.start();
		} else {
			thread.run();
		}
	}
	
	public static void say(String text, boolean newThread) {
		say(text, true, newThread);
	}
	
	public static void say(String text) {
		say(text, true, true);
	}
	
	public static void setType(int id) {
		tts.setVoice(tts.getAvailableVoices().toArray()[id].toString());
	}
	
	public static void setVolume(float volume) {
		Voice.volume = volume;
	}
	
}
