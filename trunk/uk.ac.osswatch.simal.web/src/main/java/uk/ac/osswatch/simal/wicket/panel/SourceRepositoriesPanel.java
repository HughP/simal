package uk.ac.osswatch.simal.wicket.panel;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import uk.ac.osswatch.simal.model.IRCS;

/**
 * A panel to display one or more Source Repository details.
 */
public class SourceRepositoriesPanel extends Panel {
	private static final long serialVersionUID = -2031486948152653715L;

	public SourceRepositoriesPanel(String panelId, Set<IRCS> repositories) {
		super(panelId);
		populatePage(repositories);
	}

	private void populatePage(Set<IRCS> repositories) {
		add(getRepeatingLinks(repositories));
	}

	/**
	 * Get a simple repeating view. Each resource is represented by a link to
	 * the URL contained in rdf:resource. the label for the link is either
	 * defined by rdfs:label, or the defaultLabel property, or the value of
	 * rdf:resource.
	 * 
	 * @param repeaterWicketID
	 *            the wicket:id of the HTML component
	 * @param linkWicketID
	 *            the wicket:id of the link within each list
	 * @param defaultLabel
	 *            the default value to use for the links label. If the resource
	 *            has an rdfs:label value that will be used, otherwise this
	 *            property will be used. If this property is null then the value
	 *            of rdfs:resource is used.
	 * @param resources
	 *            the resources to be added to the list
	 * @return
	 */
	protected RepeatingView getRepeatingLinks(Set<IRCS> repositories) {
		Iterator<IRCS> itr = repositories.iterator();
		RepeatingView repeating = new RepeatingView("sourceRepositories");
		WebMarkupContainer item;
		IRCS repository;
		while (itr.hasNext()) {
			item = new WebMarkupContainer(repeating.newChildId());
			repeating.add(item);
			repository = itr.next();

			item.add(getRepeatingLinks("anonRoots", "anonLink", repository
					.getAnonRoots()));
			item.add(getRepeatingLinks("devLocations", "devLink", repository
					.getLocations()));
			item.add(getRepeatingLinks("browseRoots", "browseLink", repository
					.getLocations()));
		}
		return repeating;
	}

	/**
	 * Get a simple repeating view. Each resource is represented by a link to
	 * the URL contained in rdf:resource. the label for the link is either
	 * defined by rdfs:label, or the defaultLabel property, or the value of
	 * rdf:resource.
	 * 
	 * @param repeaterWicketID
	 *            the wicket:id of the HTML component
	 * @param linkWicketID
	 *            the wicket:id of the link within each list
	 * @param resources
	 *            the resources to be added to the list
	 * @return
	 */
	protected RepeatingView getRepeatingLinks(String repeaterWicketID,
			String linkWicketID, Set<String> urls) {
		Iterator<String> itr = urls.iterator();
		RepeatingView repeating = new RepeatingView(repeaterWicketID);
		WebMarkupContainer item;
		String url;
		ExternalLink link;
		while (itr.hasNext()) {
			item = new WebMarkupContainer(repeating.newChildId());
			repeating.add(item);
			url = itr.next();
			link = new ExternalLink(linkWicketID, url);
			link.add(new Label("label", url));
			item.add(link);
		}
		return repeating;
	}
}
