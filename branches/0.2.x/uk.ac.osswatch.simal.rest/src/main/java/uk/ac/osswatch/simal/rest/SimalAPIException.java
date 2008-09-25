package uk.ac.osswatch.simal.rest;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

/**
 * An exception representing a problem with calling the REST API.
 * 
 */
public class SimalAPIException extends Exception {
  private static final long serialVersionUID = 4451929444420972751L;

  public SimalAPIException(String message) {
    super(message);
  }

  public SimalAPIException(String message, Exception cause) {
    super(message, cause);
  }
}
