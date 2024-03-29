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
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {
	init:function() {
		ProjectService.findAll();
	},

	/**
	 * Update the data currently displayed.
	 * 
	 * @FIXME: currently we don't update just the items that are displayed
	 */
	update:function() {
		ProjectService.findAll();
	}
}