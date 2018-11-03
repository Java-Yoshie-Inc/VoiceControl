package computer.model;

import computer.model.Chat.Sender;

public class Voice {
	
	public static TextToSpeech tts = new TextToSpeech();
	private static float volume = 1f;
	
	public static void say(String text, boolean print) {
		if(print) {
			Chat.send(Sender.Bot, text);
		}
		tts.speak(text, volume, true, true);
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
