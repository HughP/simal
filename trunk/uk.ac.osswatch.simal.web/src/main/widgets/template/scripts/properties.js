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
 * Utility methods for working with widget properties.
 */ 
var Properties = {
	
		submitForm: function() {
			var simalURL = dwr.util.getValue("simalURL");
			Widget.preferences.setItem("simalURL", simalURL);
		},
		
		/**
		 * Get the base URL for the Simal REST interface.
		 */
		getSimalRestURL: function() {
			var simalURL = widget.preferences.getItem("simalURL");
			if (simalURL == undefined) {
				simalURL = "http://localhost:8080";
			}
			if (simalURL.match(/\/$/)) {
				simalURL = simalURL + "simal-rest/";
			} else {
				simalURL = simalURL + "/simal-rest/";
			}
			return simalURL;
		}		
}