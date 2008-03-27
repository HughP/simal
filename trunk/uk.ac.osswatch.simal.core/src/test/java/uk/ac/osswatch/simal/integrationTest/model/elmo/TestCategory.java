package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import javax.xml.namespace.QName;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestCategory extends BaseRepositoryTest {
  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException {
    String uri = "http://simal.oss-watch.ac.uk/category/socialNews";
    IDoapCategory category = repository.findCategory(new QName(uri));
    String label = category.getLabel();
    assertEquals("Category Label is incorrect", "Social News", label);

    uri = "http://example.org/does/not/exist";
    QName qname = new QName(uri);
    category = repository.findCategory(qname);
    label = category.getLabel();
    assertNotSame("Somehow we have a valid label for a resource that does not exist",
        qname, category.getQName());
  }
}
