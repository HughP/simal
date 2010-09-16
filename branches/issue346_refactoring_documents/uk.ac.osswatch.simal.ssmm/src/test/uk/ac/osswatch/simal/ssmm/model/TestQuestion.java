package uk.ac.osswatch.simal.ssmm.model;
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

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test base functions of the Question class.
 *
 */
public class TestQuestion {

	static String QUESTION_LABEL = "Test Label";
	static String QUESTION_TEXT = "Is this a question?";
	static String QUESTION_DETAILS = "This is just a question for testing.";
	static String QUESTION_ANSWER = "Yes, of course it's a question.";
	
	static Question question;
	
	@BeforeClass
	public static void createQuestion() {
		question = new Question(QUESTION_LABEL, QUESTION_TEXT, QUESTION_DETAILS, QUESTION_ANSWER);
	}
	
	@Test
	public void testCreateQuestion(){
		Question test = new Question(QUESTION_LABEL, QUESTION_TEXT, QUESTION_DETAILS);
		assertEquals("Question text is incorrect", QUESTION_TEXT, test.getText());
		assertEquals("Description for question is not being set correctly", QUESTION_DETAILS, test.getDetails());
	}

	@Test
	public void testGetLabel() {
		assertEquals("Question label is incorrect", QUESTION_LABEL, question.getLabel());
	}
	
	@Test
	public void testSetLabel() {
		question.setLabel("testing");
		assertEquals("Question label is not being set correctly", "testing", question.getLabel());
		question.setLabel(QUESTION_LABEL);
	}
	
	@Test
	public void testGetText() {
		assertEquals("Question text is incorrect", QUESTION_TEXT, question.getText());
	}
	
	@Test
	public void testSetText() {
		question.setText("testing");
		assertEquals("Question text is not being set correctly", "testing", question.getText());
		question.setText(QUESTION_TEXT);
	}
	
	@Test
	public void testGetAnswer() {
		assertEquals("Answer to question is incorrect", QUESTION_ANSWER, question.getAnswer());
	}

	@Test
	public void testSetAnswer() {
		question.setAnswer("testing");
		assertEquals("Answer to question is not being set correctly", "testing", question.getAnswer());
		question.setAnswer(QUESTION_ANSWER);
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("Description for question is incorrect", QUESTION_DETAILS, question.getDetails());
	}
	
	@Test
	public void testSetDescription() {
		question.setDetails("testing");
		assertEquals("Description for question is not being set correctly", "testing", question.getDetails());
		question.setDetails(QUESTION_DETAILS);
	}
}
