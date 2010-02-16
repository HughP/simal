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

import java.util.LinkedHashMap;

import uk.ac.osswatch.simal.ssmm.model.QuestionSet;
import uk.ac.osswatch.simal.ssmm.model.SelectionQuestion;

public class KnowledgeQuestions extends QuestionSet {

	public KnowledgeQuestions() {
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
		put(question.getLabel(), question);
	}

}
