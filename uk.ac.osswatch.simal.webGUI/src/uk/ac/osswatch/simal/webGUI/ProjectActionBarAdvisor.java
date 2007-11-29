/*
* Copyright 2007 University of Oxford
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
package uk.ac.osswatch.simal.webGUI;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Language;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedLanguageBean;
import uk.ac.osswatch.simal.webGUI.controls.LanguageTreeViewPart;
import uk.ac.osswatch.simal.webGUI.controls.ProjectTreeViewPart;

public class ProjectActionBarAdvisor extends ActionBarAdvisor {

    private static final String ABOUT_ACTION_ID = AdminWorkbench.getID()
            + ".action.about";

    private static final String NEW_PROJECT_ACTION_ID = AdminWorkbench.getID()
            + ".action.newProject";

    private static final String IMPORT_PROJECT_ACTION_ID = AdminWorkbench.getID()
            + ".action.importProject";

    private static final String NEW_CONTRIBUTOR_ACTION_ID = AdminWorkbench.getID()
            + ".action.newContributor";

    private static final String NEW_LANGUAGE_ACTION_ID = AdminWorkbench.getID()
            + ".action.newLanguage";
    
    private IWorkbenchAction exitAction;

    private Action aboutAction;

    private Action newProjectAction;

    private Action importProjectAction;
    
    private Action newContributorAction;

    private Action newLanguageAction;

    public ProjectActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        ImageDescriptor exitImage = AbstractUIPlugin.imageDescriptorFromPlugin(
                AdminWorkbench.getID(), "icons/exit.png");
        ImageDescriptor helpImage = AbstractUIPlugin.imageDescriptorFromPlugin(
                AdminWorkbench.getID(), "icons/help.png");
        ImageDescriptor newProjectImage = AbstractUIPlugin
                .imageDescriptorFromPlugin(AdminWorkbench.getID(),
                        "icons/newProject.gif");
        ImageDescriptor importProjectImage = AbstractUIPlugin
                .imageDescriptorFromPlugin(AdminWorkbench.getID(),
                    "icons/importProject.gif");
        ImageDescriptor newContributorImage = AbstractUIPlugin
                .imageDescriptorFromPlugin(AdminWorkbench.getID(),
                        "icons/newContributor.gif");
        ImageDescriptor newLanguageImage = AbstractUIPlugin
        .imageDescriptorFromPlugin(AdminWorkbench.getID(),
                "icons/newLanguage.gif");

        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setImageDescriptor(exitImage);
        register(exitAction);

        aboutAction = new Action() {
            public void run() {
                  String title = "Information";
                  String msg = "Simal (c) Oxford University 2007\nFor more information see http://simal.osswatch.ac.uk";
                  MessageDialog.openInformation( window.getShell(),
                                                 title,
                                                 msg );
            }
        };
        aboutAction.setText("About");
        aboutAction.setId(ABOUT_ACTION_ID);
        aboutAction.setImageDescriptor(helpImage);
        register(aboutAction);

        newProjectAction = new Action() {
            public void run() {
                createProject();
            }

            private void createProject() {
                Project p = new Project();
                ProjectTreeViewPart projectTree = (ProjectTreeViewPart) PlatformUI
                        .getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().findViewReference(
                                ProjectTreeViewPart.ID).getView(false);
                projectTree.getContentProvider().addProject(p);
            }
        };
        newProjectAction.setText("New Project");
        newProjectAction.setId(NEW_PROJECT_ACTION_ID);
        newProjectAction.setImageDescriptor(newProjectImage);
        register(newProjectAction);
        
        importProjectAction = new Action() {            
            public void run() {
                importProject();
            }

            private void importProject() {
                final IInputValidator val = new IInputValidator() {
                    public String isValid( String newText ) {
                      String result = null;
                      if( newText.length() < 2 ) {
                        result = "Text must be at least 2 characters";
                      }
                      return result;
                    }
                  };
                  String title = "Import Project";
                  String mesg = "FIXME: This doesn't actually do anything yet, it's a placeholder for work in progress";
                  String def = "http://";
                  final InputDialog dlg;
                  dlg = new InputDialog( getShell(), title, mesg, def, val );
                  int returnCode = dlg.open();
                  if( returnCode == InputDialog.OK ) {
                    System.out.println( "User input: " + dlg.getValue() );
                  } else {
                      System.out.println( "User cancelled" );
                  }
            }
        };
        importProjectAction.setText("Import Project");
        importProjectAction.setId(IMPORT_PROJECT_ACTION_ID);
        importProjectAction.setImageDescriptor(importProjectImage);
        register(importProjectAction);        

        newContributorAction = new Action() {
            public void run() {
                createContributor();
            }

            private void createContributor() {
                Contributor c = new Contributor();
                ProjectTreeViewPart projectTree = (ProjectTreeViewPart) PlatformUI
                        .getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().findViewReference(
                                ProjectTreeViewPart.ID).getView(false);
                projectTree.getContentProvider().addContributor(c);
            }
        };
        newContributorAction.setText("New Contributor");
        newContributorAction.setId(NEW_CONTRIBUTOR_ACTION_ID);
        newContributorAction.setImageDescriptor(newContributorImage);
        register(newContributorAction);

        newLanguageAction = new Action() {
            public void run() {
                createLanguage();
            }

            private void createLanguage() {
                Language lang = new Language();
                ManagedLanguageBean lb = new ManagedLanguageBean();
                lb.save(lang);

                LanguageTreeViewPart languageTree = (LanguageTreeViewPart) PlatformUI
                        .getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().findViewReference(
                                LanguageTreeViewPart.ID).getView(true);
                languageTree.refresh(lang);
            }
        };
        newLanguageAction.setText("New Language");
        newLanguageAction.setId(NEW_LANGUAGE_ACTION_ID);
        newLanguageAction.setImageDescriptor(newLanguageImage);
        register(newLanguageAction);
    }

    protected void fillMenuBar(final IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("File",
                IWorkbenchActionConstants.M_FILE);
        MenuManager projectMenu = new MenuManager("Project",
                IWorkbenchActionConstants.M_PROJECT);
        MenuManager contributorMenu = new MenuManager("Contributor",
                "uk.ac.osswatch.simal.menu.contributor");
        MenuManager helpMenu = new MenuManager("Help",
                IWorkbenchActionConstants.M_HELP);

        menuBar.add(fileMenu);
        fileMenu.add(exitAction);
        fileMenu.add(newProjectAction);
        fileMenu.add(newContributorAction);
        fileMenu.add(newLanguageAction);

        menuBar.add(projectMenu);
        projectMenu.add(newProjectAction);
        projectMenu.add(importProjectAction);
        
        menuBar.add(contributorMenu);
        contributorMenu.add(newContributorAction);

        menuBar.add(helpMenu);
        helpMenu.add(aboutAction);
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(exitAction);
        toolbar.add(newProjectAction);
        toolbar.add(importProjectAction);
        toolbar.add(newContributorAction);
        toolbar.add(aboutAction);
    }
    

    
    private Shell getShell() {
        Shell shell = PlatformUI
        .getWorkbench().getActiveWorkbenchWindow().getShell();
        return shell;
    }
}
