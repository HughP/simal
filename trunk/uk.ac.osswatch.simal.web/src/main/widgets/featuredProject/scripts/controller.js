/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {
	init:function() {
		Controller.update();
	},

	/**
	 * Retrieve a new featured project from the Simal server.
	 * Once retreived the display will be updated.
	 */
	update:function(project) {
		$.mobile.pageLoading();
		ProjectService.updateFeaturedProject();
	},
	
	renderProject:function() {
		var project = ProjectService.currentProject;
		$('#name').text(project.name);
		$('#shortDesc').text(project.shortDesc);
		$('#more').append($('<a/>')
				          .attr('href', project.getDetailLink())
				          .attr('target', "_blank")
				          .text("Full details"));
		// rebuild the page to register the changes
		$('#home').page();
		// turn off the page loading graphic
		$.mobile.pageLoading(true);
	}
}