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
			new QuizQuestion("Who interpreted the dreams of Pharaoh in the the Bible?", "Joseph", new String[] {"Daniel", "David", "Samuel"}),
			new QuizQuestion("Who is the author of Ben Hur?", "Lew Wallace", new String[] {"William Shakespeare", "Victor Huge", "Bernard Shaw"}),
			new QuizQuestion("Which game is played with five players on either side?", "Basketball", new String[] {"Volleyball", "Hockey", "Football"}),
			new QuizQuestion("What is the capital of Afghanistan", "Kabul", new String[] {"Teheran", "Baghdad", "Tashkent"}),
			new QuizQuestion("Who killed US President Abraham Lincoln?", "John Wilkes Booth", new String[] {"Lee Harvey Oswald", "John Hinckley", "Michael Schiavo"}),
			new QuizQuestion("Who won the Hockey World Cup in 1975?", "India", new String[] {"Pakistan", "Germany", "Australia"}),
			new QuizQuestion("Which TV news channel began to telecast in 1980?", "CNN", new String[] {"Star News", "BBC", "Fox News"}),
			new QuizQuestion("Which of the following is not a gas?", "Mercury", new String[] {"Nitrogen", "Oxygon", "Helium"}),
			new QuizQuestion("What colour do you get when you mix red and green?", "Yellow", new String[] {"Blue", "Cyan", "Orange"}), 
			new QuizQuestion("Who is the owner of Microsoft?", "Bill Gates", new String[] {"Will Smith", "Jeff Bezos", "Daniel Radcliffe"}), 
	};
	
	private QuizQuestion lastQuestion = null;
	
	public QuizSkill(Bot bot) {
		super(bot, "Quiz");
	}
	
	@Override
	public void setup() {
		Voice.say("Welcome to " + NAME, false);
	}
	
	@Override
	protected void loop() {
		while(true) {
			//Generate Question
			QuizQuestion question = null;
			while(question == null || question.equals(lastQuestion)) {
				question = QUESTIONS[random.nextInt(QUESTIONS.length)];
			}
			
			//Say Question
			Voice.say(question.toString(), false);
			
			//Say Answer Possibilities
			String[] answers = question.getShuffledAnswers();
			for(int i=0; i < answers.length; i++) {
				Voice.say(i+1 + ": " + answers[i], false);
			}
			
			//Wait for new Input
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
			
			//Return Result
			if(answers[Integer.parseInt(input)-1].equals(question.getCorrectAnswer())) {
				Voice.say("That's correct.", false);
			} else {
				Voice.say("That's wrong. The correct answer is " + question.getCorrectAnswer() + ".", false);
			}
			
			lastQuestion = question;
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
