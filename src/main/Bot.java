package main;

import grammar.ActivationWord;
import grammar.InputBlacklist;
import grammar.Phrases;
import main.Chat.Sender;
import skills.Skill;
import skills.Skills;
import voice.Voice;

public class Bot {
	
	private final Phrases PHRASES = new Phrases(this);
	private static final Skills SKILLS = new Skills();
	
	private SpeechRecognizer speechRecognizer;
	private Skill activeSkill = null;
	
	private final SpeechRecognizeEvent EVENT = new SpeechRecognizeEvent() {
		@Override
		public void say(String text) {
			Bot.this.say(text);
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
	}
	
	public void say(String words) {
		System.out.println("Recognized: " + words);
		Chat.send(Sender.User, words);
		
		if(activeSkill != null) {
			activeSkill.sendInput(words);
		}
	}
	
	public Skills getSkills() {
		return SKILLS;
	}
	
	public void setActiveSkill(Skill activeSkill) {
		this.activeSkill = activeSkill;
		if(activeSkill != null) {
			activeSkill.start();
			speechRecognizer.setUseActivationWord(false);
		} else {
			speechRecognizer.setUseActivationWord(true);
		}
	}

	public Phrases getPhrases() {
		return PHRASES;
	}
	
	public SpeechRecognizer getSpeechRecognizer() {
		return this.speechRecognizer;
	}
	
}
