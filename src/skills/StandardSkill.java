package skills;

import grammar.Phrase;
import grammar.PhraseComparison;
import main.Bot;
import main.Chat;
import tools.Wikipedia;
import voice.Voice;

public class StandardSkill extends Skill {
	
	public StandardSkill(Bot bot) {
		super(bot, "Standard Skill of Voice Control");
	}
	
	@Override
	public void setup() {
		
	}
	
	@Override
	protected void loop() {
		while(true) {
			bot.getSpeechRecognizer().setUseActivationWord(true);
			String input = waitForInput();
			
			float similarity = 0;
			Phrase phrase = null;
			
			for (Phrase templatePhrase : bot.getPhrases()) {
				PhraseComparison phraseComparison = new PhraseComparison(templatePhrase, input);
				if (phraseComparison.getSimilarity() > similarity) {
					similarity = phraseComparison.getSimilarity();
					phrase = templatePhrase;
				}
			}
			if (phrase != null && similarity >= 0.2f) {
				System.out.println(similarity + " " + phrase);
				phrase.run(input.toLowerCase());
			} else {
				String oldWords = input;
				Voice.say("Should I search for " + input + "?", false);
				
				bot.getSpeechRecognizer().setUseActivationWord(false);
				input = waitForInput();
				
				if (input.toLowerCase().equals("yes")) {
					try {
						String result = Wikipedia.getInformation(oldWords);
						Voice.say(result, true);
					} catch (Exception e) {
						Chat.sendError(e);
					}
				} else {
					Voice.say("Ok");
				}
			}
		}	
	}
	
}
