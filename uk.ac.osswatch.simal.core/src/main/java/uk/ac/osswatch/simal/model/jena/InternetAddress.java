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

import uk.ac.osswatch.simal.model.IInternetAddress;

/**
 * A class for handling emails.
 * 
 */
public class InternetAddress extends Resource implements IInternetAddress {
  private static final long serialVersionUID = -5050032847747325204L;

  public InternetAddress(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public String getObfuscatedAddress() {
    String address = getURI();
    if (address != null) {
      int atPosition = address.indexOf("@");
      if (atPosition > -1) {
        StringBuffer obfuscatedAddress = new StringBuffer();

        obfuscatedAddress.append(address.substring(0, atPosition));
        obfuscatedAddress.append(" [at] ");
        obfuscatedAddress.append(address.substring(atPosition + 1).replaceAll(
            "[.]", "..."));
        address = obfuscatedAddress.toString();
      }
    }

    return address;
  }

  public String getAddress() {
    return getURI();
  }

  public String toString() {
    return getAddress();
  }
}
