package uk.ac.osswatch.simal.ssmm.model;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Iterator;

public class TestSelectionQuestion {
	static LinkedHashMap<String, String> options;
	static SelectionQuestion question;
	
	@BeforeClass
	public static void createQuestion() {
		options = new LinkedHashMap<String, String>();
		options.put("One", "Option one");
		options.put("Two", "Option two");
		options.put("Three", "Option three");
		options.put("Four", "Option four");
		
		question = new SelectionQuestion(TestQuestion.QUESTION_LABEL, TestQuestion.QUESTION_TEXT, TestQuestion.QUESTION_DETAILS, options);
	}

	@Test
	public void testOptionsSize() {
		assertEquals("There is an incorrect number of options", options.size(), question.getOptions().size());
	}
	
	@Test
	public void testOptionsOrder() {
		Iterator<String> itr = question.getOptions().keySet().iterator();
		String key = itr.next();
		assertEquals("First key is incorrect", "One", key);
		key = itr.next();
		assertEquals("Two key is incorrect", "Two", key);
		key = itr.next();
		assertEquals("Three key is incorrect", "Three", key);
		key = itr.next();
		assertEquals("Four key is incorrect", "Four", key);
	}
	
	@Test
	public void testAddRemoveOption() {
		int originalSize = question.getOptions().size();
		question.addOption("Five", "Option five");
		assertEquals("There is an incorrect number of options after adding an option", originalSize + 1, question.getOptions().size());
		
		question.removeOption("Five");
		assertEquals("There is an incorrect number of options after removing an option", originalSize, question.getOptions().size());
	}
	
	@Test
	public void testAnswer() {
		question.setAnswer(1);
		assertEquals("The answer is not correctly set when using an integer index", "Two", question.getAnswer());
		
		question.setAnswer(2);
		assertEquals("The answer is not correctly set when using a second integer index", "Two, Three", question.getAnswer());
		

		question.setAnswer("One, Two");
		assertEquals("The answer is not correctly set when using a second integer index", "Two, Three, One", question.getAnswer());
	}
	
}
