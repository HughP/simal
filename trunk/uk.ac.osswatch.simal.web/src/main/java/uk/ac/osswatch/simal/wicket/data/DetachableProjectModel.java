package uk.ac.osswatch.simal.wicket.data;

import javax.xml.namespace.QName;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DetachableProjectModel extends LoadableDetachableModel implements
		IModel {
	private static final long serialVersionUID = -9017519516676203598L;
	QName qname;

	public DetachableProjectModel(String qname) {
		this.qname = new QName(qname);
	}

	public DetachableProjectModel(QName qname) {
		this.qname = qname;
	}

	public DetachableProjectModel(Project project) {
		this(project.getQName());
	}

	@Override
	protected Object load() {
		Project project;
		try {
			project = SimalRepository.getProject(qname);
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			project = null;
		}
		return project;
	}

}
