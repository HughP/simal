package uk.ac.osswatch.simal.service;

/*
 * Copyright 2007,2010 University of Oxford 
 * 
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

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IncompatibleTypeException;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An interface for working with IDocuments in the repository.
 * 
 */
public interface IDocumentService extends IService {

  /**
   * Get an IDocument from the repository based on its URI which is also the
   * URL. If the URI is not known in the repository a new IDocument is created.
   * If it is known but of a different type a WronTypeException is thrown.
   * 
   * @param uri
   *          The URI of the IDocument
   * @return
   * @throws SimalRepositoryException
   */
  public IDocument getOrCreate(String uri) throws SimalRepositoryException,
      IncompatibleTypeException;

  /**
   * Create a new homepage in the repository.
   * 
   * @param uri
   *          a URI to identify this homepage
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateURIException
   *           if an entity with the given String already exists
   */
  public IDocument create(String uri) throws SimalRepositoryException,
      DuplicateURIException;

  /**
   * Get an existing IDocument resource from the repository. If no document with
   * this URI exists then return null.
   * 
   * @param uri
   *          the URI of the IDocument
   * @return the IDocument resource or null
   * @throws SimalRepositoryException
   *           When repository is not
   * @throws IncompatibleTypeException
   *           When URI is known is the repository but is of a type incompatible
   *           with IDocument
   */
  public IDocument get(String uri) throws SimalRepositoryException,
      IncompatibleTypeException;

}
