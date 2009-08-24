package uk.ac.osswatch.simal.rdf;
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

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.simal.SimalOntology;

/**
 * NamespaceContext for working with Simal DOAP files. This is used whenever we
 * want to do XPath processing of an RDF document in Simal.
 */
public class SimalNamespaceContext implements NamespaceContext {

    public String getNamespaceURI(String prefix) {
        if (prefix == null) throw new NullPointerException("Null prefix");
        else if ("doap".equals(prefix)) return Doap.NS;
        else if ("foaf".equals(prefix)) return Foaf.NS;
        else if ("rdf".equals(prefix)) return RDF.getURI();
        else if ("simal".equals(prefix)) return SimalOntology.NS;
        else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
        return XMLConstants.NULL_NS_URI;
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator<?> getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

}
