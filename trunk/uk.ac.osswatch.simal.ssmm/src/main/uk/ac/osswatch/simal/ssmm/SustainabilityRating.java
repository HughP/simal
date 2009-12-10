package uk.ac.osswatch.simal.ssmm;

import java.util.LinkedHashMap;
import java.util.Scanner;

import uk.ac.osswatch.simal.ssmm.model.Question;

public class SustainabilityRating {

  private static final Object HELP_COMMAND = "help";
  private static LinkedHashMap<String, Question> questions = new LinkedHashMap<String, Question>();

	/**
	   * @param args
	   */
	  public static void main(String[] args) {
		  System.out.println("Please answer the following questions.");
		  System.out.println("If you want more details type '" + HELP_COMMAND + "'");
		  System.out.println("If you are unsure of the answer simply press enter");
		  
		  Question question = new Question("Project name", "What is the project name?", "Please provide a project name.");
		  questions.put(question.getLabel(), question);
		  String answer = askQuestion(question);
		  System.out.println("Name: " + answer);
	  }

	/**
	 * Ask a question and return the answer. The answer is also stored in the question
	 * object.
	 * 
	 * @param question
	 * @return
	 */
	private static String askQuestion(Question question) {
		System.out.println("\n");
		System.out.println(question.getLabel());
		for (int i = 0; i < question.getLabel().length(); i++) {
			System.out.print("=");
		}
		System.out.println("\n");
		System.out.println(question.getText());
		Scanner in = new Scanner(System.in);
		String answer = in.nextLine();
		if (answer.equals(HELP_COMMAND)) {
			System.out.println(question.getDetails());
			return askQuestion(question);
		}
		question.setAnswer(answer);
		return question.getAnswer();
	}
}
