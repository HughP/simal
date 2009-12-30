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

import java.util.LinkedHashMap;
import java.util.Iterator;

public class TestMultipleChoiceQuestion {
	static LinkedHashMap<String, String> options;
	static MultipleChoiceQuestion question;
	
	@BeforeClass
	public static void createQuestion() {
		options = new LinkedHashMap<String, String>();
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
	}
}
