package computer.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Synonyms extends ArrayList<String> {
	
	private static final long serialVersionUID = 1L;
	
	public Synonyms(String[] array) {
		addAll(Arrays.asList(array));
	}
	
	public Synonyms(String text) {
		add(text);
	}
	
	public boolean equals(String object) {
		return contains(object);
	}
	
}
