package uk.ac.osswatch.simal.ssmm;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import java.util.Iterator;
import java.util.Scanner;

import uk.ac.osswatch.simal.ssmm.model.MultipleChoiceQuestion;
import uk.ac.osswatch.simal.ssmm.model.Question;
import uk.ac.osswatch.simal.ssmm.model.QuestionSet;
import uk.ac.osswatch.simal.ssmm.model.SelectionQuestion;

public class SustainabilityRating {

  private static final Object HELP_COMMAND = "help";
  private static InfoQuestions infoQuestions = new InfoQuestions();
  private static LegalQuestions legalQuestions = new LegalQuestions();
  private static KnowledgeQuestions knowledgeQuestions = new KnowledgeQuestions();
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
	    askAll(infoQuestions);
	}
	
	private static void askKnowledgeQuestions() {
  	    System.out.println("Knowledge Information");
	    System.out.println("=====================");
	    askAll(knowledgeQuestions);
	}
	
	private static void askLegalQuestions() {
  	    System.out.println("Legal Information");
	    System.out.println("=================");
	    askAll(legalQuestions);
	}

	/**
	 * Ask all the questions in a set.
	 * 
	 * @param infoQuestions2 the questions to ask
	 */
	private static void askAll(QuestionSet infoQuestions2) {
		Iterator<Question> itr = infoQuestions2.values().iterator();
		while (itr.hasNext()) {
			Question question = itr.next();
			ask(question);
		}
	}
	
	/**
	 * Report the response to all the questions in a set.
	 * @param infoQuestions2
	 */
	private static void reportAll(QuestionSet infoQuestions2) {
		Iterator<Question> itr = infoQuestions2.values().iterator();
		while (itr.hasNext()) {
			Question question = itr.next();
			System.out.print(question.getLabel());
			System.out.print(": ");
			System.out.println(question.getAnswer());
		}
	}

	/**
	 * Ask a question and return the answer. The answer is also stored in the question
	 * object for later retrieval.
	 * 
	 * @param question
	 * @return
	 */
	private static String ask(Question question) {
		System.out.println("\n");
		printHeading(question);
		System.out.println(question.getText());
		
		if (question instanceof SelectionQuestion) {
			getSelectionResponse((SelectionQuestion) question);
		} else if (question instanceof MultipleChoiceQuestion) {
			getMultiChoiceResponse((MultipleChoiceQuestion) question);
		} else {
		    getFreeFormresponse(question);
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
