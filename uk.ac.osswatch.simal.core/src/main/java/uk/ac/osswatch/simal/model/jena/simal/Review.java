package uk.ac.osswatch.simal.model.jena.simal;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Person;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.model.jena.Resource;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalException;

public class Review extends Resource implements IReview {

	private static final long serialVersionUID = 1L;

	public Review(com.hp.hpl.jena.rdf.model.Resource resource) {
	    super(resource);
    }
	  
	public Calendar getDate() {
	    String dateString = getLiteralValue(SimalOntology.DATE);
	    if (dateString != null) {
	      Calendar cal = GregorianCalendar.getInstance();
	      cal.setTime(new Date(dateString));
	      return cal;
	    } else {
	      return null;
	    }
	}
	
	public void setDate(Calendar cal) {
		Model model = getJenaResource().getModel();
		String dateString = DateFormat.getDateInstance().format(cal.getTime());
	    Statement statement = model.createLiteralStatement(getJenaResource(),
	        SimalOntology.DATE, dateString);
	    model.add(statement);
	}
	
	@Override
	public String getLabel()  {
		StringBuilder label = new StringBuilder(getType());
		label.append(" review of '");
		try {
			label.append(getProject().getLabel());
		} catch (SimalException e1) {
			label.append("UKNOWN PROJECT");
		}
		label.append("', performed by ");
		try {
			label.append(getReviewer().getLabel());
		} catch (SimalException e) {
			label.append("unknown reviewer");
		}
		label.append(" on ");
		label.append(getShortDate());
		return label.toString();
	}

	public IPerson getReviewer() throws SimalException {
		List<Statement> reviewers = listProperties(SimalOntology.PERSON);
		if (reviewers.size() == 0) {
			throw new SimalException("No reviewer recorded for review: ");
		} else if (reviewers.size() > 1) {
			throw new SimalException("More than one reviewer recorded for review ");
		}
		IPerson reviewer = new Person(reviewers.get(0).getResource());
		return reviewer;
	}

	public String getType() {
		String type = getLiteralValue(SimalOntology.TYPE);
		if (type == null || type =="") {
			return "Unknown type";
		} else {
			return type;
		}
	}
	
	public void setType(String type) {
		Model model = getJenaResource().getModel();
	    Statement statement = model.createLiteralStatement(getJenaResource(),
	        SimalOntology.TYPE, type);
	    model.add(statement);
	}

	public IProject getProject() throws SimalException {
		List<Statement> projects = listProperties(SimalOntology.PROJECT);
		if (projects.size() == 0) {
			throw new SimalException("No project recorded for review: ");
		} else if (projects.size() > 1) {
			throw new SimalException("More than one project recorded for review ");
		}
		IProject project = new Project(projects.get(0).getResource());
		return project;
	}

	public void setProject(IProject project) {
		Model model = getJenaResource().getModel();
	    Statement statement = model.createStatement(getJenaResource(),
	        SimalOntology.PROJECT, (com.hp.hpl.jena.rdf.model.Resource) project
	            .getRepositoryResource());
	    model.add(statement);
	}

	public void setReviewer(IPerson reviewer) {
		Model model = getJenaResource().getModel();
	    Statement statement = model.createStatement(getJenaResource(),
	        SimalOntology.PERSON, (com.hp.hpl.jena.rdf.model.Resource) reviewer
	            .getRepositoryResource());
	    model.add(statement);
		
	}

	public String getShortDate() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		return df.format(getDate().getTime());
	}
}
