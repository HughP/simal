/*
 * Copyright 2009 University of Oxford
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
package uk.ac.osswatch.simal.importData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Pims {
    private static final Logger logger = LoggerFactory.getLogger(Pims.class);

	public static final String PIMS_PROJECT_URI = "http://www.jisc.ac.uk/project/pims";

	private Pims() {
	}
	
	/**
	 * Import institutions from an export PIMS spreadsheet.
	 * 
	 * @param url
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DuplicateURIException
	 * @throws SimalException 
	 */
	public static void importInstitutions(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
		HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		HSSFRichTextString title = row.getCell(1).getRichStringCellValue();
        if (!title.getString().equals("name")) {
        	throw new SimalException(url + " is not a valid PIMS project export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
        	Document doc;
        	Element foaf;
        	try {
				doc = createRdfDocument();
				foaf = doc.createElementNS(Foaf.getURI(), "Organization");
			} catch (ParserConfigurationException e1) {
				throw new SimalException("Unable to create XML document for import");
			}
			
	        row   = sheet.getRow(i);
	        
	        // rdf:about
        	int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        foaf.setAttributeNS(RDF.getURI(), "about", getOrganisationURI(id));
	        
	        // foaf:name
	        String value = row.getCell(1).getRichStringCellValue().getString();
	        Element elem = doc.createElementNS(Foaf.getURI(), "name");
	        elem.setTextContent(value);
	        foaf.appendChild(elem);
	        
	        // foaf:currentProject
	        int projectId = ((Double)row.getCell(2).getNumericCellValue()).intValue();
	        elem = doc.createElementNS(Foaf.getURI(), "currentProject");
	        elem.setAttributeNS(RDF.getURI(), "resource", getProjectURI(projectId));
	        foaf.appendChild(elem);
	        
	        doc.getDocumentElement().appendChild(foaf);
	        serialise(doc);
	        SimalRepositoryFactory.getInstance().addRDFXML(doc);
        }
	}

	private static String getOrganisationURI(int institutionId) {
		return "http://jisc.ac.uk/institution#" + institutionId;
	}
	
	/**
	 * Import projects from an exported PIMS spreadheet.
	 * 
	 * @param url
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DuplicateURIException
	 * @throws SimalException 
	 */
	public static void importProjects(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
		HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		HSSFRichTextString title = row.getCell(2).getRichStringCellValue();
        if (!title.getString().equals("projects.name")) {
        	throw new SimalException(url + " is not a valid PIMS project export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
        	Document doc;
        	Element doap;
        	try {
				doc = createRdfDocument();
				doap = doc.createElementNS(Doap.getURI(), "Project");
			} catch (ParserConfigurationException e1) {
				throw new SimalException("Unable to create XML document for import");
			}
	        
        	row = sheet.getRow(i);
	        
        	// rdf:about
        	int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        doap.setAttributeNS(RDF.getURI(), "about", getProjectURI(id));
	        
	        // doap:name
	        String value = row.getCell(2).getRichStringCellValue().toString();
	        Element elem = doc.createElementNS(Doap.getURI(), "name");
	        elem.setTextContent(value);
	        doap.appendChild(elem);
	        
	        // doap:description
	        value = row.getCell(4).getRichStringCellValue().getString();
	        elem = doc.createElementNS(Doap.getURI(), "description");
	        elem.setTextContent(value);
	        doap.appendChild(elem);

	        // doap:homepage
	        value = row.getCell(6).getRichStringCellValue().getString();
	        if (value.length() != 0 && !value.equals("tbc")) {
	        	elem = doc.createElementNS(Doap.getURI(), "homepage");
	        	elem.setAttributeNS(RDF.getURI(), "resource", value);
	        	elem.setAttributeNS(RDFS.getURI(), "label", "Homepage");
		        doap.appendChild(elem);
	        }
	        
	        //TODO: capture workpackage info: String projectWorkpackage = row.getCell(5).getStringCellValue();

	        //TODO: capture short name: String shortName = row.getCell(3).getStringCellValue();
	        
	        // doap:category
	        value = getCategoryURI(((Double)row.getCell(1).getNumericCellValue()).intValue());
        	elem = doc.createElementNS(Doap.getURI(), "category");
        	elem.setAttributeNS(RDF.getURI(), "resource", value);
	        doap.appendChild(elem);
	        
	        // TODO: Capture state info: String projectStateName = row.getCell(7).getStringCellValue();
	        // TODO: Capture start date info: String projectStartDate = row.getCell(8).getStringCellValue();
	        // TODO: Capture start date info: String projectEndDate = row.getCell(9).getStringCellValue();
	        
	        doc.getDocumentElement().appendChild(doap);
	        // serialise(doc);
	        SimalRepositoryFactory.getInstance().addProject(doc, url, "http://www.jisc.ac.uk");
        }
	}
	
	/**
	 * Serialise an XML document, used for debugging.
	 * 
	 * @param domImpl
	 * @param document
	 */
	private static void serialise(Document document) {
        DOMImplementationLS ls = (DOMImplementationLS) document.getImplementation();
        LSSerializer lss = ls.createLSSerializer();
        LSOutput lso = ls.createLSOutput();
        lso.setByteStream(System.out);
        lss.write(document, lso);
    }
	
	private static Document createRdfDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation domImpl = db.getDOMImplementation();
        Document document = domImpl.createDocument(RDF.getURI(),
                "RDF", null);
        return document;
	}

	/**
	 * Import programmes from an exported PIMS spreadsheet. Themes are known as categories in 
	 * the Simal application
	 * 
	 * @param url
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DuplicateURIException
	 * @throws SimalException 
	 */
	public static void importProgrammes(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(1).getRichStringCellValue().getString();
        if (!title.equals("programmes.name")) {
        	throw new SimalException(url + " is not a valid PIMS programme export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
        	Document doc;
        	Element cat;
        	try {
				doc = createRdfDocument();
				cat = doc.createElementNS(Doap.getURI(), "category");
			} catch (ParserConfigurationException e1) {
				throw new SimalException("Unable to create XML document for import");
			}

			row = sheet.getRow(i);
			
        	// rdf:about
			int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        cat.setAttributeNS(RDF.getURI(), "about", getCategoryURI(id));

	        // doap:name
	        String value = row.getCell(1).getRichStringCellValue().getString();
	        cat.setAttributeNS(RDFS.getURI(), "label", value);
	        
	        doc.getDocumentElement().appendChild(cat);
	        serialise(doc);
	        SimalRepositoryFactory.getInstance().addRDFXML(doc);
	    }
	}

	/**
	 * Import contacts relating to each project as exported by PIMS.
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws SimalException 
	 */
	public static void importProjectContacts(URL url) throws IOException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(2).getRichStringCellValue().getString();
        if (!title.equals("contacts.name")) {
        	throw new SimalException(url + " is not a valid PIMS project contact export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
        	Document doc;
        	Element project;
        	try {
				doc = createRdfDocument();
				project = doc.createElementNS(Doap.getURI(), "Project");
			} catch (ParserConfigurationException e1) {
				throw new SimalException("Unable to create XML document for import");
			}
			
	        row = sheet.getRow(i);
	        
	        // rdf:about (Project)
	        int projectId = ((Double)row.getCell(1).getNumericCellValue()).intValue();
	        project.setAttributeNS(RDF.getURI(), "about", getProjectURI(projectId));
	        
	        // foaf:Person
	        Element person = doc.createElementNS(Foaf.getURI(), "Person");
        	
        	// rdf:about (Person)
	        int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        person.setAttributeNS(RDF.getURI(), "about", getPersonURI(id));
	        	        
	        // foaf:name
	        String name = row.getCell(2).getRichStringCellValue().getString();
	        Element elem = doc.createElementNS(Foaf.getURI(), "name");
	        elem.setTextContent(name);
	        person.appendChild(elem);
	        
	        // TODO: record the contacts job_title
	        // TODO: record the contacts institutions.name
	        
	        // foaf:mbox
	        HSSFRichTextString email = row.getCell(6).getRichStringCellValue();
	        elem = doc.createElementNS(Foaf.getURI(), "mbox");
	        elem.setAttributeNS(RDF.getURI(), "resource", email.getString());
	        person.appendChild(elem);
	        
	        // TODO: record contact telephone detail
	        //HSSFRichTextString tel = row.getCell(7).getRichStringCellValue();


	        // add appropriate doap:* element for person
	        String role = row.getCell(3).getRichStringCellValue().getString();
	        if (role.equals("Programme Stream Manager") || role.equals("Programme Strand Manager") || role.equals("Programme Manager")) {
	        	elem = doc.createElementNS(Doap.getURI(), "helper");
	        	elem.appendChild(person);
	        } else if (role.equals("Project Director") || role.equals("Project Manager")) {
	        	elem = doc.createElementNS(Doap.getURI(), "maintainer");
	        	elem.appendChild(person);
	        } else if (role.equals("Project Team Member")) {
	        	elem = doc.createElementNS(Doap.getURI(), "developer");
	        	elem.appendChild(person);
	        } else {
	        	logger.warn("Got a person (" + name + ") with an unkown role, adding as helper: " + role);
	        }
        	project.appendChild(elem);
	        
	        doc.getDocumentElement().appendChild(project);
	        serialise(doc);
	        SimalRepositoryFactory.getInstance().addProject(doc, url, getProjectURI(projectId));
	    }
	}
	
	/**
	 * Get a URI for the programme ID provided.
	 * @param id
	 * @return
	 */
	private static String getCategoryURI(int id) {
		return "http://jisc.ac.uk/programme#" + id;
	}
	
	/**
	 * Get a URI for the person ID provided.
	 * @param id
	 * @return
	 */
	private static String getPersonURI(int id) {
		return "http://jisc.ac.uk/person#" + id;
	}
	
	/**
	 * Get a URI for the project ID provided.
	 * @param id
	 * @return
	 */
	private static String getProjectURI(int id) {
		return "http://jisc.ac.uk/project#" + id;
	}
}
