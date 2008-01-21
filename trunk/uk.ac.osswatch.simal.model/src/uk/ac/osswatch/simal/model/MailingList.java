package uk.ac.osswatch.simal.model;

public class MailingList {
	String resourceURL;
	String title;
	
	public MailingList() {};

	public MailingList(String title, String resourceURL) {
		setTitle(title);
		setResourceURL(resourceURL);
	}

	/**
	 * The URL at which more information can be found.
	 * 
	 * @return
	 */
	public String getResourceURL() {
		return resourceURL;
	}

	/**
	 * The URL at which more information can be found.
	 */
	public void setResourceURL(String url) {
		resourceURL = url;
	}

	/**
	 * The title of this mailing list.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The title of this mailing list.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
