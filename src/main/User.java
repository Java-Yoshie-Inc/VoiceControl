package main;

import grammar.InputBlacklist;

public class User {
	
	private SpeechRecognizer speechRecognizer;
	
	public User(Bot bot, SpeechRecognizeEvent event) {
		speechRecognizer = new SpeechRecognizer(event);
		speechRecognizer.setBlacklist(new InputBlacklist());
	}
	
	public SpeechRecognizer getSpeechRecognizer() {
		return this.speechRecognizer;
	}
	
}
