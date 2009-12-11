package uk.ac.osswatch.simal.ssmm.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

/**
 * A question that allows zero or more options to be selected from a range of options.
 *
 */
public class SelectionQuestion extends MultipleChoiceQuestion {
	
	LinkedHashMap<String, String> selectedOptions = new LinkedHashMap<String, String>();

	public SelectionQuestion(String label, String text, String details,
			LinkedHashMap<String, String> options) {
		super(label, text, details, options);
	}

	/**
	 * Set a single answer as selected. Previous answers that have been set remain selected.
	 * 
	 * @TODO throw an exception on an illegal option
	 */
	@Override
	public void setAnswer(int index) {
		LinkedHashMap<String, String> selected = getSelectedOptions();
		String key = (String)getOptions().keySet().toArray()[index];
		String value = (String)getOptions().values().toArray()[index];
		selected.put(key, value);
		setSelectedOptions(selected);
	}
	
	/**
	 * Set an answer as unselected, all other selections remain selected.
	 * @param index
	 */
	public void unsetAnswer(int index) {
		super.setAnswer(index);
	}

	/**
	 * Get a comma separated list of keys for options that are selected.
	 */
	@Override
	public String getAnswer() {
		if (getSelectedOptions().size() == 0) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator<String> itr = getSelectedOptions().keySet().iterator();
			while (itr.hasNext()) {
				sb.append(itr.next());
				if (itr.hasNext()) {
					sb.append(", ");
				}
			}
			return sb.toString();
		}
	}

	/**
	 * Set all selected options using a comma separated list of options.
	 * If any of the items in the list are not keys for an option then they
	 * are silently ignored. 
	 * 
	 * Previously selected items will remain selected.
	 * 
	 * @TODO throw an exception on an illegal option
	 */
	@Override
	public void setAnswer(String answer) {
		StringTokenizer st = new StringTokenizer(answer, ",");
		while (st.hasMoreTokens()) {
			String key = st.nextToken();
			LinkedHashMap<String, String> selected = getSelectedOptions();
			if (!selected.containsKey(key)) {
				if (getOptions().containsKey(key)) {
				  selected.put(key, getOptions().get(key));
				}
			}
		}
	}

	/**
	 * Get a linked map containing all selected options.
	 * 
	 * @return
	 */
	public LinkedHashMap<String, String> getSelectedOptions() {
		return selectedOptions;
	}

	/**
	 * Set the selected options with a linked map containing all selections.
	 * @return
	 */
	public void setSelectedOptions(LinkedHashMap<String, String> selected) {
		selectedOptions = selected;
	}
}
