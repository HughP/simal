package uk.ac.osswatch.simal.wicket;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.repeater.RepeatingView;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.elmo.Resource;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;

/**
 * The base page for a standard simal web page. It contains the
 * standard markup for the site, such as headers and footers.
 * All pages in the Simal site should extend this page.
 */
public class BasePage extends WebPage {
	private static final long serialVersionUID = 7789964685286908825L;
	private static final CompressedResourceReference DEFAULT_CSS = new CompressedResourceReference(UserApplication.class, "default.css");
	
	public BasePage() {
		add(HeaderContributor.forCss( DEFAULT_CSS ));
		add(new BookmarkablePageLink("exhibitBrowserLink", ExhibitProjectBrowserPage.class));
		add(new Label("footer", "This is in the footer"));
	}

	/**
	 * Get a simple repeating view. Each resource is represented by
	 * a link to the URL contained in rdf:resource. the label for the
	 * link is either defined by rdfs:label, or the defaultLabel
	 * property, or the value of rdf:resource.
	 * 
	 * @param repeaterWicketID
	 *            the wicket:id of the HTML component
	 * @param linkWicketID
	 *            the wicket:id of the link within each list
	 * @param defaultLabel
	 *            the default value to use for the links label. If
	 *            the resource has an rdfs:label value that will be
	 *            used, otherwise this property will be used. If this
	 *            property is null then the value of rdfs:resource is
	 *            used.
	 * @param resources
	 *            the resources to be added to the list
	 * @param fetchLabels
	 *            If set to true attempt to fetch a label from
	 *            the repository if one is not supplied by the resource.
	 *            If set to false the label will be set to either the defaultLabel
	 *            (if not null) or to the resource URI.
	 * @return
	 */
	protected RepeatingView getRepeatingLinks(String repeaterWicketID, String linkWicketID, String defaultLabel,
			Set<Resource> resources, boolean fetchLabels) {
				Iterator<Resource> itr = resources.iterator();
				RepeatingView repeating = new RepeatingView(repeaterWicketID);
				WebMarkupContainer item;
				Resource resource;
				String label;
				String comment;
				String url;
				ExternalLink link;
				while (itr.hasNext()) {
					item = new WebMarkupContainer(repeating.newChildId());
					repeating.add(item);
					resource = itr.next();
					comment = resource.getComment();
					url = resource.toString();
					label = resource.getLabel(defaultLabel, fetchLabels);
					link = new ExternalLink(linkWicketID, url);
					link.add(new Label("label", label));
					if (comment != null) {
						link.add(new SimpleAttributeModifier("alt", comment));
					}
					item.add(link);
				}
				return repeating;
			}

    /**
	 * Get a simple repeating view. Each resource is represented by
	 * a link to the URL contained in rdf:resource. the label for the
	 * link is either defined by rdfs:label, or if no such value
	 * is defined, the value of rdf:resource.
	 * 
	 * @param repeaterWicketID
	 *            the wicket:id of the HTML component
	 * @param linkWicketID
	 *            the wicket:id of the link within each list
	 * @param resources
	 *            the resources to be added to the list
	 * @return
	 */
	protected RepeatingView getRepeatingLinks(String repeaterWicketID, String linkWicketID, Set<Resource> resources, boolean fetchLabels) {
		return getRepeatingLinks(repeaterWicketID, linkWicketID, null, resources, fetchLabels);
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

	/**
	 * Get a simple repeating view displaying a number of person summary panels.
	 * 
	 * @param repeaterWicketID the wicket ID of the repeating view
	 * @param personWicketID the wicket ID for the repeating person panel
	 * @param people the set of people to display in this repeating view
	 * @return
	 */
	protected RepeatingView getRepeatingPersonPanel(String repeaterWicketID, String personWicketID,
			Set<IPerson> people) {
		Iterator<IPerson> itr = people.iterator();
		RepeatingView repeating = new RepeatingView(repeaterWicketID);
		WebMarkupContainer item;
		IPerson person;
		while (itr.hasNext()) {
			item = new WebMarkupContainer(repeating.newChildId());
			repeating.add(item);
			person = itr.next();
			item.add(new PersonSummaryPanel(personWicketID, person));
		}
		return repeating;
	}
}

