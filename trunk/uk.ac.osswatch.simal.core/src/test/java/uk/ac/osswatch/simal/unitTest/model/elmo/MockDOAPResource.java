package uk.ac.osswatch.simal.unitTest.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.concepts.rdfs.Resource;

import uk.ac.osswatch.simal.integrationTest.model.elmo.AbstractTestDOAP;
import uk.ac.osswatch.simal.model.elmo.DoapResource;

public class MockDOAPResource extends DoapResource {
	private static final long serialVersionUID = -5174587733994913681L;
	private QName qname = new QName("http://example.org/testing/qname#");

	protected MockDOAPResource() {
		super();
	}

	protected MockDOAPResource(QName name) {
		qname = name;
	}

	@Override
	public String getCreated() {
		return super.getCreated();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return super.getDescription();
	}

	@Override
	public Set<String> getLicences() {
		// TODO Auto-generated method stub
		return super.getLicences();
	}

	@Override
	public String getName() {
		return AbstractTestDOAP.TEST_SIMAL_PROJECT_NAME;
	}

	@Override
	public QName getQName() {
		return qname;
	}

	@Override
	protected Set<String> getResourceURIs(Set<Resource> resources) {
		// TODO Auto-generated method stub
		return super.getResourceURIs(resources);
	}

	@Override
	public String getShortDesc() {
		return AbstractTestDOAP.TEST_SIMAL_PROJECT_SHORT_DESC;
	}
	
	@Override
	public String getLabel(String defaultLabel) {
		return getQName().toString();
	}
}
