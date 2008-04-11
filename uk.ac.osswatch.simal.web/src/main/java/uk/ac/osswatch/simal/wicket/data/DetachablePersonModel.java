package uk.ac.osswatch.simal.wicket.data;

import javax.xml.namespace.QName;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class DetachablePersonModel extends LoadableDetachableModel implements
		IModel {
	private static final long serialVersionUID = -9017519516676203598L;
	QName qname;

	public DetachablePersonModel(IPerson person) {
		this.qname = person.getQName();
	}

	public DetachablePersonModel(QName qname) {
		this.qname = qname;
	}

	@Override
	protected Object load() {
		IPerson person;
		try {
			person = UserApplication.getRepository().getPerson(qname);
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			person = null;
		}
		return person;
	}

}
