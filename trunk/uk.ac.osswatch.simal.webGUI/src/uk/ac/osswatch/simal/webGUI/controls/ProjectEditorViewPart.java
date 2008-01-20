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
package uk.ac.osswatch.simal.webGUI.controls;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class ProjectEditorViewPart extends ViewPart {
    public static final String ID = "uk.ac.osswatch.simal.webGUI.ProjectEditorViewPart";

    private Project currentProject;

    String[] languageItems = { "Java", "C++", "PHP" };

    String[] operatingSystemItems = { "Linux", "Windows XP", "OSX" };

    String[] licenceItems = { "Apache License 2.0", "GPL", "LGPL" };

    String[] contributorItems = { "Contributor 1", "Contributor 2",
            "Contributor 3" };

    private Composite top = null;

    private Text id = null;

    private Text name = null;

    private Text shortDesc = null;

    private Text description = null;

    private Text url = null;

    private Text mailingListURL = null;

    private Text wikiURL = null;

    private Text downloadURL = null;

    private Text issueTrackerURL = null;

    private List languages = null;

    private List operatingSystems = null;

    private List licences = null;

    private List contributors = null;

    public void createPartControl(final Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        top = new Composite(parent, SWT.NONE);
        top.setLayout(gridLayout);

        id = ViewPartHelper.addText("ID:", top);
        name = ViewPartHelper.addText("Name:", top);
        shortDesc = ViewPartHelper.addText("Short Description:", top);
        description = ViewPartHelper.addText("Description:", top);
        url = ViewPartHelper.addText("URL:", top);
        mailingListURL = ViewPartHelper.addText("Mailing List URL:", top);
        wikiURL = ViewPartHelper.addText("Wiki URL:", top);
        downloadURL = ViewPartHelper.addText("Download URL:", top);
        issueTrackerURL = ViewPartHelper.addText("Issue Tracker URL:", top);
        languages = ViewPartHelper.addList("Languages:", languageItems, top);
        operatingSystems = ViewPartHelper.addList("Operating Systems",
                operatingSystemItems, top);
        licences = ViewPartHelper.addList("Licences", licenceItems, top);
        contributors = ViewPartHelper.addList("Contributors", contributorItems,
                top);

        createSelectionListener();
    }

    private void createSelectionListener() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        ISelectionService selectionService = window.getSelectionService();
        selectionService.addSelectionListener(new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part,
                    final ISelection selection) {
                if (selection != null) {
                    try {
                        save();
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    IStructuredSelection sselection = (IStructuredSelection) selection;
                    Object firstElement = sselection.getFirstElement();
                    if (firstElement instanceof Project) {
                        populate((Project) firstElement);
                    }
                }
            }
        });
    }

    protected void save() throws MalformedURLException {
        if (currentProject != null) {
            currentProject.setName(name.getText());
            currentProject.setShortDesc(shortDesc.getText());
            currentProject.setDescription(description.getText());
            if (url.getText() != "")
                currentProject.setHomepageURL(new URL(url.getText()));
            if (mailingListURL.getText() != "")
                currentProject.setMailingListURL(new URL(mailingListURL
                        .getText()));
            if (wikiURL.getText() != "")
                currentProject.setWikiURL(new URL(wikiURL.getText()));
            if (downloadURL.getText() != "")
                currentProject.setDownloadURL(new URL(downloadURL.getText()));
            if (issueTrackerURL.getText() != "")
                currentProject.setIssueTrackerURL(new URL(issueTrackerURL
                        .getText()));

            ManagedProjectBean pb = new ManagedProjectBean();
            pb.save(currentProject);

            ProjectTreeViewPart projectTree = (ProjectTreeViewPart) PlatformUI
                    .getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findViewReference(ProjectTreeViewPart.ID).getView(false);
            projectTree.refresh(currentProject);
        }
    }

    private void populate(final Project project) {
        currentProject = project;
        id.setText(Long.toString(project.getId()));
        if (project.getName() == null) {
            name.setText("");
        } else {
            name.setText(project.getName());

        }
        shortDesc.setText(project.getShortDesc());
        description.setText(project.getDescription());
        if (project.getHomepageURL() != null) {
            url.setText(project.getHomepageURL());
        }
        if (project.getMailingListURL() != null) {
            mailingListURL
                    .setText(project.getMailingListURL());
        }
        if (project.getWikiURL() != null) {
            wikiURL.setText(project.getWikiURL());
        }
        if (project.getDownloadURL() != null) {
            downloadURL.setText(project.getDownloadURL());
        }
        if (project.getIssueTrackerURL() != null) {
            issueTrackerURL.setText(project.getIssueTrackerURL());
        }
    }

    public void setFocus() {
        name.setFocus();
    }

}
