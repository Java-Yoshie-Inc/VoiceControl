package grammar;

import tools.StringUtils;

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
			float similarityPercentage = StringUtils.getSimilarity1(text, synonym);
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
