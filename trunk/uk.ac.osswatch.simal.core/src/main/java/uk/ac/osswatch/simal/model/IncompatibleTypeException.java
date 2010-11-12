package uk.ac.osswatch.simal.model;

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
import uk.ac.osswatch.simal.rdf.SimalException;

/**
 * Exception used when a resource is of a different type than required or
 * supported and this other type is incompatible with the required type.
 */
public class IncompatibleTypeException extends SimalException {

  private static final long serialVersionUID = -5354081098162235388L;

  /**
   * @param message
   * @param cause
   */
  public IncompatibleTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public IncompatibleTypeException(String message) {
    super(message);
  }

}
