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

		PHRASES.add(new Phrase("what time is it", new Action() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("hh");
				SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
				String am = "am";
				System.out.println(new SimpleDateFormat("HH:mm").format(cal.getTime()));
				Voice.say("It is " + sdf.format(cal.getTime()) + " " + sdf2.format(cal.getTime()));
			}
		}));
		//PHRASES.add(new Phrase(String[] {"Hello", "Hi", "Good Morning"}, "Hello"));
		PHRASES.add(new Phrase("How are you", "Oh, i am fine"));
		PHRASES.add(new Phrase("stop", "Thank you for using our services. Au revoir!", new Action() {
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
		Phrase phrase = new Phrase(new Synonyms(words));
		float highestSimilarity = 0;
		Phrase bestPhrase = null;

		for (Phrase templatePhrase : PHRASES) {
			if (templatePhrase.getSimilarity(phrase) > highestSimilarity) {
				highestSimilarity = templatePhrase.getSimilarity(phrase);
				bestPhrase = templatePhrase;
			}
		}

		if (bestPhrase != null) {
			bestPhrase.run();
		} else {
			Voice.say("I am sorry, I didnt understand that");
		}

		System.out.println("Recognized: " + phrase);
	}

}
