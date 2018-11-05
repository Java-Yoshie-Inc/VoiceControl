package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.ibm.icu.text.DateFormatSymbols;

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
		
	static {
		Voice.setType(2);
	}
	
	public static void main(String[] args) {
		new Computer();
	}
	
	public Computer() {
		Chat.init(this);
		speechRecognizer = new SpeechRecognizer(this);
	}
	
	public void say(String words) {
		System.out.println("Recognized: " + words);
		Chat.send(Sender.User, words);
		
		if (asksForYesOrNo) {
			if (words.toLowerCase().equals("yes")) {
				try {
					String result = Wikipedia.getInformation(oldWords);
					Voice.say(result, true);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Voice.say("Ok");
			}
			asksForYesOrNo = false;
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
			speechRecognizer.askQuestion(false);
		} else {
			oldWords = words;
			Voice.say("Should I search for " + words + "?", false);
			asksForYesOrNo = true;
			speechRecognizer.askQuestion(true);
		}
	}

}
