package uk.ac.osswatch.simal.model.simal;
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

import java.util.Calendar;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalException;

public interface IReview extends IResource {
	
	public String TYPE_OPENNESS = "Openness";
	
	/**
	 * Get the person who performed this review.
	 * @throws SimalException 
	 */
	public IPerson getReviewer() throws SimalException;

	/**
	 * Set the person who performed this review.
	 * @param reviewer
	 */
	public void setReviewer(IPerson reviewer);
	
	/**
	 * Get the date this review was made.
	 */
	public Calendar getDate();
	
	/**
	 * Get the date formatted as a short date for display.
	 * @return
	 */
	public String getShortDate();
	
	/**
	 * Get the type of this review.
	 */
	public String getType();

	/**
	 * Get the project that is the subject of this review.
	 * @return
	 * @throws SimalException 
	 */
	public IProject getProject() throws SimalException;

	/**
	 * Set the project that is the subject of this review.
	 * @param project
	 */
	public void setProject(IProject project);

	/**
	 * Set the date that this review was conducted.
	 */
	public void setDate(Calendar instance);

	/**
	 * Set the type of this review.
	 * @param type
	 */
	public void setType(String type);

}
