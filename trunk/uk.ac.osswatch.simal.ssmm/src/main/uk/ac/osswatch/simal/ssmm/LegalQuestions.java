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

import uk.ac.osswatch.simal.ssmm.model.MultipleChoiceQuestion;
import uk.ac.osswatch.simal.ssmm.model.QuestionSet;

public class LegalQuestions extends QuestionSet {

	public LegalQuestions() {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("Don't know", "The licence model is not currently understood.");
		options.put("Proprietary", "The licence used is a proprietary licence that has not been recognised by either the Free Software Foundation or the Open source Initiative");
		options.put("OSI", "The licence used has been approved by the Open Source Initiative.");
		options.put("FSF", "The licence has been recognised by the Free Software Foundation as a Free Software License");
		options.put("FSF and OSI", "The license is recognised by the FSF and by the OSI.");

	    MultipleChoiceQuestion question = new MultipleChoiceQuestion("License type", "Is the licence recognised as a common Free and Open Source licences?", "If the licence has been recognised by either of these bodies, it is more likely to have been assessed and found to be relatively open than a new licence or one which has not been OSI or FSF approved.", options);
		put(question.getLabel(), question);
	}

}
