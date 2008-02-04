package uk.ac.osswatch.simal.rdf.io;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

/**
 * Validates an RDF/XML file for use within SIMAL.
 * Parsing an invalid RDF/XML file using this handler will
 * result in a RDFHandlerException being thrown.
 *  
 */
public class ValidatingRDFXMLHandler implements RDFHandler {

	public static final String NO_QNAME_PRESENT = "No valid QName present";

	public void endRDF() throws RDFHandlerException {
		
	}

	public void handleComment(String arg0) throws RDFHandlerException {
		
	}

	public void handleNamespace(String arg0, String arg1)
			throws RDFHandlerException {
		
	}

	public void handleStatement(Statement statement) throws RDFHandlerException {
		String subject = statement.getSubject().toString();
		if (subject.startsWith("_:") || subject.indexOf(",") > 0 || subject.lastIndexOf("/") == subject.length()) {
			// looks like we have no QName
			throw new RDFHandlerException(NO_QNAME_PRESENT);
		}
		//System.out.println(statement.toString());
	}

	public void startRDF() throws RDFHandlerException {
		
	}

}
