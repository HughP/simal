package uk.ac.osswatch.simal.wicket.panel;

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

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel.ReadOnlyStyleBehavior;

/**
 * Simple test using the WicketTester
 */
public class TestSourceRepositoriesPanel extends TestBase {

  @Test
  @SuppressWarnings("serial")
  public void testRenderPanel() {
    tester.startPanel(new TestPanelSource() {
      public Panel getTestPanel(String panelId) {
        try {
          IProject project =  SimalRepositoryFactory.getProjectService().getProject(projectURI);
          ReadOnlyStyleBehavior rosb = new EditProjectPanel(panelId, project).new ReadOnlyStyleBehavior();
          return new SourceRepositoriesPanel(panelId, "Source Repositories", project.getRepositories(), rosb, project);
        } catch (SimalRepositoryException e) {
          fail(e.getMessage());
          return null;
        }
      }
    });
    tester.assertVisible("panel:sourceRepositories");
  }
}
