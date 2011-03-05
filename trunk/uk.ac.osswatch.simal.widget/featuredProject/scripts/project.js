/**
 * Copyright 2011 University of Oxford
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * The Project model for working with project instances.
 */ 
function Project(id, name, shortDesc, people, categories, languages){
    this.id = id;
    this.name = name;
    this.shortDesc = shortDesc;
    this.people = people;
    this.categories = categories;
    this.languages = languages;
    
    this.getDetailLink = function() {
    	return Properties.getSimalURL() + "/project/simalID/" + id;
    }
}

var ProjectService = {

  //Find all projects in the repository
  projects: [],  
  currentProject: undefined,
  updateFeaturedProject:function() {
	  var loc = Properties.getSimalRestURL() + "project/project-featured/json";
      loc = Widget.proxify(loc);
	  $.getJSON(loc, function(data) {
		  var project;
		  $.each(data, function(key, val) {
			  if (key == "items") {
				  $.each(val, function(key, projectJSON) {
					  ProjectService.currentProject = new Project(projectJSON.simalID, 
							                projectJSON.name, 
							                projectJSON.shortdesc, 
							                projectJSON.person, 
							                projectJSON.category, 
							                projectJSON.programmingLanguage);
					  ProjectService.projects[ProjectService.currentProject.id] = ProjectService.currentProject;
					  Controller.renderProject();
				  });
			  } else {
				  Controller.renderProject();
			  }
		  });
	  });
  }
}
