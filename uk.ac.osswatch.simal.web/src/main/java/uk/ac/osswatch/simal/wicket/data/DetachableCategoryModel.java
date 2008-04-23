package uk.ac.osswatch.simal.wicket.data;

import javax.xml.namespace.QName;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class DetachableCategoryModel extends LoadableDetachableModel implements
		IModel {
	private static final long serialVersionUID = -9017519516676203598L;
	QName qname;

	public DetachableCategoryModel(IDoapCategory category) {
		this.qname = category.getQName();
	}

	public DetachableCategoryModel(QName qname) {
		this.qname = qname;
	}

	@Override
	protected Object load() {
		IDoapCategory category;
		try {
			category = UserApplication.getRepository().findCategory(qname);
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			category= null;
		}
		return category;
	}

}
