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
function Project(id, name, shortDesc){
    this.id = id;
    this.name = name;
    this.shortDesc = shortDesc;
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
				  var project = new Project(val.simalID, val.name, val.shortdesc);
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
	  var project = ProjectService.projects[projectID];
	  var header = $('<div data-role="header" data-position="fixed"></div>')
                   .append("<h4>" + project.name + "</h4>")
	  //var footer = $('<div data-role="footer" data-position="fixed"><h4>Footer<h4></div>');
	  var content = $("<div data-role='content'/>")
		            .append($("<h2>" + project.name + "</h2>"))
		            .append($("<p>" + project.shortDesc + "</p>"))
		
	  $.mobile.pageContainer.append($("<div data-role='page'/>")
		 				  .attr("id", projectID)
		 				  .attr("data-url", projectID)
		 				  .append(header)
		 				  .append(content));
	  $('#' + projectID).page();
	  $.mobile.changePage("#" + projectID);
  }
}
