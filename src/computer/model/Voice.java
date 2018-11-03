package computer.model;

import computer.model.Chat.Sender;

public class Voice {
	
	public static TextToSpeech tts = new TextToSpeech();
	private static float volume = 1f;
	
	public static void say(String text, boolean print) {
		tts.speak(text, volume, true, true);
		if(print) {
			Chat.send(Sender.Bot, text);
		}
	}
	
	public static void say(String text) {
		say(text, true);
	}
	
	public static void setType(int id) {
		tts.setVoice(tts.getAvailableVoices().toArray()[id].toString());
	}
	
	public static void setVolume(float volume) {
		Voice.volume = volume;
	}
	
}
