/*
 * Copyright 2009 University of Oxford
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
package uk.ac.osswatch.simal.jcr;

import java.io.IOException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.apache.jackrabbit.core.TransientRepository;

public abstract class BaseJcrTest {
	protected static Session session;
	
	@BeforeClass
	public static void startJcrServer() throws IOException, LoginException, RepositoryException {
        Repository repository = new TransientRepository();
        session = repository.login(
                new SimpleCredentials("username", "password".toCharArray()));
        String[] files = {
        	      "./src/test-config/jcrmapping.xml",
        	      "./src/test-config/jcrmapping-atomic.xml",
        	      "./src/test-config/jcrmapping-beandescriptor.xml"
        	  };
        //ObjectContentManager ocm = new ObjectContentManagerImpl(session, files);
    }
	
	@AfterClass
	public static void stopJcrServer() {
        session.logout();
	}
	
}
