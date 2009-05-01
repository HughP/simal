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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class Pims {
	
	/**
	 * Create a new PImS importer.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SimalRepositoryException 
	 * @throws DuplicateURIException 
	 */
	public Pims() throws FileNotFoundException, IOException, SimalRepositoryException, DuplicateURIException {
        // Get data from projects spreadsheet
        String filename = this.getClass().getResource("test/data/QryProjectsForSimal.xls").getFile();
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filename));
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row   = sheet.getRow(1);
        ISimalRepository repo = SimalRepositoryFactory.getInstance();
        IProject project = repo.createProject(RDFUtils.getDefaultProjectURI(repo.getNewProjectID()));
        
        double id = row.getCell(0).getNumericCellValue();
        project.addName(row.getCell(2).getStringCellValue());
        project.setDescription(row.getCell(4).getStringCellValue());
        IDoapHomepage page = repo.createHomepage(row.getCell(6).getStringCellValue());
        page.addName("Homepage");
        project.addHomepage(page);
        //TODO: capture workpackage info: String projectWorkpackage = row.getCell(5).getStringCellValue();
        //TODO: capture short name: String shortName = row.getCell(3).getStringCellValue();
        //TODO: capture programme info: String programmeId = row.getCell(1).getStringCellValue();
        // TODO: Capture state info: String projectStateName = row.getCell(7).getStringCellValue();
        // TODO: Capture start date info: String projectStartDate = row.getCell(8).getStringCellValue();
        // TODO: Capture start date info: String projectEndDate = row.getCell(9).getStringCellValue();
        
        // Get data from Institutions spreadsheet
        filename = this.getClass().getResource("test/data/QryProjectInstitutionsForSimal.xls").getFile();
        wb = new HSSFWorkbook(new FileInputStream(filename));
        sheet = wb.getSheetAt(0);
        row   = sheet.getRow(1);
        double institutionId = row.getCell(0).getNumericCellValue();
        IOrganisation org = repo.createOrganisation("http://jisc.ac.uk/institution/" + institutionId);
        org.addName(row.getCell(1).getStringCellValue());
        //String institutionProjectId = row.getCell(0).getStringCellValue();
        
	}
}
