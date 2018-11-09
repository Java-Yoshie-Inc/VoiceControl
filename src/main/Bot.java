package main;

import grammar.Action;
import grammar.ActivationWord;
import grammar.InputBlacklist;
import grammar.Phrase;
import grammar.Phrases;
import grammar.Synonyms;
import main.Chat.Sender;
import skills.Skill;
import skills.Skills;
import skills.StandardSkill;
import voice.Voice;

public class Bot {
	
	private final Phrases PHRASES = new Phrases(this);
	private final Skills SKILLS = new Skills(this);
	
	private SpeechRecognizer speechRecognizer;
	private Skill activeSkill;
	
	private final Phrase STOP = new Phrase(new Synonyms(new String[] {"stop"}), new Action() {
		@Override
		public void run(String text) {
			setActiveSkill(new StandardSkill(Bot.this));
		}
	});
	
	private final SpeechRecognizeEvent EVENT = new SpeechRecognizeEvent() {
		@Override
		public void say(String input) {
			System.out.println("Recognized: " + input);
			Chat.send(Sender.User, input);
			if(STOP.getSynonyms().equals(input)) {
				setActiveSkill(new StandardSkill(Bot.this));
				Voice.stop();
			}
			activeSkill.sendInput(input);
		}
	};
	
	public static void main(String[] args) {
		new Bot();
	}
	
	static {
		Voice.setType(2);
		Voice.setVolume(1f);
	}
	
	public Bot() {
		Chat.init(EVENT);
		
		speechRecognizer = new SpeechRecognizer(ActivationWord.Hey, EVENT);
		speechRecognizer.setUseActivationWord(true);
		speechRecognizer.setBlacklist(new InputBlacklist());
		
		setActiveSkill(new StandardSkill(this));
	}
	
	public Skills getSkills() {
		return SKILLS;
	}
	
	public void setActiveSkill(Skill activeSkill) {
		if(this.activeSkill != null) {
			this.activeSkill.stop();
		}
		this.activeSkill = activeSkill;
		activeSkill.start();
	}

	public Phrases getPhrases() {
		return PHRASES;
	}
	
	public SpeechRecognizer getSpeechRecognizer() {
		return this.speechRecognizer;
	}
	
}
