package grammar;

public enum ActivationWord {

	Computer(new Synonyms(new String[] { "concerts", "compute", "computers", "conduit", "come to a jerk", "and", "i'm sure", "can cure", "kosher", "compare", "club", "computer", "come shop", "crunches", "come to", "come true", "i'm cured", "clump you okay", "clump you would have" })), 
	John(new Synonyms(new String[] { "john", "johnny", "jonny", "the shop", "jill", "sure", "the op", "the job", "job", "jody", "dont't", "joan"})), 
	Hey(new Synonyms(new String[] {"hey", "a", "eight", "and", "heh"})), 
	;
	
	private Synonyms synonyms;
	
	private ActivationWord(Synonyms synonyms) {
		this.setSynonyms(synonyms);
	}
	
	public Synonyms getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(Synonyms synonyms) {
		this.synonyms = synonyms;
	}
	
}