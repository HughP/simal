package uk.ac.osswatch.simal.ssmm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

import uk.ac.osswatch.simal.ssmm.model.Question;

public class SustainabilityRating {

  private static final Object HELP_COMMAND = "help";
  private static LinkedHashMap<String, Question> infoQuestions = new LinkedHashMap<String, Question>();
  private static LinkedHashMap<String, Question> legalQuestions = new LinkedHashMap<String, Question>();
	/**
	   * @param args
	   */
	  public static void main(String[] args) {
		  System.out.println("Please answer the following questions.");
		  System.out.println("If you want more details type '" + HELP_COMMAND + "'");
		  System.out.println("If you are unsure of the answer simply press enter");
		  
		  askInfoQuestions();
		  askLegalQuestions();
		  
		  reportAll(infoQuestions);
		  reportAll(legalQuestions);
	  }

	private static void askInfoQuestions() {
  	    System.out.println("General Information");
	    System.out.println("===================");
	    
		Question question = new Question("Project name", "What is the project name?", "Please provide a project name.");
		infoQuestions.put(question.getLabel(), question);
        question = new Question("Project URL", "What is the URL for the project?", "Please provide a project URL.");
	    infoQuestions.put(question.getLabel(), question);
	     
	    askAll(infoQuestions);
	}
	
	private static void askLegalQuestions() {
  	    System.out.println("General Information");
	    System.out.println("===================");

	    Question question = new Question("License type", "Is the licence recognised as a common Free and Open Source licences?", "If the licence has been recognised by either of these bodies, it is more likely to have been assessed and found to be relatively open than a new licence  or one which has not been OSI or FSF approved.");
		legalQuestions.put(question.getLabel(), question);
          
	    askAll(legalQuestions);
	}

	/**
	 * Ask all the questions in a set.
	 * 
	 * @param questions the questions to ask
	 */
	private static void askAll(LinkedHashMap<String, Question> questions) {
		Iterator<Question> itr = questions.values().iterator();
		while (itr.hasNext()) {
			Question question = itr.next();
			ask(question);
		}
	}
	
	/**
	 * Report the response to all the questions in a set.
	 * @param questions
	 */
	private static void reportAll(LinkedHashMap<String, Question> questions) {
		System.out.println("\nResponse summary\n");
		Iterator<Question> itr = questions.values().iterator();
		while (itr.hasNext()) {
			Question question = itr.next();
			System.out.print(question.getLabel());
			System.out.print(": ");
			System.out.println(question.getAnswer());
		}
	}

	/**
	 * Ask a question and return the answer. The answer is also stored in the question
	 * object.
	 * 
	 * @param question
	 * @return
	 */
	private static String ask(Question question) {
		System.out.println("\n");
		printHeading(question);
		System.out.println(question.getText());
		Scanner in = new Scanner(System.in);
		String answer = in.nextLine();
		if (answer.equals(HELP_COMMAND)) {
			System.out.println(question.getDetails());
			return ask(question);
		}
		question.setAnswer(answer);
		return question.getAnswer();
	}

	private static void printHeading(Question question) {
		System.out.println(question.getLabel());
		for (int i = 0; i < question.getLabel().length(); i++) {
			System.out.print("-");
		}
		System.out.println("\n");
	}
}
