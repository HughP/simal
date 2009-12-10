package uk.ac.osswatch.simal.ssmm.model;

import java.util.LinkedHashMap;


public class MultipleChoiceQuestion extends Question {
	LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
	
	/**
	 * Create a multiple choice question.
	 * 
	 * @param label huamn readable label for the question
	 * @param text the text of the question
	 * @param details descriptive details about the question
	 * @param options the options available, where the key is used as a short hand for the question and the value is a description of the question.
	 */
	public MultipleChoiceQuestion(String label, String text, String details, LinkedHashMap<String, String> options) {
		super(label, text, details);
		this.options = options;
	}

	/**
	 * Get a map of options available in response to this question.
	 * 
	 * @return a hasmap keyed by the short version of the response. The value is a full description of the response.
	 */
	public LinkedHashMap<String, String> getOptions() {
		return options;
	}

	/**
	 * Set a map of options available to answer this question.
	 * 
	 * @param options a linked hashmap keyed by the short version of the response. The value is a full description of the response.
	 */
	public void setOptions(LinkedHashMap<String, String> options) {
		this.options = options;
	}

	/**
	 * Add an option to the possible answers.
	 * 
	 * @param option short version of the option
	 * @param description a full description of the option
	 */
	public void addOption(String option, String description) {
		options.put(option, description);
	}
	
	/**
	 * Remove an option to the possible answers.
	 *
	 * @param option short version of the option
	 */
	public void removeOption(String option) {
		options.remove(option);
	}

	/**
	 * Set the recorded answer according to an index pointer into the LinkedhashMap of options.
	 * 
	 * @param index 0 base index into the options
	 */
	public void setAnswer(int index) {
		setAnswer((String)getOptions().keySet().toArray()[index]);
	}

}
