package main;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ibm.icu.text.DateFormatSymbols;

import grammar.Action;
import grammar.Phrase;
import grammar.PhraseComparison;
import grammar.Synonyms;
import main.Chat.Sender;
import tools.Wikipedia;
import voice.Voice;

public class Computer {

	private static final List<Phrase> PHRASES = new ArrayList<Phrase>();
	
	private boolean asksForYesOrNo = false;
	private String oldWords;
		
	private SpeechRecognizer speechRecognizer;
	
	private static String[] locationSynonyms = new String[] {"where it", "where is", "where are", "where's", "where can i find", "where in"};
	
	static {
		Voice.setType(2);
		
		PHRASES.add(new Phrase(new Synonyms(new String[] { "Hello", "Hi", "Good Morning", "Good Evening" }),
				new String[] { "Hello", "Good morning", "Hi" }));
		PHRASES.add(new Phrase(new Synonyms("how are you"), new String[] { "Oh, I am fine!", "Great" }));
		PHRASES.add(new Phrase(
				new Synonyms(
						new String[] { "what's the time", "what is the time", "what time is it", "how late is it" }),
				new Action() {
					@Override
					public void run(String text) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("hh");
						SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
						Voice.say("It is " + sdf.format(cal.getTime()) + " " + sdf2.format(cal.getTime()));
					}
				}));
		PHRASES.add(new Phrase(new Synonyms("stop"), new Action() {
			@Override
			public void run(String gtext) {
				Voice.say("Thank you for using our service. Au revoir!", false);
				System.exit(0);
			}
		}));
		PHRASES.add(new Phrase(new Synonyms("tell me a joke"), new Action() {
			@Override
			public void run(String gtext) {
				Voice.say("Thank you for using our service. Au revoir!", false);
				System.exit(0);
			}
		}));
		PHRASES.add(new Phrase(new Synonyms("plunger"), "Oh, I love plungers."));
		PHRASES.add(new Phrase(new Synonyms(new String[] { "what's the date", "what is the date", "what is the day" }),
				new Action() {
					@Override
					public void run(String text) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
						String[] date = sdf.format(cal.getTime()).split(":");
						Voice.say("Today is the " + date[0] + "nd of " + getMonth(Integer.parseInt(date[1])) + " "
								+ date[2]);
					}
				}));
		PHRASES.add(new Phrase(new Synonyms(new String[] { "What is", "Who is" }), new Action() {
			@Override
			public void run(String text) {
				try {
					String term = text.replace("what is ", "").replace("who is ", "");
					if (term != null && !term.equals("")) {
						Voice.say("Here are the results for " + term, false);
						String result = Wikipedia.getInformation(term);
						Voice.say(result, true);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(locationSynonyms), new Action() {
			@Override
			public void run(String text) {
				try {
					String location = text;
					for(String synonym : locationSynonyms) {
						location = location.replace(synonym, "");
					}
					location = location.trim();
					Voice.say("Here is the location of " + location);
					location = location.replace(" ", "+");
					Desktop.getDesktop().browse(new URI("https://www.google.de/maps/place/" + location));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] { "Shut down my computer" }), new Action() {
			@Override
			public void run(String text) {
				Voice.say("Your computer will shut down in one minute.");
				try {
					Runtime.getRuntime().exec("shutdown /s /t 60");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] { "Cancel shut down my computer" }), new Action() {
			@Override
			public void run(String text) {
				Voice.say("Yes");
				try {
					Runtime.getRuntime().exec("shutdown /a");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
	public static void main(String[] args) {
		new Computer();
	}
	
	public Computer() {
		Chat.init(this);
		speechRecognizer = new SpeechRecognizer(this);
	}
	
	public void say(String words) {
		System.out.println("Recognized: " + words);
		Chat.send(Sender.User, words);
		
		if (asksForYesOrNo) {
			if (words.toLowerCase().equals("yes")) {
				try {
					String result = Wikipedia.getInformation(oldWords);
					Voice.say(result, true);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Voice.say("Ok");
			}
			asksForYesOrNo = false;
			return;
		}

		float similarity = 0;
		Phrase phrase = null;
		
		for (Phrase templatePhrase : PHRASES) {
			PhraseComparison phraseComparison = new PhraseComparison(templatePhrase, words);
			if (phraseComparison.getSimilarity() > similarity) {
				similarity = phraseComparison.getSimilarity();
				phrase = templatePhrase;
			}
		}
		if (phrase != null && similarity >= 0.2f || asksForYesOrNo) {
			phrase.run(words.toLowerCase());
			speechRecognizer.askQuestion(false);
		} else {
			oldWords = words;
			Voice.say("Should I search for " + words + "?", false);
			asksForYesOrNo = true;
			speechRecognizer.askQuestion(true);
		}
	}
	
	public static String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month - 1];
	}

}
