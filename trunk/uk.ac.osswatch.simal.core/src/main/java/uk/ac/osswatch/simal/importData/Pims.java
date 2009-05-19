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
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

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
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(1).getStringCellValue();
        if (!title.equals("name")) {
        	throw new SimalException(url + " is not a valid PIMS project export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
	        row   = sheet.getRow(i);
	        int institutionId = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        IOrganisation org = repo.createOrganisation("http://jisc.ac.uk/institution#" + institutionId);
	        org.addName(row.getCell(1).getStringCellValue());
	        int institutionProjectId = ((Double)row.getCell(2).getNumericCellValue()).intValue();
	        org.addCurrentProject("http;//jisc.ac.uk/project#" + institutionProjectId);
        }
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
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(2).getStringCellValue();
        if (!title.equals("projects.name")) {
        	throw new SimalException(url + " is not a valid PIMS project export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
	        row = sheet.getRow(i);
	        int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        IProject project = repo.getOrCreateProject(getProjectURI(id));
	        project.addName(row.getCell(2).getStringCellValue());
	        project.setDescription(row.getCell(4).getStringCellValue());
	        
	        String homepage = row.getCell(6).getStringCellValue();
	        if (homepage.length() != 0 && !homepage.equals("tbc")) {
				try {
					IDoapHomepage page = repo.createHomepage(homepage);
			        page.addName("Homepage");
			        project.addHomepage(page);
			    } catch (DuplicateURIException e) {
			    	logger.warn("PIMS import used a duplicate homepage URL of " + homepage);
			    }
	        }
	        //TODO: capture workpackage info: String projectWorkpackage = row.getCell(5).getStringCellValue();
	        //TODO: capture short name: String shortName = row.getCell(3).getStringCellValue();
	        String programmeId = getCategoryURI(((Double)row.getCell(1).getNumericCellValue()).intValue());
	        IDoapCategory cat = repo.getOrCreateCategory(programmeId);
	        project.addCategory(cat);
	        // TODO: Capture state info: String projectStateName = row.getCell(7).getStringCellValue();
	        // TODO: Capture start date info: String projectStartDate = row.getCell(8).getStringCellValue();
	        // TODO: Capture start date info: String projectEndDate = row.getCell(9).getStringCellValue();
        }
	}
	
	/**
	 * Import programmes from an exported PIMS spreadheet. Themes are known as categories in 
	 * the Simal application
	 * 
	 * @param url
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DuplicateURIException
	 * @throws SimalException 
	 */
	public static void importProgrammes(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
		IProject project = getPimsProject();
		
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(1).getStringCellValue();
        if (!title.equals("programmes.name")) {
        	throw new SimalException(url + " is not a valid PIMS programme export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
	        row = sheet.getRow(i);
	        int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        IDoapCategory cat = repo.getOrCreateCategory(getCategoryURI(id));
	        String name = row.getCell(1).getStringCellValue();
	        cat.addName(name);
	        
	        project.addCategory(cat);
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
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = sheet.getRow(0);
		String title = row.getCell(2).getStringCellValue();
        if (!title.equals("contacts.name")) {
        	throw new SimalException(url + " is not a valid PIMS project contact export file");
        }
        
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i<= lastRow; i++) {
	        row = sheet.getRow(i);
	        int id = ((Double)row.getCell(0).getNumericCellValue()).intValue();
	        IPerson person = repo.getOrCreatePerson(getPersonURI(id));
	        
	        String name = row.getCell(2).getStringCellValue();
	        person.addName(name);
	        
	        int projectId = ((Double)row.getCell(1).getNumericCellValue()).intValue();
	        IProject project = repo.getOrCreateProject(getProjectURI(projectId));
	        
	        // FIXME: record the contacts job_title
	        // FIXME: record the contacts institutions.name
	        
	        String role = row.getCell(3).getStringCellValue();
	        if (role.equals("Programme Strand Manager") || role.equals("Programme Manager")) {
	        	project.addHelper(person);
	        	Set<IProject> projs = person.getProjects();
	        } else if (role.equals("Project Director") || role.equals("Project Manager")) {
	        	project.addMaintainer(person);
	        	Set<IProject> projs = person.getProjects();
	        } else if (role.equals("Project Team Member")) {
	        	project.addDeveloper(person);
	        	Set<IProject> projs = person.getProjects();
	        } else {
	        	logger.warn("Got a person with an unown role: " + role);
	        }
	        
	        String email = row.getCell(6).getStringCellValue();
	        person.addEmail(email);
	        
	        String tel = row.getCell(7).getStringCellValue();
	        // FIXME: record contact telephone detail
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

	/**
	 * Get or create a resource to represent the PIMS project.
	 * @return
	 * @throws SimalRepositoryException 
	 * @throws DuplicateURIException 
	 */
	private static IProject getPimsProject() throws SimalRepositoryException, DuplicateURIException {
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
		IProject project = repo.getProject(PIMS_PROJECT_URI);
		if (project == null) {
			project = repo.createProject(PIMS_PROJECT_URI);
			project.addName("PIMS");;
			project.setShortDesc("JISC Project Information Management System");
		}
		return project;
	}
}
