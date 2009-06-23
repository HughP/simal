package uk.ac.osswatch.simal.wicket.report;
/*
 * Copyright 2009 University of Oxford
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

import org.apache.wicket.markup.html.basic.Label;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;

/**
 * A page for reporting on the status of a set of projects in the repository.
 * This page provides summary information about the projects supplied.
 */
public class ProjectsSummaryReportPage extends BasePage {
    private int numOfProjects;

	public ProjectsSummaryReportPage() {
	      try {
	  		  numOfProjects = UserApplication.getRepository().getAllProjects().size();
	  		  add(new Label("numOfProjects", Integer.toString(numOfProjects)));	
	  		  populateReviewDetails();
	  		  populateRepositoryDetails();
			  populateHomepageDetails();
			  populateMailingListDetails();
			  populateStaffDetails();
			  populateBugDatabaseDetails();
			  populateReleaseDetails();
		} catch (SimalRepositoryException e) {
		      UserReportableException error = new UserReportableException(
		              "Unable to get repository statistics", ProjectsSummaryReportPage.class, e);
		      setResponsePage(new ErrorReportPage(error));
		}
	  }

	private void populateReviewDetails() throws SimalRepositoryException {
		  int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
		  add(new Label("numOfProjectsWithReview", Integer.toString(numOfProjectsWithReview)));
		  
		  int numOfProjectsWithoutReview = numOfProjects - numOfProjectsWithReview;
		  add(new Label("numOfProjectsWithoutReview", Integer.toString(numOfProjectsWithoutReview)));

		  Double percentOfProjectsWithReview = Double.valueOf(((double)numOfProjectsWithReview / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithReview", Math.round(percentOfProjectsWithReview) + "%"));
	}

	private void populateRepositoryDetails() throws SimalRepositoryException {
		  int numOfProjectsWithRCS = SimalRepositoryFactory.getProjectService().getProjectsWithRCS().size();
		  add(new Label("numOfProjectsWithRCS", Integer.toString(numOfProjectsWithRCS)));
		  
		  int numOfProjectsWithoutRCS = numOfProjects - numOfProjectsWithRCS;
		  add(new Label("numOfProjectsWithoutRCS", Integer.toString(numOfProjectsWithoutRCS)));

		  Double percentOfProjectsWithRCS = Double.valueOf(((double)numOfProjectsWithRCS / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithRCS", Math.round(percentOfProjectsWithRCS) + "%"));
	}

	private void populateHomepageDetails() throws SimalRepositoryException {
		  int numOfProjectsWithHomepage = SimalRepositoryFactory.getProjectService().getProjectsWithHomepage().size();
		  add(new Label("numOfProjectsWithHomepage", Integer.toString(numOfProjectsWithHomepage)));
		  
		  int numOfProjectsWithoutHomepage = numOfProjects - numOfProjectsWithHomepage;
		  add(new Label("numOfProjectsWithoutHomepage", Integer.toString(numOfProjectsWithoutHomepage)));

		  Double percentOfProjectsWithHomepage = Double.valueOf(((double)numOfProjectsWithHomepage / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithHomepage", Math.round(percentOfProjectsWithHomepage) + "%"));
	}

	private void populateMailingListDetails() throws SimalRepositoryException {
		  int numOfProjectsWithMailingList = SimalRepositoryFactory.getProjectService().getProjectsWithMailingList().size();
		  add(new Label("numOfProjectsWithMailingList", Integer.toString(numOfProjectsWithMailingList)));
		  
		  int numOfProjectsWithoutMailingList = numOfProjects - numOfProjectsWithMailingList;
		  add(new Label("numOfProjectsWithoutMailingList", Integer.toString(numOfProjectsWithoutMailingList)));

		  Double percentOfProjectsWithMailingList = Double.valueOf(((double)numOfProjectsWithMailingList / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithMailingList", Math.round(percentOfProjectsWithMailingList) + "%"));
	}

	private void populateStaffDetails() throws SimalRepositoryException {
		  int numOfProjectsWithMaintainer = SimalRepositoryFactory.getProjectService().getProjectsWithMaintainer().size();
		  add(new Label("numOfProjectsWithMaintainer", Integer.toString(numOfProjectsWithMaintainer)));
		  
		  int numOfProjectsWithoutMaintainer = numOfProjects - numOfProjectsWithMaintainer;
		  add(new Label("numOfProjectsWithoutMaintainer", Integer.toString(numOfProjectsWithoutMaintainer)));

		  Double percentOfProjectsWithMaintainer = Double.valueOf(((double)numOfProjectsWithMaintainer / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithMaintainer", Math.round(percentOfProjectsWithMaintainer) + "%"));
	}

	private void populateBugDatabaseDetails() throws SimalRepositoryException {
		  int numOfProjectsWithBugDatabase = SimalRepositoryFactory.getProjectService().getProjectsWithBugDatabase().size();
		  add(new Label("numOfProjectsWithBugDatabase", Integer.toString(numOfProjectsWithBugDatabase)));
		  
		  int numOfProjectsWithoutBugDatabase = numOfProjects - numOfProjectsWithBugDatabase;
		  add(new Label("numOfProjectsWithoutBugDatabase", Integer.toString(numOfProjectsWithoutBugDatabase)));

		  Double percentOfProjectsWithBugDatabase = Double.valueOf(((double)numOfProjectsWithBugDatabase / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithBugDatabase", Math.round(percentOfProjectsWithBugDatabase) + "%"));
	}

	private void populateReleaseDetails() throws SimalRepositoryException {
		  int numOfProjectsWithRelease = SimalRepositoryFactory.getProjectService().getProjectsWithRelease().size();
		  add(new Label("numOfProjectsWithRelease", Integer.toString(numOfProjectsWithRelease)));
		  
		  int numOfProjectsWithoutRelease = numOfProjects - numOfProjectsWithRelease;
		  add(new Label("numOfProjectsWithoutRelease", Integer.toString(numOfProjectsWithoutRelease)));

		  Double percentOfProjectsWithRelease = Double.valueOf(((double)numOfProjectsWithRelease / (double)numOfProjects) * 100);
		  add(new Label("percentProjectsWithRelease", Math.round(percentOfProjectsWithRelease) + "%"));
	}
	
}

