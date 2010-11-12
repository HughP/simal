/*
 * Copyright 2010 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.model;

/**
 * An IDocument is the representation of a foaf:Document.
 * It can be used for all types of websites, eg. URLs that 
 * refer to a doap:homepage or a doap:wiki.
 * 
 */
public interface IDocument extends IFoafResource {

  /**
   * Make sure the Document is of the right type foaf:Document. If the Resource
   * is of another type throw a WrongTypeException.
   * 
   * @return this to allow chaining.
   * @throws IncompatibleTypeException
   *           If the Resource of this Document is of another type.
   */
  public IDocument ensureDocumentType() throws IncompatibleTypeException;
}
