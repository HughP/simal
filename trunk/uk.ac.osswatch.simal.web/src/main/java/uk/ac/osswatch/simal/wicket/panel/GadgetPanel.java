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
  private String appID;
   
	
	/**
	 * Create a gadget panel.
	 * 
	 * @param wicketID - the wicket ID for the panel
	 * @param specURL - the specification of the gadget
	 * @param appID - the application ID to use in open social tokens
	 */
	public GadgetPanel(String wicketID, String specURL, String appID) {
		super(wicketID);
		this.specURL = specURL;
		uid = new UID();
		setAppID(appID);
		
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


  /**
   * Get the gadget specification URL
   * @return
   */
  public String getSpecURL() {
    return specURL;
  }


  /**
   * Get the application identifier to be used in open social tokens.
   * @return
   */
  public String getAppID() {
    return appID;
  }


  /**
   * Set the application identifier to be used in open social tokens.
   * @param appID
   */
  public void setAppID(String appID) {
    this.appID = appID;
  }

}

