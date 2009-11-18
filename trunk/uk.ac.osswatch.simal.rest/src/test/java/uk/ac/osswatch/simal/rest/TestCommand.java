package uk.ac.osswatch.simal.rest;

/*
 * Copyright 2008 University of Oxford
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCommand {

	@Test
	public void testGetProjectURL() throws SimalAPIException {
		String id = "prj136";
		String path = "/project/source-simal/project-" + id + "/xml"; 
		String url = "http://localhost:8080/simal-rest" + path;
		RESTCommand cmd = RESTCommand.createCommand(path);

		assertEquals("Command method is incorrect", RESTCommand.GET_PROJECT, cmd.getCommandMethod());
		assertEquals("Command format is incorrect", RESTCommand.FORMAT_XML, cmd.getFormat());
		assertEquals("Command path is incorrect", path, cmd.getPath());
		assertEquals("Command project ID is incorrect", id, cmd.getProjectID());
		assertEquals("Command source is incorrect", "simal", cmd.getSource());
		assertEquals("Command URL is incorrect", url, cmd.getURL());
	}
}