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
		
		options.clear();
		options.put("Don't know", "It is not fully understood who has the right to run the software");
		options.put("Specific Group", "Only some specified group may run the software (i.e. restricted exclusively or proprietary) – e.g. free for education only.");
		options.put("Specific Use", "Anyone but for some specific purpose or but some specific group (i.e. restricted inclusively) – e.g. no commercial use.");
		options.put("Anyone", "Anyone may run the software.");
		question = new MultipleChoiceQuestion("Permission to run", "Who has permission to run the software?", "If the right to run the software is limited, that limits the recipient base of the software and thus openness is limited.", options);
		put(question.getLabel(), question);
		
		options.clear();
		options.put("Don't know", "The documented dependencies have not been verified independently");
		options.put("No", "There is at least one dependency that has not been correctly documented");
		options.put("Yes, no audit process", "Yes, but the audit process is undocumented. This means that undocumented dependencies may be introduced at any time.");
		options.put("Yes, separate audit process", "Yes, and there is a documented audit process. However, this process is not integrated with the release management process");
		options.put("Yes, integrated audit process", "Yes, there is a documented audit process which is an integrated part of the release management process.");
		question = new MultipleChoiceQuestion("Documented dependencies", "Are all project dependencies clearly documented and licence compatibilities audited?", "Crediting dependencies and conforming to their licence requirements is critical to ensuring the project conforms to all legal requirements placed on a FOSS project.", options);
		put(question.getLabel(), question);
		
		// FIXME: done up to , and including, Legal Q2 in http://pipka.org/misc/Foundations-of-openness-V2-release.odt
		// Q3 in http://spreadsheets.google.com/ccc?key=0AsvIGTEZElqhdGFMQi1qendNTFNtc184NFhxNFVaOUE&hl=en
	}

}
