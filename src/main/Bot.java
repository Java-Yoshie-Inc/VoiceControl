package main;

import grammar.Action;
import grammar.ActivationWord;
import grammar.Phrase;
import grammar.Synonyms;
import skills.Skill;
import skills.Skills;
import skills.standard.Phrases;
import skills.standard.StandardSkill;
import voice.Voice;

public class Bot {
	
	public enum Source {Chat, SpeechRecognition}
	private final ActivationWord activationWord = ActivationWord.Hey;
	private final Phrases PHRASES = new Phrases(this);
	private final Skills SKILLS = new Skills(this);
	
	private Skill activeSkill;
	private boolean ignoreInputs = false;
	private boolean useActivationWord = true;
	private boolean isEnabled = false;
	
	private final Phrase STOP = new Phrase(new Synonyms(new String[] {"stop"}), new Action() {
		@Override
		public void run(String text) {
			setActiveSkill(new StandardSkill(Bot.this));
		}
	});
	
	public Bot() {		
		setActiveSkill(new StandardSkill(this));
	}
	
	public boolean react(Source source, String input) {
		if(!ignoreInputs || source == Source.Chat) {
			if(!useActivationWord || activationWord.getSynonyms().equals(input) && !isEnabled || source == Source.Chat) {
				isEnabled = true;
				if(useActivationWord && source != Source.Chat) {
					Voice.say("Yes", false, true);
					return false;
				}
			}
			
			if(isEnabled) {
				if(STOP.getSynonyms().equals(input)) {
					Voice.stop();
					STOP.run(input);
					return true;
				}
				isEnabled = false;
				activeSkill.sendInput(input);
				return true;
			} else {
				System.out.println("Cannot identify " + input + " as activation word");
				return false;
			}
		}
		return false;
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
	
	public ActivationWord getActivationWord() {
		return this.activationWord;
	}
	public void setIgnoreInputs(boolean ignore) {
		this.ignoreInputs = ignore;
	}
	public void setUseActivationWord(boolean useActivationWord) {
		this.useActivationWord = useActivationWord;
	}
	
}
