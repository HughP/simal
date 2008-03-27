package uk.ac.osswatch.simal.wicket.data;

import javax.xml.namespace.QName;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class DetachableProjectModel extends LoadableDetachableModel implements
		IModel {
	private static final long serialVersionUID = -9017519516676203598L;
	QName qname;

	public DetachableProjectModel(IProject project) {
		this.qname = project.getQName();
	}

	public DetachableProjectModel(QName qname) {
		this.qname = qname;
	}

	@Override
	protected Object load() {
		IProject project;
		try {
			project = UserApplication.getRepository().getProject(qname);
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			project = null;
		}
		return project;
	}

}
