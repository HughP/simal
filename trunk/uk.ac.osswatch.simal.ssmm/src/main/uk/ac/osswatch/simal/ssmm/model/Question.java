package uk.ac.osswatch.simal.ssmm.model;

/**
 * A base question type that allows a free text answer.
 *
 */
public class Question {
	String text;
	String details;
	String answer;
	private String label;
	
	/**
	 * Create a default question that accepts a text response. 
	 * 
	 * @param label a human readable label for the question
	 * @param text - the text of the question
	 * @param details - a description of the question
	 * @param answer - a default answer
	 */
	public Question(String label, String text, String details,
			String answer) {
		setLabel(label);
		setText(text);
		setDetails(details);
		setAnswer(answer);
	}
	
	/**
	 * Create a default question that accepts a text response.
	 * 
	 * @param label a human readable label for the question
	 * @param text the text of the question
	 * @param details a description of the question
	 */
	public Question(String label, String text, String details) {
		setLabel(label);
		setText(text);
		setDetails(details);
	}

	/**
	 * Get the text of the question.
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the text of the question.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Get the descriptive details of the question. This can, optionally, contain HTML. 
	 * @return
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Set the descriptive details of the question. This can, optionally, contain HTML. 
	 */
	public void setDetails(String details) {
		this.details = details;
	}
	
	/**
	 * Get the answer (if any) provided for this question.
	 * @return
	 */
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Set the answer (if any) provided for this question.
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * Get a human readable label for this question.
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set a human readable label for this question.
	 * @return
	 */
	public void setLabel(String newLabel) {
		this.label = newLabel;
	}

}
