package uk.ac.osswatch.simal.ssmm.model;

import java.util.Collection;
import java.util.LinkedHashMap;
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

public class QuestionSet {
	private LinkedHashMap<String, Question>  questions = new LinkedHashMap<String, Question>();
	
	/**
	 * Add a new question to this set. It will be placed at the end of the current set of questions.
	 * 
	 * @param id - a unique identifier for this question, usually this will be one or two words
	 * @param question - the full text of the question
	 */
	public void put(String id, Question question) {
		if (questions.containsKey(id)) throw new IllegalArgumentException("Attempt to add a questoin with a duplicate identifier: " + id);
		questions.put(id, question);
	}

	/**
	 * Return a collection containing all the questions in this set.
	 * @return
	 */
	public Collection<Question> values() {
		return questions.values();
	}

}
