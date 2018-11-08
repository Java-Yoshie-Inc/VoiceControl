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
			
			String[] answers = question.getAnswers();
			for(int i=0; i < answers.length; i++) {
				Voice.say(i+1 + ": " + answers[i], false);
			}
			
			String input = null;
			while(input == null || !isNumeric(input) || isNumeric(input) && Integer.parseInt(input) > answers.length) {
				input = waitForInput();
			}
			
			if(answers[Integer.parseInt(input)-1].equalsIgnoreCase(question.getCorrectAnswer())) {
				Voice.say("That's correct.", false);
			} else {
				Voice.say("That's wrong.", false);
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
