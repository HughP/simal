package uk.ac.osswatch.simal.wicket;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.authentication.LoginPage;
import uk.ac.osswatch.simal.wicket.authentication.SimalSession;
import uk.ac.osswatch.simal.wicket.doap.ProjectBrowserPage;

public class TestLoginPage extends TestBasePage {

	@Before
	public void initTester() throws SimalRepositoryException {
		tester = new WicketTester(new UserApplication());
		tester.startPage(LoginPage.class);
		tester.assertRenderedPage(LoginPage.class);
	}

	@Override
	@Test
	public void testRenderPage() {
		tester.startPage(BasePage.class);
		tester.assertRenderedPage(LoginPage.class);
		tester.assertVisible("footer");

		tester.clickLink("projectBrowserLink");
		tester.assertRenderedPage(ProjectBrowserPage.class);
	}

	@Test
	public void testBadLogin() {
		FormTester formTester = tester.newFormTester("loginForm");
		formTester.setValue("username", "bad");
		formTester.setValue("password", "login");
		formTester.submit("login");

		tester.assertRenderedPage(LoginPage.class);

		String[] errors = { "Invalid username/password" };
		tester.assertErrorMessages(errors);
		SimalSession sessionData = SimalSession.get();
		assertFalse(sessionData.isAuthenticated());
	}

	@Test
	public void testGoodLogin() {
		FormTester formTester = tester.newFormTester("loginForm");
		formTester.setValue("username", "simal");
		formTester.setValue("password", "simal");
		formTester.submit("login");

		tester.assertRenderedPage(LoginPage.class);

		tester.assertNoErrorMessage();
		SimalSession sessionData = SimalSession.get();
		assertTrue("User not authenticated", sessionData.isAuthenticated());

		assertEquals("username is not correct", "simal", sessionData
				.getUsername());
	}
}
