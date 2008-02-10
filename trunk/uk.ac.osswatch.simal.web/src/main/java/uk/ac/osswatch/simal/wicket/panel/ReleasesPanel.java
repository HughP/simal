package uk.ac.osswatch.simal.wicket.panel;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import uk.ac.osswatch.simal.model.IVersion;
import uk.ac.osswatch.simal.model.elmo.ProjectException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A simple panel providing details of a set of releases.
 */
public class ReleasesPanel extends Panel {
	private static final long serialVersionUID = 3993329475348185480L;

	public ReleasesPanel(String panelId, Set<IVersion> releases)
			throws SimalRepositoryException {
		super(panelId);
		populatePage(releases);
	}

	private void populatePage(Set<IVersion> releases)
			throws SimalRepositoryException {
		Iterator<IVersion> itr = releases.iterator();
		RepeatingView repeating = new RepeatingView("releases");
		WebMarkupContainer item;
		IVersion release;
		Label label;
		while (itr.hasNext()) {
			item = new WebMarkupContainer(repeating.newChildId());
			repeating.add(item);
			release = itr.next();
			try {
				label = new Label("name", release.getName());
			} catch (ProjectException e) {
				throw new SimalRepositoryException("Unable to get release name", e);
			}
			item.add(label);
			item.add(getRepeatingLabels("revisions", "revision", release.getRevisions()));
			label = new Label("created", release.getCreated().toString());
			item.add(label);
		}
		add(repeating);
	}
	


	/**
	 * Get a simple repeating view. Each resource in the supplied set will be
	 * represented in the list using the supplied string as a label.
	 * 
	 * @param repeaterWicketID
	 *            the wicket:id of the HTML component
	 * @param labelWicketID
	 *            the wicket:id of the label within each list item
	 * @param resources
	 *            the resources to be added to the list
	 * @return
	 */
	protected RepeatingView getRepeatingLabels(String repeaterWicketID, String labelWicketID, Set<String> labels) {
		Iterator<String> itr = labels.iterator();
		RepeatingView repeating = new RepeatingView(repeaterWicketID);
		WebMarkupContainer item;
		String label;
		while (itr.hasNext()) {
			item = new WebMarkupContainer(repeating.newChildId());
			repeating.add(item);
			label = itr.next();
			item.add(new Label(labelWicketID, label));
		}
		return repeating;
	}

}
