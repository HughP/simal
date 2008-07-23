package uk.ac.osswatch.simal.model.jena;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLocation;
import uk.ac.osswatch.simal.model.IDoapRepository;

public class Repository extends DoapResource implements IDoapRepository {

  private boolean isARCH = false;
  private boolean isBK = false;
  private boolean isCVS = false;
  private boolean isSVN = false;

  public Repository(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
    Resource type = jenaResource.getProperty(RDF.type).getResource();
    if (type.equals(Doap.ARCH_REPOSITORY)) {
      isARCH = true;
    }
    if (type.equals(Doap.BKREPOSITORY)) {
      isBK = true;
    }
    if (type.equals(Doap.CVSREPOSITORY)) {
      isCVS = true;
    }
    if (type.equals(Doap.SVNREPOSITORY)) {
      isSVN = true;
    }
  }

  public Set<String> getAnonRoots() {
    HashSet<String> locations = new HashSet<String>();
    StmtIterator itr = jenaResource.listProperties(Doap.ANON_ROOT);
    while (itr.hasNext()) {
      locations.add(itr.nextStatement().getString());
    }
    return locations;
  }

  public Set<IDoapLocation> getLocations() {
    HashSet<IDoapLocation> locations = new HashSet<IDoapLocation>();
    StmtIterator itr = jenaResource.listProperties(Doap.LOCATION);
    while (itr.hasNext()) {
      locations.add(new Location(itr.nextStatement().getResource()));
    }
    return locations;
  }

  public boolean isARCH() {
    return isARCH;
  }

  public boolean isBK() {
    return isBK;
  }

  public boolean isCVS() {
    return isCVS;
  }

  public boolean isSVN() {
    return isSVN;
  }

  public Set<String> getModule() {
    HashSet<String> modules = new HashSet<String>();
    StmtIterator itr = jenaResource.listProperties(Doap.MODULE);
    while (itr.hasNext()) {
      modules.add(itr.nextStatement().getString());
    }
    return modules;
  }

  public Set<IDoapLocation> getBrowse() {
    HashSet<IDoapLocation> locations = new HashSet<IDoapLocation>();
    StmtIterator itr = jenaResource.listProperties(Doap.BROWSE);
    while (itr.hasNext()) {
      locations.add(new Location(itr.nextStatement().getResource()));
    }
    return locations;
  }
}
