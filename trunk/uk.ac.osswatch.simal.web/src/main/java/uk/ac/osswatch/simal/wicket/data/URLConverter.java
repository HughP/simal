package uk.ac.osswatch.simal.wicket.data;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * Converts from and to URLs.
 */
public class URLConverter implements IConverter {
  private static final long serialVersionUID = 6821787035834490918L;

  /**
   * Construct.
   */
  public URLConverter() {
  }

  /**
   * @see wicket.util.convert.IConverter#convert(java.lang.Object,
   *      java.lang.Class)
   */
  public URL convertToObject(String value, Locale locale) {
    if (value == null) {
      return null;
    }

    try {
      return new URL(value);
    } catch (MalformedURLException e) {
      throw new ConversionException("'" + value + "' is not a valid URL");
    }
  }


  public String convertToString(Object value, Locale locale) {
    return value != null ? value.toString() : null;
  }
}