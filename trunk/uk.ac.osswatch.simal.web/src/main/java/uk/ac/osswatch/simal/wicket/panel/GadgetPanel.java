package uk.ac.osswatch.simal.wicket.panel;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * A panel to display a Google Gadget (or Open Social gadget).
 */
public class GadgetPanel extends Panel {
	private static final long serialVersionUID = -5887625691251320087L;
    private static final String SERVER_BASE = "http://localhost:8080/";
    private static final String FILES_DIR = "container/";
    private static final CompressedResourceReference GADGET_CSS = new CompressedResourceReference(GadgetPanel.class, "gadget.css");
    
	/**
	 * Create a gadget panel.
	 * 
	 * @param id - the wicket ID for the panel
	 * @param specURL - the specification of the gadget
	 */
	public GadgetPanel(String id, String specURL) {
		super(id);
		
		add(HeaderContributor.forCss(GADGET_CSS));
		add(HeaderContributor
				.forJavaScript(SERVER_BASE + "js/rpc.js?c=1"));
		add(HeaderContributor
				.forJavaScript(SERVER_BASE + FILES_DIR + "cookies.js"));
		add(HeaderContributor
				.forJavaScript(SERVER_BASE + FILES_DIR + "gadgets.js"));
		add(new StringHeaderContributor("<script type=\"text/javascript\">var specUrl = '" + specURL + "'; var gadgetServerBase = '" + SERVER_BASE + "';</script>"));
	}

}

