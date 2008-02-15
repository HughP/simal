package uk.ac.osswatch.simal.unitTest.model.elmo;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.model.elmo.Resource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class MockProject extends Project {
	private static final long serialVersionUID = 5055851775619447759L;
	private MockDOAPResource resource = new MockDOAPResource();
	
	public MockProject() {
		super();
	}
	
	public Set<Resource> getCategories() {
		HashSet<Resource> categories = new HashSet<Resource>();
		categories.add(new MockDOAPResource(new QName(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_ONE)));
		categories.add(new MockDOAPResource(new QName(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_TWO)));
		return categories;		
	}

	@Override
	public QName getQName() {
		return resource.getQName();
	}
	
	@Override
	public String getName() {
		return resource.getName();
	}
	
	@Override
	public String getShortDesc() {
		return resource.getShortDesc();
	}
	
	@Override
	public String getLabel(String defaultLabel) {
		return resource.getLabel(defaultLabel);
	}
	
	@Override
	public Set<IPerson> getMaintainers() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;		
	}

	@Override
	public Set<IPerson> getDevelopers() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;	
	}

	@Override
	public Set<IPerson> getDocumenters() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;	
	}

	@Override
	public Set<IPerson> getHelpers() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;	
	}

	@Override
	public Set<IPerson> getTesters() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;	
	}

	@Override
	public Set<IPerson> getTranslators() throws SimalRepositoryException {
		HashSet<IPerson> people = new HashSet<IPerson>();
		return people;	
	}
	
	@Override
	public Set<String> getProgrammingLangauges() {
		HashSet<String> languages = new HashSet<String>();
		languages.add(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE);
		languages.add(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO);
		return languages;
	}

}
