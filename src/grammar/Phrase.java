package grammar;

import java.util.Random;

import voice.Voice;

public class Phrase {

	private static final Random RANDOM = new Random();

	private final Synonyms synonyms;
	private final Action action;

	public Phrase(Synonyms synonyms, String action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run(String text) {
				Voice.say(action);
			}
		};
	}
	
	public Phrase(Synonyms synonyms, String[] actions) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run(String words) {
				Voice.say(actions[RANDOM.nextInt(actions.length)]);
			}
		};
	}
	
	public Phrase(Synonyms synonyms, Action action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run(String text) {
				action.run(text);
			}
		};
	}
	
	public Phrase(Synonyms synonyms, String actionText, Action action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run(String text) {
				Voice.say(actionText);
				action.run(text);
			}
		};
	}
	
	public void run(String text) {
		if (action != null) {
			action.run(getParameter(text));
		}
	}
	
	private String getParameter(String parameter) {
		for(String synonym : synonyms) {
			parameter = parameter.replaceAll(synonym, "");
		}
		parameter = parameter.trim();
		return parameter;
	}
	
	@Override
	public String toString() {
		return synonyms.toString();
	}
	public Synonyms getSynonyms() {
		return synonyms;
	}

}
