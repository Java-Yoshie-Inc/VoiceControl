package tools;

public class StringUtils {
	
	public static float getSimilarity1(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) {
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0f;
		}
		return (longerLength - editDistance(longer, shorter)) / (float) longerLength;

	}
	
	private static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}
	
	public static float getSimilarity2(String s1, String s2) {
		float similarity = 0;
		String[] words = s2.split(" ");
		for (String word : s1.split(" ")) {
			for (String word2 : words) {
				if (word.toLowerCase().equals(word2.toLowerCase())) {
					similarity++;
					break;
				} else {
					/*for (int i = 3; i < word.length(); i++) {
						String substring = word.substring(i - 3, i);
						for (int j = 3; j < word2.length(); j++) {
							String substring2 = word2.substring(j - 3, j);
							if (substring.equals(substring2)) {
								similarity += 0.5f;
							}
						}
					}*/
				}
			}
		}
		return (float) similarity / words.length;
	}
	
}