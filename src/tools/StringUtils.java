package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

	public static final String[] DIGITS = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
	public static final String[] TENS = { null, "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };
	public static final String[] TEENS = { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };
	public static final String[] MAGNITUDES = { "hundred", "thousand", "million", "point" };
	public static final String[] ZERO = { "zero", "oh" };
	
	public static void main(String[] args) {
		
	}
	
	public static int getNumber(String input) {
	    String result = "";
	    String[] decimal = input.split(MAGNITUDES[3]);
	    String[] millions = decimal[0].split(MAGNITUDES[2]);

	    for (int i = 0; i < millions.length; i++) {
	        String[] thousands = millions[i].split(MAGNITUDES[1]);

	        for (int j = 0; j < thousands.length; j++) {
	            int[] triplet = {0, 0, 0};
	            StringTokenizer set = new StringTokenizer(thousands[j]);

	            if (set.countTokens() == 1) { //If there is only one token given in triplet
	                String uno = set.nextToken();
	                triplet[0] = 0;
	                for (int k = 0; k < DIGITS.length; k++) {
	                    if (uno.equals(DIGITS[k])) {
	                        triplet[1] = 0;
	                        triplet[2] = k + 1;
	                    }
	                    if (uno.equals(TENS[k])) {
	                        triplet[1] = k + 1;
	                        triplet[2] = 0;
	                    }
	                }
	            }


	            else if (set.countTokens() == 2) {  //If there are two tokens given in triplet
	                String uno = set.nextToken();
	                String dos = set.nextToken();
	                if (dos.equals(MAGNITUDES[0])) {  //If one of the two tokens is "hundred"
	                    for (int k = 0; k < DIGITS.length; k++) {
	                        if (uno.equals(DIGITS[k])) {
	                            triplet[0] = k + 1;
	                            triplet[1] = 0;
	                            triplet[2] = 0;
	                        }
	                    }
	                }
	                else {
	                    triplet[0] = 0;
	                    for (int k = 0; k < DIGITS.length; k++) {
	                        if (uno.equals(TENS[k])) {
	                            triplet[1] = k + 1;
	                        }
	                        if (dos.equals(DIGITS[k])) {
	                            triplet[2] = k + 1;
	                        }
	                    }
	                }
	            }

	            else if (set.countTokens() == 3) {  //If there are three tokens given in triplet
	                String uno = set.nextToken();
	                String dos = set.nextToken();
	                String tres = set.nextToken();
	                for (int k = 0; k < DIGITS.length; k++) {
	                    if (uno.equals(DIGITS[k])) {
	                        triplet[0] = k + 1;
	                    }
	                    if (tres.equals(DIGITS[k])) {
	                        triplet[1] = 0;
	                        triplet[2] = k + 1;
	                    }
	                    if (tres.equals(TENS[k])) {
	                        triplet[1] = k + 1;
	                        triplet[2] = 0;
	                    }
	                }
	            }

	            else if (set.countTokens() == 4) {  //If there are four tokens given in triplet
	                String uno = set.nextToken();
	                String dos = set.nextToken();
	                String tres = set.nextToken();
	                String cuatro = set.nextToken();
	                for (int k = 0; k < DIGITS.length; k++) {
	                    if (uno.equals(DIGITS[k])) {
	                        triplet[0] = k + 1;
	                    }
	                    if (cuatro.equals(DIGITS[k])) {
	                        triplet[2] = k + 1;
	                    }
	                    if (tres.equals(TENS[k])) {
	                        triplet[1] = k + 1;
	                    }
	                }
	            }
	            else {
	                triplet[0] = 0;
	                triplet[1] = 0;
	                triplet[2] = 0;
	            }

	            result = result + Integer.toString(triplet[0]) + Integer.toString(triplet[1]) + Integer.toString(triplet[2]);
	        }
	    }

	    if (decimal.length > 1) {  //The number is a decimal
	        StringTokenizer decimalDigits = new StringTokenizer(decimal[1]);
	        result = result + ".";
	        System.out.println(decimalDigits.countTokens() + " decimal digits");
	        while (decimalDigits.hasMoreTokens()) {
	            String w = decimalDigits.nextToken();
	            System.out.println(w);

	            if (w.equals(ZERO[0]) || w.equals(ZERO[1])) {
	                result = result + "0";
	            }
	            for (int j = 0; j < DIGITS.length; j++) {
	                if (w.equals(DIGITS[j])) {
	                    result = result + Integer.toString(j + 1);
	                }   
	            }

	        }
	    }
	    return Integer.parseInt(result);
	}
	
	public static String[] getPossibleSubstrings(String str) {
		List<String> array = new ArrayList<String>();
		for (int i = 0; i < str.length(); i++) {
			for (int j = i + 1; j <= str.length(); j++) {
				array.add(str.substring(i, j));
			}
		}
		return array.toArray(new String[0]);
	}
	
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