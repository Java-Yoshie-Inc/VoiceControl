package main;

import grammar.ActivationWord;
import grammar.InputBlacklist;
import grammar.Phrase;
import grammar.PhraseComparison;
import grammar.Phrases;
import main.Chat.Sender;
import tools.Wikipedia;
import voice.Voice;

public class Computer {
	
	private static final Phrases PHRASES = new Phrases();
	
	private boolean asksForYesOrNo = false;
	private String oldWords;
	
	private SpeechRecognizer speechRecognizer;
	
	private final SpeechRecognizeEvent EVENT = new SpeechRecognizeEvent() {
		@Override
		public void say(String text) {
			Computer.this.say(text);
		}
	};
	
	public static void main(String[] args) {
		new Computer();
	}
	
	static {
		Voice.setType(2);
		Voice.setVolume(1f);
	}
	
	public Computer() {
		Chat.init(EVENT);
		
		speechRecognizer = new SpeechRecognizer(ActivationWord.Hey, EVENT);
		speechRecognizer.setUseActivationWord(true);
		speechRecognizer.setBlacklist(new InputBlacklist());
	}
	
	public void say(String words) {
		System.out.println("Recognized: " + words);
		Chat.send(Sender.User, words);
		
		if (asksForYesOrNo) {
			if (words.toLowerCase().equals("yes")) {
				try {
					String result = Wikipedia.getInformation(oldWords);
					Voice.say(result, true);
				} catch (Exception e) {
					Chat.sendError(e);
				}
			} else {
				Voice.say("Ok");
			}
			asksForYesOrNo = false;
			speechRecognizer.setUseActivationWord(true);
			return;
		}
		
		float similarity = 0;
		Phrase phrase = null;
		
		for (Phrase templatePhrase : PHRASES) {
			PhraseComparison phraseComparison = new PhraseComparison(templatePhrase, words);
			if (phraseComparison.getSimilarity() > similarity) {
				similarity = phraseComparison.getSimilarity();
				phrase = templatePhrase;
			}
		}
		if (phrase != null && similarity >= 0.2f || asksForYesOrNo) {
			phrase.run(words.toLowerCase());
		} else {
			oldWords = words;
			Voice.say("Should I search for " + words + "?", false);
			asksForYesOrNo = true;
			speechRecognizer.setUseActivationWord(false);
		}
	}

}
