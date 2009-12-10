package uk.ac.osswatch.simal.ssmm.model;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

public class TestMultipleChoiceQuestion {
	static HashMap<String, String> options;
	static MultipleChoiceQuestion question;
	
	@BeforeClass
	public static void createQuestion() {
		options = new HashMap<String, String>();
		options.put("One", "Option one");
		options.put("Two", "Option two");
		options.put("Three", "Option three");
		options.put("Four", "Option four");
		
		question = new MultipleChoiceQuestion(TestQuestion.QUESTION_LABEL, TestQuestion.QUESTION_TEXT, TestQuestion.QUESTION_DETAILS, options);
	}

	@Test
	public void testOptionsSize() {
		assertEquals("There is an incorrect number of options", options.size(), question.getOptions().size());
	}
	
	@Test
	public void testAddRemoveOption() {
		int originalSize = question.getOptions().size();
		question.addOption("Five", "Option five");
		assertEquals("There is an incorrect number of options after adding an option", originalSize + 1, question.getOptions().size());
		
		question.removeOption("Five");
		assertEquals("There is an incorrect number of options after removing an option", originalSize, question.getOptions().size());
	}
}
