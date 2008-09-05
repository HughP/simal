package uk.ac.osswatch.simal.model.jena;
/*
 * 
Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLocation;
import uk.ac.osswatch.simal.model.IDoapRepository;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class Repository extends DoapResource implements IDoapRepository {
  private static final long serialVersionUID = 1L;
  
  private boolean isARCH = false;
  private boolean isBK = false;
  private boolean isCVS = false;
  private boolean isSVN = false;

  public Repository(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
    Resource type = getJenaResource().getProperty(RDF.type).getResource();
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
    StmtIterator itr = getJenaResource().listProperties(Doap.ANON_ROOT);
    while (itr.hasNext()) {
      locations.add(itr.nextStatement().getString());
    }
    return locations;
  }

  public Set<IDoapLocation> getLocations() {
    HashSet<IDoapLocation> locations = new HashSet<IDoapLocation>();
    StmtIterator itr = getJenaResource().listProperties(Doap.LOCATION);
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
    StmtIterator itr = getJenaResource().listProperties(Doap.MODULE);
    while (itr.hasNext()) {
      modules.add(itr.nextStatement().getString());
    }
    return modules;
  }

  public Set<IDoapLocation> getBrowse() {
    HashSet<IDoapLocation> locations = new HashSet<IDoapLocation>();
    StmtIterator itr = getJenaResource().listProperties(Doap.BROWSE);
    while (itr.hasNext()) {
      locations.add(new Location(itr.nextStatement().getResource()));
    }
    return locations;
  }
}
