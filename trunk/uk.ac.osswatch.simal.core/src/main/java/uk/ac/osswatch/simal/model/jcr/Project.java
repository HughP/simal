package uk.ac.osswatch.simal.model.jcr;
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
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IFeed;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Project extends DoapResource implements IProject {
	private static final long serialVersionUID = 1L;
	
	public Project(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	public void addCategory(IDoapCategory category) {
		// TODO Auto-generated method stub
		
	}

	public void addDeveloper(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public void addDocumenter(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public void addHelper(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public void addHomepage(IDoapHomepage homepage) {
		// TODO Auto-generated method stub
		
	}

	public void addMaintainer(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public void addTester(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public void addTranslator(IPerson person) {
		// TODO Auto-generated method stub
		
	}

	public HashSet<IPerson> getAllPeople() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapCategory> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getDevelopers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getDocumenters() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapDownloadMirror> getDownloadMirrors() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapDownloadPage> getDownloadPages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IFeed> getFeeds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getHelpers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapHomepage> getHomepages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapBugDatabase> getIssueTrackers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapMailingList> getMailingLists() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getMaintainers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getOSes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapHomepage> getOldHomepages() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOpennessRating() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Set<String> getProgrammingLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapRelease> getReleases() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapRepository> getRepositories() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapScreenshot> getScreenshots() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getTesters() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getTranslators() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapWiki> getWikis() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeDeveloper(IPerson person) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void removeDocumenter(IPerson person)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void removeHelper(IPerson person) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void removeHomepage(IDoapHomepage homepage) {
		// TODO Auto-generated method stub
		
	}

	public void removeMaintainer(IPerson person)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void removeTester(IPerson person) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void removeTranslator(IPerson person)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}
}
