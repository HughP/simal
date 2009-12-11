package uk.ac.osswatch.simal.ssmm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

import uk.ac.osswatch.simal.ssmm.model.MultipleChoiceQuestion;
import uk.ac.osswatch.simal.ssmm.model.Question;
import uk.ac.osswatch.simal.ssmm.model.SelectionQuestion;

public class SustainabilityRating {

  private static final Object HELP_COMMAND = "help";
  private static LinkedHashMap<String, Question> infoQuestions = new LinkedHashMap<String, Question>();
  private static LinkedHashMap<String, Question> legalQuestions = new LinkedHashMap<String, Question>();
  private static LinkedHashMap<String, Question> knowledgeQuestions = new LinkedHashMap<String, Question>();
	/**
	   * @param args
	   */
	  public static void main(String[] args) {
		  System.out.println("Please answer the following questions.");
		  System.out.println("If you want more details type '" + HELP_COMMAND + "'");
		  System.out.println("If you are unsure of the answer simply press enter");
		  
		  askInfoQuestions();
		  askLegalQuestions();
		  askKnowledgeQuestions();

		  System.out.println("\nResponse summary\n");  
		  reportAll(infoQuestions);
		  reportAll(legalQuestions);
		  reportAll(knowledgeQuestions);
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
	
	private static void askKnowledgeQuestions() {
  	    System.out.println("Knowledge Information");
	    System.out.println("=====================");

		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("User Docs", "User documentation section on website");
		options.put("Design Docs", "Design documentation");
		options.put("Roadmap", "Managed project roadmap");
		options.put("Metadata", "Machine readable meta-data");
		options.put("wiki", "Publicly writeable wiki");
		options.put("Revision control", "Version control system");
		options.put("Discussion", "Email lists or online forums");
		options.put("IM", "Instant messaging/IRC");
		options.put("Issue Tracker", "Issue tracker for bug and feature tracking");

	    SelectionQuestion question = new SelectionQuestion("Communication Channels", "Which publicly available communication or dissemination mechanisms does the project use?", "Multiple documentation and communication components are indicative of at least the opportunity for project knowledge to exist. There are certainly cases where too many avenues of knowledge can hurt a project.", options);
		knowledgeQuestions.put(question.getLabel(), question);
          
	    askAll(knowledgeQuestions);
	}
	
	private static void askLegalQuestions() {
  	    System.out.println("Legal Information");
	    System.out.println("=================");

		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("Don't know", "The licence model is not currently understood.");
		options.put("Proprietary", "The licence used is a proprietary licence that has not been recognised by either the Free Software Foundation or the Open source Initiative");
		options.put("OSI", "The licence used has been approved by the Open Source Initiative.");
		options.put("FSF", "The licence has been recognised by the Free Software Foundation as a Free Software License");
		options.put("FSF and OSI", "The license is recognised by the FSF and by the OSI.");

	    MultipleChoiceQuestion question = new MultipleChoiceQuestion("License type", "Is the licence recognised as a common Free and Open Source licences?", "If the licence has been recognised by either of these bodies, it is more likely to have been assessed and found to be relatively open than a new licence or one which has not been OSI or FSF approved.", options);
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
		String answer;
		
		if (question instanceof SelectionQuestion) {
			answer = getSelectionResponse((SelectionQuestion) question);
		} else if (question instanceof MultipleChoiceQuestion) {
			answer = getMultiChoiceResponse((MultipleChoiceQuestion) question);
		} else {
		    answer = getFreeFormresponse(question);
		}
		return question.getAnswer();
	}

	private static String getSelectionResponse(SelectionQuestion question) {
		System.out.println("Enter a single option at a time, enter 'end' to finish making selections");
		
		Iterator<String> keys = question.getOptions().keySet().iterator();
		int idx = 0;
		while (keys.hasNext()) {
			String key = keys.next();
			if (question.getSelectedOptions().containsKey(key)) {
				System.out.print("SELECTED ");
			}
			System.out.print(idx);
			System.out.print(": ");
			System.out.print(key);
			System.out.print(" (");
			System.out.print(question.getOptions().get(key));
			System.out.println(")");
			idx = idx + 1;
		}
		
		Scanner in = new Scanner(System.in);
		String response = in.nextLine();
		if (response.equals("end")) {
			return question.getAnswer();
		}
		if (response.equals(HELP_COMMAND)) {
			System.out.println(question.getDetails());
			return ask(question);
		}
		int intAnswer = new Integer(response);
		if (intAnswer < 1 || intAnswer >= idx) {
			System.out.println("Please enter a value between 0 and " + (idx - 1));
			return ask(question);
		}
		question.setAnswer(intAnswer);
		return getSelectionResponse(question);
	}

	/**
	 * Get the answer to a multichoice question.
	 * 
	 * @param question
	 * @return
	 */
	private static String getMultiChoiceResponse(MultipleChoiceQuestion question) {
		Iterator<String> keys = question.getOptions().keySet().iterator();
		int idx = 0;
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.print(idx);
			System.out.print(": ");
			System.out.print(key);
			System.out.print(" (");
			System.out.print(question.getOptions().get(key));
			System.out.println(")");
			idx = idx + 1;
		}
		
		Scanner in = new Scanner(System.in);
		String response = in.nextLine();
		if (response.equals(HELP_COMMAND)) {
			System.out.println(question.getDetails());
			return ask(question);
		}
		int intAnswer = new Integer(response);
		if (intAnswer < 1 || intAnswer >= idx) {
			System.out.println("Please enter a value between 0 and " + (idx - 1));
			return ask(question);
		}
		question.setAnswer(intAnswer);
		return question.getAnswer();
	}

	private static String getFreeFormresponse(Question question) {
		String answer;
		Scanner in = new Scanner(System.in);
		answer = in.nextLine();
		if (answer.equals(HELP_COMMAND)) {
			System.out.println(question.getDetails());
			return ask(question);
		}
		question.setAnswer(answer);
		return answer;
	}

	private static void printHeading(Question question) {
		System.out.println(question.getLabel());
		for (int i = 0; i < question.getLabel().length(); i++) {
			System.out.print("-");
		}
		System.out.println("\n");
	}
}
