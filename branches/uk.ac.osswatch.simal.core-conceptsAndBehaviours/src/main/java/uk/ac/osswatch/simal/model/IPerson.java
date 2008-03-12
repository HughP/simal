package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.annotations.rdf;

/**
 * A person in the simal space. This records simal 
 * specific data about a person over and above what
 * is possible in FOAF.
 *
 */

@rdf("http://xmlns.com/foaf/0.1/Person")
public interface IPerson extends Person, IFoafPersonBehaviour {
}
