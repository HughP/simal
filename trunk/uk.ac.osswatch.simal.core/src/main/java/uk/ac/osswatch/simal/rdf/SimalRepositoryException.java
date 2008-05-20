/*
 * Copyright 2007 University of Oxford
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
package uk.ac.osswatch.simal.rdf;

/**
 * An exception that is thrown when the repository cannot be configured or
 * accessed correctly.
 * 
 */
public class SimalRepositoryException extends SimalException {
  private static final long serialVersionUID = 9157131958514241674L;

  /**
   * @param message
   * @param cause
   */
  public SimalRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public SimalRepositoryException(String message) {
    super(message);
  }

}
