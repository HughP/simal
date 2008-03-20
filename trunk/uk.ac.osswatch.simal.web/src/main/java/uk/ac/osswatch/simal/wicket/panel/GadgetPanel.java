package uk.ac.osswatch.simal.wicket.panel;

import java.rmi.server.UID;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * A panel to display a Google Gadget (or Open Social gadget).
 */
public class GadgetPanel extends Panel {
	private static final long serialVersionUID = -5887625691251320087L;
  private UID uid;
  private String specURL;
   
	
	/**
	 * Create a gadget panel.
	 * 
	 * @param wicketID - the wicket ID for the panel
	 * @param specURL - the specification of the gadget
	 */
	public GadgetPanel(String wicketID, String specURL) {
		super(wicketID);
		this.specURL = specURL;
		uid = new UID();
		
		AttributeAppender attr = new AttributeAppender("id", new Model(uid.toString()), " ");
		WebMarkupContainer container = new WebMarkupContainer("gadget");
		container.add(attr);
		add(container);
	}


	/**
	 * Get a unique identifier for this gadget.
	 * @return
	 */
  public String getUid() {
    return uid.toString();
  }


  public String getSpecURL() {
    return specURL;
  }

}

