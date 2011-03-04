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
function Project(id, name, shortDesc, people, categories){
    this.id = id;
    this.name = name;
    this.shortDesc = shortDesc;
    this.people = people;
    this.categories = categories;
}

var ProjectService = {

  //Find all projects in the repository
  projects: [],
  findAll:function() {
	$.mobile.pageLoading();
	var loc = Properties.getSimalRestURL() + "allProjects/json";    
	loc = Widget.proxify(loc);
	$.getJSON(loc, function(data) {
		  $('#projectList').empty();
		  $.each(data, function(key, val) {
			  $.each(val, function(key, val) {
			      // Create project and store for future use
				  var project = new Project(val.simalID, val.name, val.shortdesc, val.person, val.category);
			      ProjectService.projects[project.id] = project;
			      
			      // Add to list view
			      $('#projectList').append($("<li/>")
			    		  				   .append($("<a/>")
			    		  						   .attr('onClick', 'ProjectService.showProjectDetail("' + project.id + '")')
					      				           .text(project.name)));	        
		  	      $('#projectList').listview('refresh');		  	      
			  });
		  });
		  $.mobile.pageLoading(true);
		});
  },
  
  showProjectDetail:function(projectID) {
	  /*
	   * FIXME: show all the data we have
	   * 
	   * Example of JSON from Simal:
	   * 
	   * [{
       * "id":"http://simal.oss-watch.ac.uk/doap/simal:test-prj1430#Project",
       * "label":"Simal Project Catalogue System",
       * "name":"Simal Project Catalogue System",
       * "shortdesc":"Simal is a tool for building and tracking projects and the people involved in those projects.", 
       * "simalID":"prj1430", 
       * "category":["XML", "JISC Learning and Teaching committee", "Digital Libraries in the Classroom Programme", "E-Learning Frameworks and Tools", "Focus on Access to Institutional Resources (FAIR) Programme", "Shared Infrastructure Services Strand", "E-Resources Theme", "Users & Innovation programme: Personalising technologies", "E-Learning Capital Programme", "Build Management", "Portals Programme", "E-Learning Innovation Programme", "SWaNI interoperability pilots 2 programme", "Core Middleware Infrastructure Programme", "SFC E-Learning Transformation Programme", "Database", "Support for e-research", "Distributed E-Learning Programme", "Presentation Programme", "Web Framework", "MLEs for Lifelong Learning: building MLEs across HE and FE", "Network Server", "HTTP", "Digital Library Infrastructure Programme", "Linking Digital Libraries with VLEs Programme", "E-Administration Theme", "Digitisation Programme", "Social News", "Network Client", "Virtual Research Environments Programme", "E-Research Theme", "Core Middleware: Technology Development Programme", "Repositories and Preservation Programme", "PALS Metadata and Interoperability Programme phase one", "Supporting Institutional Records Management Programme", "PALS Metadata and Interoperability Programme phase two", "Exchange for Learning (X4L) Programme", "Information Environment Theme", "Access Management", "E-Learning Theme", "E-Learning Pedagogy Programme", "Digital Repositories Programme 2005-7", "Grid OGC Collision Programme", "Digital Preservation and Records Management Programme", "JISC Organisational Support Work Programme", "Social Bookmarking", "Social Networking", "Graphics", "Authentication, Authorisation and Accounting Programme", "E-Infrastructure Programme"], 
       * "person":["Ross Gardler", "Gavin McDonald"], 
       * "programmingLanguage":["XML", "Java"]}
	   */
	  var project = ProjectService.projects[projectID];
	  var header = $('<div data-role="header" data-position="fixed"></div>')
                   .append("<h4>" + project.name + "</h4>")
	  //var footer = $('<div data-role="footer" data-position="fixed"><h4>Footer<h4></div>');
      var people = $('<div data-role="collapsible" data-collapsed="true"/>')
                   .append($('<h3>Participants</h3>'));
	  $.each(project.people, function(key, person) {
		  people.append($('<p>' + person + '</p>'));
	  });
	  var categories = $('<div data-role="collapsible" data-collapsed="true"/>')
                       .append($('<h3>Categories</h3>'));
	  $.each(project.categories, function(key, category) {
		  categories.append($('<p>' + category + '</p>'));
	  });
	  var content = $("<div data-role='content'/>")
		            .append($("<h2>" + project.name + "</h2>"))
		            .append($("<p>" + project.shortDesc + "</p>"))
		            .append(categories)
		            .append(people)
	  $.mobile.pageContainer.append($("<div data-role='page'/>")
		 				  .attr("id", projectID)
		 				  .attr("data-url", projectID)
		 				  .append(header)
		 				  .append(content));
	  $('#' + projectID).page();
	  $.mobile.changePage("#" + projectID);
  }
}
