package uk.ac.osswatch.simal.wicket.authentication;
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

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.wicket.ToolsPage;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;
import uk.ac.osswatch.simal.wicket.panel.project.AddIResourcePanel;
import uk.ac.osswatch.simal.wicket.simal.AddReviewPanel;

public class SimalAuthorizationStrategy implements IAuthorizationStrategy,
		IUnauthorizedComponentInstantiationListener {
   private static final Logger logger = LoggerFactory.getLogger(SimalAuthorizationStrategy.class);

	public boolean isActionAuthorized(Component component, Action action) {
	  if (component instanceof AddReviewPanel && action.getName().equals(Action.RENDER)) {
	    return SimalSession.get().isAuthenticated();
    } else if (component instanceof AddIResourcePanel && action.getName().equals(Action.RENDER)) {
      return SimalSession.get().isAuthenticated();
    } else if (component instanceof AbstractAddDoapResourcePanel<?> && action.getName().equals(Action.RENDER)) {
      return SimalSession.get().isAuthenticated();
    }
		return true;
	}

	public <T extends Component> boolean isInstantiationAuthorized(Class<T> component) {
		if (component.isAssignableFrom(ToolsPage.class)) {
		   return SimalSession.get().isAuthenticated();
		}
		return true;
	}

	public void onUnauthorizedInstantiation(Component component) {
		logger.info("Unathorised Instantiation on " + component);
		throw new RestartResponseAtInterceptPageException(LoginPage.class);
	}

}
