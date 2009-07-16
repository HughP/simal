package uk.ac.osswatch.simal.model.jcr;

import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class MailingList extends DoapResource implements IDoapMailingList {
	private static final long serialVersionUID = 1L;

	public MailingList() {
		super();
	}
	
	public MailingList(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

}
