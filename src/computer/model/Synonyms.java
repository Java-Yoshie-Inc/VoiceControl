package computer.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Synonyms extends ArrayList<String> {
	
	private static final long serialVersionUID = 1L;
	
	public Synonyms(String[] array) {
		addAll(Arrays.asList(array));
	}
	
	public Synonyms(ArrayList<String> list) {
		addAll(list);
	}
	
	public Synonyms(String text) {
		add(text);
	}
	
	public boolean equals(String object) {
		return contains(object);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.toArray(new String[0]));
	}
	
	public Object[] toArray() {
		return super.toArray();
	}
	
}
