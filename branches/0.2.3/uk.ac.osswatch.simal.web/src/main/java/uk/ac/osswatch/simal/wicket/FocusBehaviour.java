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
package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;

public class FocusBehaviour extends AbstractBehavior {
	 private static final long serialVersionUID = 1L;

	 private Component component = null;

	 @Override
	 public void bind(Component component) {
	  if (!(component instanceof FormComponent)) {
	   throw new IllegalArgumentException("DefaultFocusBehavior: component must be instanceof FormComponent");
	  }
	  this.component = component;
	  component.setOutputMarkupId(true);
	 }

	 @Override
	 public void renderHead(IHeaderResponse iHeaderResponse) {
	  super.renderHead(iHeaderResponse);
	  iHeaderResponse.renderOnLoadJavascript("document.getElementById('"
	    + component.getMarkupId() + "').focus();");
	 }
}
