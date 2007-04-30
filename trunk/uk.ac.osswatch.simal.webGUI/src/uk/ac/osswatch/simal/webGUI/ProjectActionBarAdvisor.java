package uk.ac.osswatch.simal.webGUI;

import org.eclipse.rap.jface.action.Action;
import org.eclipse.rap.jface.action.ICoolBarManager;
import org.eclipse.rap.jface.action.IMenuManager;
import org.eclipse.rap.jface.action.IToolBarManager;
import org.eclipse.rap.jface.action.MenuManager;
import org.eclipse.rap.jface.action.ToolBarContributionItem;
import org.eclipse.rap.jface.action.ToolBarManager;
import org.eclipse.rap.jface.dialogs.MessageDialog;
import org.eclipse.rap.jface.resource.ImageDescriptor;
import org.eclipse.rap.jface.window.IWindowCallback;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchActionConstants;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.actions.ActionFactory;
import org.eclipse.rap.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.rap.ui.entrypoint.ActionBarAdvisor;
import org.eclipse.rap.ui.entrypoint.IActionBarConfigurer;
import org.eclipse.rap.ui.plugin.AbstractUIPlugin;

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

    private static final String NEW_CONTRIBUTOR_ACTION_ID = AdminWorkbench.getID()
            + ".action.newContributor";

    private static final String NEW_LANGUAGE_ACTION_ID = AdminWorkbench.getID()
            + ".action.newLanguage";
    
    private IWorkbenchAction exitAction;

    private Action aboutAction;

    private Action newProjectAction;

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
                showAboutDialog();
            }

            private void showAboutDialog() {
                String title = "Information";
                String mesg = "Simal (c) Oxford University 2007\nFor more information see http://simal.osswatch.ac.uk";
                IWindowCallback callback = new IWindowCallback() {
                    public void windowClosed(final int returnCode) {

                    }
                };
                IWorkbench workbench = PlatformUI.getWorkbench();
                IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                MessageDialog.openInformation(window.getShell(), title, mesg,
                        callback);
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
        
        menuBar.add(contributorMenu);
        contributorMenu.add(newContributorAction);

        menuBar.add(helpMenu);
        helpMenu.add(aboutAction);
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(RWT.FLAT | RWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(exitAction);
        toolbar.add(newProjectAction);
        toolbar.add(newContributorAction);
        toolbar.add(aboutAction);
    }
}
