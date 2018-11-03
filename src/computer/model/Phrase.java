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
			int similarity = 0;
			String[] words = synonym.split(" ");
			
			for(String word : text.split(" ")) {
				boolean contains = false;;
				for(String word2 : words) {
					if(word.toLowerCase().equals(word2.toLowerCase())) {
						contains = true;
						break;
					}
				}
				
				if(contains) {
					similarity++;
				}
			}
			
			float similarityPercentage = (float) similarity / words.length;
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
