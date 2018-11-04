package grammar;

public class PhraseComparison {
	
	private final Phrase phrase;
	private final String text;
	
	private final float similarity;
	private final String synonym;
	
	public PhraseComparison(Phrase phrase, String text) {
		this.phrase = phrase;
		this.text = text;
		
		float highestSimilarity = 0f;
		String bestSynonym = null;
		
		for (String synonym : phrase.getSynonyms()) {
			float similarity = 0;
			String[] words = synonym.split(" ");
			for (String word : text.split(" ")) {
				for (String word2 : words) {
					if (word.toLowerCase().equals(word2.toLowerCase())) {
						similarity++;
						break;
					} else {
						for (int i = 3; i < word.length(); i++) {
							String substring = word.substring(i - 3, i);
							for (int j = 3; j < word2.length(); j++) {
								String substring2 = word2.substring(j - 3, j);
								if (substring.equals(substring2)) {
									similarity += 0.5;
								}
							}
						}
					}
				}
			}
			
			float similarityPercentage = (float) similarity / words.length;
			if (similarityPercentage > highestSimilarity) {
				highestSimilarity = similarityPercentage;
			}
		}
		
		this.similarity = highestSimilarity;
		this.synonym = bestSynonym;
	}
	
	public float getSimilarity() {
		return this.similarity;
	}
	
	public String getSynonym() {
		return this.synonym;
	}
	
}
