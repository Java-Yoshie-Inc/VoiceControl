package computer.model;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ibm.icu.text.DateFormatSymbols;

import computer.model.Chat.Sender;

public class Computer {

	private static final List<Phrase> PHRASES = new ArrayList<Phrase>();
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
		PHRASES.add(new Phrase(new Synonyms(new String[] {"Hello", "Hi", "Good Morning", "Good Evening"}), new String[] {"Hello", "Good morning", "Hi"}));
		PHRASES.add(new Phrase(new Synonyms("how are you"), new String[] {"Oh, I am fine!", "Great"}));
		PHRASES.add(new Phrase(new Synonyms("stop"), "Thank you for using our service. Au revoir!", new Action() {
			@Override
			public void run() {
				System.exit(0);
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] {"what is the date", "what is the day"}), new Action() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
				String[] date = sdf.format(cal.getTime()).split(":");
				Voice.say("Today is the " + date[0] + "nd of " + getMonth(Integer.parseInt(date[1])) + " " + date[2]);
			}
		}));
		PHRASES.add(new Phrase(new Synonyms(new String[] {"where is", "where can I find"}), new Action() {
			@Override
			public void run() {
				try {
					String location = "Hamburg";
					Desktop.getDesktop().browse(new URI("https://www.google.de/maps/place/"+location));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}));
		//Runtime.getRuntime().exec("shutdown /h");
	}

	public static void main(String[] args) {
		new Computer();
	}

	public Computer() {
		Chat.init(this);
		new SpeechRecognizerMain(this);
	}
	
	public void say(String words) {
		Chat.send(Sender.User, words);
		
		float highestSimilarity = 0;
		Phrase bestPhrase = null;
		
		for (Phrase templatePhrase : PHRASES) {
			if (templatePhrase.getSimilarity(words) > highestSimilarity) {
				highestSimilarity = templatePhrase.getSimilarity(words);
				bestPhrase = templatePhrase;
			}
		}

		if (bestPhrase != null && highestSimilarity >= 0.2f) {
			bestPhrase.run();
		} else {
			Voice.say("I am sorry, I didnt understand that.");
		}
	}
	
	public static String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}

}
