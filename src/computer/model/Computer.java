package computer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Computer {

	private static final List<Phrase> PHRASES = new ArrayList<Phrase>();
	private final SpeechRecognizerMain recognizer;

	ArrayList<Phrase> phrases = new ArrayList<Phrase>();

	static {
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
<<<<<<< HEAD
		PHRASES.add(new Phrase(new Synonyms(new String[] {"Hello", "Hi", "Good Morning"}), "Hello"));
=======
		System.out.println("sdfsdfsf");
		//PHRASES.add(new Phrase(String[] {"Hello", "Hi", "Good Morning"}, "Hello"));
>>>>>>> master
		PHRASES.add(new Phrase(new Synonyms("how are you"), "Oh, i am fine"));
		PHRASES.add(new Phrase(new Synonyms("stop"), "Thank you for using our services. Au revoir!", new Action() {
			@Override
			public void run() {
				System.exit(0);
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

		System.out.println("Recognized: " + words);
	}

}
