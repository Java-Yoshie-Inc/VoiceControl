package computer.model;

public class Phrase {
	
	private final Synonyms synonyms;
	private final Action action;
	
	public Phrase(Synonyms synonyms, String action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run() {
				Voice.say(action);
			}
		};
	}
	
	public Phrase(Synonyms synonyms, Action action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run() {
				action.run();
			}
		};
	}
	
	public Phrase(Synonyms synonyms, String actionText, Action action) {
		this.synonyms = synonyms;
		this.action = new Action() {
			@Override
			public void run() {
				Voice.say(actionText);
				action.run();
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
			float similarityPercentage = (float) similarity / words.length;
			System.out.println(similarityPercentage + " " + synonym);
			if(similarityPercentage > highestSimilarity) {
				highestSimilarity = similarityPercentage;
			}
		}
		return highestSimilarity;
	}
	
	public void run() {
		if(action != null) {
			action.run();
		}
	}
	
	@Override
	public String toString() {
		return synonyms.toString();
	}
	
}
