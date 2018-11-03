package computer.model;

public class Voice {
	
	public static TextToSpeech tts = new TextToSpeech();
	
	public static void say(String text) {
		tts.speak(text, 1, true, true);
	}
	
	public static void setType(int id) {
		tts.setVoice(tts.getAvailableVoices().toArray()[id].toString());
	}
	
}
