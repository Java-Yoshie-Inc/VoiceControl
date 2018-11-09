package skills.quiz;

import java.util.Random;

import main.Bot;
import skills.Skill;
import voice.Voice;

public class QuizSkill extends Skill {
	
	private static final Random random = new Random();
	
	private static final QuizQuestion[] QUESTIONS = new QuizQuestion[] {
			new QuizQuestion("How long is the Great Wall of China?", "400 miles", new String[] {"1000 miles", "100 miles", "265 miles"}), 
			new QuizQuestion("Who invented Ferrari?", "Enzo Ferrari", new String[] {"Albert Einstein", "Alfred Hitchcock", "Jeff Bezos"}), 
			new QuizQuestion("Which famous British women murderer of the 19th century was never arrested?", "Jack the Ripper", new String[] {"Roger Whittaker", "Micheal Jackson", "George Clooney"}),
			new QuizQuestion("What colour do you get when you mix red and white?", "Pink", new String[] {"Yellow", "Green", "Blue"}), 
			new QuizQuestion("What temperature does water boil at?", "100 ° Celsius", new String[] {"107 ° Celsius", "200 ° Celsius", "50 ° Celsius"}),
			new QuizQuestion("Who wrote Macbeth and Hamlet?", "Shakespeare", new String[] {"John Ashbery", "Steven Paulsen", "Joanne Rowling"}), 
			new QuizQuestion("When did the First World War start?", "1914", new String[] {"1879", "1917", "1923"}), 
			new QuizQuestion("What did Joseph Priesley discover in 1774?", "Oxygon", new String[] {"Chlorine", "Kunzite", "Emeralds"}), 
			new QuizQuestion("Which is the only mammal that can't jump?", "Elephant", new String[] {"Kangaroo", "Mouse", "Tapir"}), 
	};
	
	public QuizSkill(Bot bot) {
		super(bot, "Quiz");
	}
	
	@Override
	public void setup() {
		Voice.say("Welcome to " + NAME, false);
		loop();
	}
	
	@Override
	protected void loop() {
		while(true) {
			QuizQuestion question = QUESTIONS[random.nextInt(QUESTIONS.length)];
			Voice.say(question.toString(), false);
			
			String[] answers = question.getShuffledAnswers();
			for(int i=0; i < answers.length; i++) {
				Voice.say(i+1 + ": " + answers[i], false);
			}
			
			String input = null;
			while(true) {
				input = waitForInput();
				if(!isNumeric(input)) {
					Voice.say("That isn't a number", false);
				} else if(isNumeric(input) && Integer.parseInt(input) > answers.length) {
					Voice.say("You can only use a number between 1 and " + answers.length + ".", false);
				} else {
					break;
				}
			}
			
			if(answers[Integer.parseInt(input)-1].equals(question.getCorrectAnswer())) {
				Voice.say("That's correct.", false);
			} else {
				Voice.say("That's wrong. The correct answer is " + question.getCorrectAnswer(), false);
			}
		}
	}
	
	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
