package computer.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ibm.icu.text.DateFormatSymbols;

import computer.model.Chat.Sender;

public class Computer {

	private static final List<Phrase> PHRASES = new ArrayList<Phrase>();
	private final SpeechRecognizerMain recognizer;
	static {
		Chat.init();
		Voice.setType(2);
		
		PHRASES.add(new Phrase(new Synonyms(new String[] {"what is the time", "what time is it", "how late is it"}), new Action() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("hh");
				SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
				Voice.say("It is " + sdf.format(cal.getTime()) + " " + sdf2.format(cal.getTime()));
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] {"Hello", "Hi", "Good Morning"}), "Hello"));
		PHRASES.add(new Phrase(new Synonyms(new String[] {"Hello", "Hi", "Good Morning"}), "Hello"));
		PHRASES.add(new Phrase(new Synonyms("how are you"), "Oh, i am fine"));
		PHRASES.add(new Phrase(new Synonyms("stop"), "Thank you for using our services. Au revoir!", new Action() {
			@Override
			public void run() {
				System.exit(0);
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] {"what is the date"}), new Action() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
				String[] date = sdf.format(cal.getTime()).split(":");
				Voice.say("Today is the " + date[0] + "nd of " + getMonth(Integer.parseInt(date[1])) + " " + date[2]);
			}
		}));
	}

	public static void main(String[] args) {
		new Computer();
	}

	public Computer() {
		recognizer = new SpeechRecognizerMain(this);
	}
	
	public void say(String words) {
		Chat.send(Sender.User, words);
		System.out.println("Recognized: " + words);
		
		float highestSimilarity = 0;
		Phrase bestPhrase = null;
		
		for (Phrase templatePhrase : PHRASES) {
			if (templatePhrase.getSimilarity(words) > highestSimilarity) {
				highestSimilarity = templatePhrase.getSimilarity(words);
				bestPhrase = templatePhrase;
			}
		}

		if (bestPhrase != null) {
			bestPhrase.run();
		} else {
			Voice.say("I am sorry, I didnt understand that");
		}
	}
	
	public static String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}

}
