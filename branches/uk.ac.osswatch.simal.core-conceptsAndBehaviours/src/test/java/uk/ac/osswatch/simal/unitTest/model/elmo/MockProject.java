package uk.ac.osswatch.simal.unitTest.model.elmo;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapResourceBehaviour;
import uk.ac.osswatch.simal.model.IFaofPersonBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class MockProject extends DoapProjectBehaviour {
  private static final long serialVersionUID = 5055851775619447759L;
  private MockDOAPResource resource = new MockDOAPResource();

  public MockProject() {
    super();
  }

  public Set<IDoapResourceBehaviour> getCategories() {
    HashSet<IDoapResourceBehaviour> categories = new HashSet<IDoapResourceBehaviour>();
    categories.add(new MockDOAPResource(new QName(
        BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_ONE)));
    categories.add(new MockDOAPResource(new QName(
        BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_TWO)));
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
  public String getLabel(String defaultLabel, boolean fetchLabel) {
    return resource.getLabel(defaultLabel, fetchLabel);
  }

  @Override
  public Set<IFaofPersonBehaviour> getMaintainers() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<IFaofPersonBehaviour> getDevelopers() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<IFaofPersonBehaviour> getDocumenters() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<IFaofPersonBehaviour> getHelpers() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<IFaofPersonBehaviour> getTesters() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<IFaofPersonBehaviour> getTranslators() throws SimalRepositoryException {
    HashSet<IFaofPersonBehaviour> people = new HashSet<IFaofPersonBehaviour>();
    return people;
  }

  @Override
  public Set<String> getProgrammingLangauges() {
    HashSet<String> languages = new HashSet<String>();
    languages
        .add(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE);
    languages
        .add(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO);
    return languages;
  }

}
