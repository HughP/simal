package uk.ac.osswatch.simal.model.jena;

/*
 * Copyright 2007, 2010 University of Oxford * 
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
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.rdf.Doap;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class Repository extends DoapResource implements IDoapRepository {
  private static final long serialVersionUID = 2181691886103506908L;
  
  private DoapRepositoryType repoType;

  public static enum DoapRepositoryType {
    ArchRepository, BKRepository, BazaarBranch, CVSRepository, 
    DarcsRepository, GitRepository, HgRepository, SVNRepository;
  };

  public Repository(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
    Resource type = getJenaResource().getProperty(RDF.type).getResource();
    
    repoType = DoapRepositoryType.valueOf(type.getLocalName()); 
  }

  public Set<String> getAnonRoots() {
    HashSet<String> locations = new HashSet<String>();
    Iterator<Statement> itr = listProperties(Doap.ANON_ROOT).iterator();
    while (itr.hasNext()) {
      locations.add(itr.next().getString());
    }
    return locations;
  }

  public Set<IDocument> getLocations() {
    HashSet<IDocument> locations = new HashSet<IDocument>();
    Iterator<Statement> itr = listProperties(Doap.LOCATION).iterator();
    while (itr.hasNext()) {
      locations.add(new Document(itr.next().getResource()));
    }
    return locations;
  }

  public boolean isARCH() {
    return (repoType == DoapRepositoryType.ArchRepository);
  }

  public boolean isBazaar() {
    return (repoType == DoapRepositoryType.BazaarBranch);
  }

  public boolean isBK() {
    return (repoType == DoapRepositoryType.BKRepository);
  }

  public boolean isCVS() {
    return (repoType == DoapRepositoryType.CVSRepository);
  }

  public boolean isDarcs() {
    return (repoType == DoapRepositoryType.DarcsRepository);
  }

  public boolean isGit() {
    return (repoType == DoapRepositoryType.GitRepository);
  }

  public boolean isMercurial() {
    return (repoType == DoapRepositoryType.HgRepository);
  }

  public boolean isSVN() {
    return (repoType == DoapRepositoryType.SVNRepository);
  }

  public Set<String> getModule() {
    HashSet<String> modules = new HashSet<String>();
    Iterator<Statement> itr = listProperties(Doap.MODULE).iterator();
    while (itr.hasNext()) {
      modules.add(itr.next().getString());
    }
    return modules;
  }

  public Set<IDocument> getBrowse() {
    HashSet<IDocument> locations = new HashSet<IDocument>();
    Iterator<Statement> itr = listProperties(Doap.BROWSE).iterator();
    while (itr.hasNext()) {
      locations.add(new Document(itr.next().getResource()));
    }
    return locations;
  }
}
