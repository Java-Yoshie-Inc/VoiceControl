package computer.model;

import java.util.Random;

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
	
	public float getSimilarity(String text) {
		float highestSimilarity = 0f;
		
		for(String synonym : synonyms) {
			float similarity = 0;
			String[] words = synonym.split(" ");
			for(String word : text.split(" ")) {
				for(String word2 : words) {
					if(word.toLowerCase().equals(word2.toLowerCase())) {
						similarity++;
						break;
					} else {
						for(int i = 3; i < word.length(); i++) {
							String substring = word.substring(i - 3, i);
							for(int j = 3; j < word2.length(); j++) {
								String substring2 = word2.substring(j - 3, j);
								if(substring.equals(substring2)) {
									similarity += 0.5;
								}
							}
						}
					}
				}
			}
			
			float similarityPercentage = (float) similarity / Math.max(text.length(), words.length);
			if(similarityPercentage > highestSimilarity) {
				highestSimilarity = similarityPercentage;
			}
		}
		return highestSimilarity;
	}
	
	public void run(String text) {
		if(action != null) {
			action.run(text);
		}
	}
	
	@Override
	public String toString() {
		return synonyms.toString();
	}
	
}
