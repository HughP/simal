package uk.ac.osswatch.simal.rdf.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * Validates and annotates an RDF/XML file for use within SIMAL. This handler
 * does things like ensure a valid QName is present and key Simal information is
 * captured. <br/>
 * <h3>FIXME</h3>
 * At present this handler wraps an RDFXMLWriter and writes the file to the
 * local disk. This can then be read from the disk complete with annotations.
 * This is unwanted overhead, ideally this handler will be re-written to
 * annotate the file on the fly as it is being added to the repository.
 * 
 */
public class AnnotatingRDFXMLHandler implements RDFHandler {

	private RDFXMLWriter handler;

	/**
	 * Create a new AnnotatingRDFXMLHandler that will write to a file.
	 * 
	 * @param file
	 *            the file to write the annotated RDF/XML output to.
	 * @throws IOException
	 *             if there is a problem creating the output file
	 */
	public AnnotatingRDFXMLHandler(File file) throws IOException {
		handler = new RDFXMLWriter(new FileWriter(file));
	}

	public void endRDF() throws RDFHandlerException {
		handler.endRDF();
	}

	public void handleComment(String comment) throws RDFHandlerException {
		handler.handleComment(comment);
	}

	public void handleNamespace(String prefix, String name)
			throws RDFHandlerException {
		handler.handleNamespace(prefix, name);
	}

	public void handleStatement(Statement inStatement)
			throws RDFHandlerException {
		Statement outStatement = inStatement;
		Resource outSubject;
		URI outPredicate;
		Value outValue;

		outSubject = verifyQName(inStatement.getSubject());
		outValue = fixEncoding(inStatement.getObject());
		outPredicate = fixFoaf(inStatement.getPredicate());

		outStatement = new StatementImpl(outSubject,
				outPredicate, outValue);

		handler.handleStatement(outStatement);
	}

	/**
	 * Fixes up statements in the FOAF schema.
	 * 
	 * @param predicate
	 * @return
	 */
	private URI fixFoaf(final URI inPredicate) {
		URI outPredicate = inPredicate;
		String strValue = inPredicate.stringValue();
		if (strValue.contains("/foaf/")) {
			if (strValue.endsWith("/name")) {
			  // for some reason elmo does not support foaf:name, for now just convert to givenname
			  outPredicate = new URIImpl(strValue.substring(0, strValue.length() - 5) + "/givenname");
			}
		}
		return outPredicate;
	}

	private Value fixEncoding(final Value inValue)
			throws RDFHandlerException {
		Value outValue = inValue;
		
		if (inValue instanceof LiteralImpl) {
			String strValue = inValue.stringValue();
			try {
				byte[] utf8Bytes = strValue.getBytes("UTF8");
				outValue = new LiteralImpl(new String(utf8Bytes));
			} catch (UnsupportedEncodingException e) {
				throw new RDFHandlerException("Unable to encode value", e);
			}
		}
		return outValue;
	}

	private Resource verifyQName(final Resource inSubject) {
		Resource outSubject = inSubject;

		String strSubject = inSubject.toString();
		if (strSubject.startsWith("_:") || strSubject.indexOf(",") > 0
				|| strSubject.lastIndexOf("/") == strSubject.length()) {
			// looks like we have no QName
			outSubject = new URIImpl(SimalRepository.DEFAULT_NAMESPACE_URI
					+ inSubject);
		}
		return outSubject;
	}

	public void startRDF() throws RDFHandlerException {
		handler.startRDF();
	}

}
