package skills.quiz;

import java.util.Arrays;
import java.util.List;

public class QuizQuestion {
	
	private final String QUESTION;
	private final String CORRECT_ANSWER;
	private final String[] WRONG_ANSWERS;
	
	public QuizQuestion(String question, String correctAnswer, String[] wrongAnswers) {
		this.QUESTION = question;
		this.CORRECT_ANSWER = correctAnswer;
		this.WRONG_ANSWERS = wrongAnswers;
	}
	
	@Override
	public String toString() {
		return QUESTION;
	}
	
	public String getQuestion() {
		return QUESTION;
	}
	public String getCorrectAnswer() {
		return CORRECT_ANSWER;
	}
	public String[] getAnswers() {
		List<String> array = Arrays.asList(WRONG_ANSWERS);
		array.add(CORRECT_ANSWER);
		return array.toArray(new String[0]);
	}
	
}