package main;

import main.Bot.Source;
import main.Chat.Sender;
import voice.Voice;

public class VoiceControl {
	
	private final Bot bot;
	
	public VoiceControl() {
		bot = new Bot();
		new User(bot, new SpeechRecognizeEvent() {
			@Override
			public void say(String input) {
				boolean processed = bot.react(Source.SpeechRecognition, input);
				if(processed) {
					Chat.send(Sender.User, input);
				}
			}
		});
		Chat.init(new SpeechRecognizeEvent() {
			@Override
			public void say(String input) {
				boolean processed = bot.react(Source.Chat, input);
				if(processed) {
					Chat.send(Sender.User, input);
				}
			}
		});
	}
	
	static {
		Voice.setType(2);
		Voice.setVolume(1f);
	}
	
	public static void main(String[] args) {
		new VoiceControl();
	}

}
