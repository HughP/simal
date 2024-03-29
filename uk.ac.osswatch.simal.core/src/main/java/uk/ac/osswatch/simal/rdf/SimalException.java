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
 * A base level Simal Exception. This should be used whenever Simal wants to
 * throw an exception that is not covered by another SimalException class.
 * 
 */
public class SimalException extends Exception {
  private static final long serialVersionUID = -3554180177554295441L;

  /**
   * @param message
   * @param cause
   */
  public SimalException(String message, Throwable cause) {
    super(message, cause);
  }

  public SimalException(String message) {
    super(message);
  }

}
