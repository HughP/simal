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

import uk.ac.osswatch.simal.ssmm.model.Question;
import uk.ac.osswatch.simal.ssmm.model.QuestionSet;

public class InfoQuestions extends QuestionSet {

	public InfoQuestions() {
		Question question = new Question("Project name", "What is the project name?", "Please provide a project name.");
		put(question.getLabel(), question);
        question = new Question("Project URL", "What is the URL for the project?", "Please provide a project URL.");
	    put(question.getLabel(), question);
	    question = new Question("Contact Name", "What is your name?", "It is useful for us to thave the name of the person completing this set of questions in case we need to talk to the project about your responses.");
	    put(question.getLabel(), question);
	    question = new Question("Contact EMail", "What is your email address?", "It is useful for us to thave your email so that we can keep you informed about updates to this set of questions.");
	    put(question.getLabel(), question);
	}

}
