package uk.ac.osswatch.simal.webGUI.controls;

import java.net.MalformedURLException;

import org.eclipse.rap.jface.viewers.ISelection;
import org.eclipse.rap.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.layout.GridLayout;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.rwt.widgets.List;
import org.eclipse.rap.rwt.widgets.Text;
import org.eclipse.rap.ui.ISelectionListener;
import org.eclipse.rap.ui.ISelectionService;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchPart;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.part.ViewPart;

import uk.ac.osswatch.simal.model.Contributor;

public class ContributorEditorViewPart extends ViewPart {
    public static final String ID = "uk.ac.osswatch.simal.webGUI.controls.ContributorEditorViewPart";

    private Contributor currentContributor;

    String[] projectItems = { "Project 1", "project 2", "Project 3" };

    private Composite form = null;

    private Text id;

    private Text name;

    private Text email;

    private Text url;

    private List projects;

    public void createPartControl(final Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form = new Composite(parent, RWT.NONE);
        form.setLayout(gridLayout);

        id = ViewPartHelper.addText("ID:", form);
        name = ViewPartHelper.addText("Name:", form);
        email = ViewPartHelper.addText("Email:", form);
        url = ViewPartHelper.addText("URL:", form);
        projects = ViewPartHelper.addList("Projects:", projectItems, form);

        createSelectionListener();
    }

    public void setFocus() {
        name.setFocus();
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
                    if (firstElement instanceof Contributor) {
                        populate((Contributor) firstElement);
                    }
                }
            }
        });
    }

    private void populate(final Contributor contributor) {
        currentContributor = contributor;

        id.setText(Long.toString(contributor.getId()));
        name.setText(contributor.getName());
        email.setText(contributor.getEmail());
        url.setText(contributor.getUrl());
    }

    protected void save() throws MalformedURLException {
        if (currentContributor != null) {
            currentContributor.setName(name.getText());
            currentContributor.setEmail(email.getText());
            currentContributor.setUrl(url.getText());

            ProjectTreeViewPart projectTree = (ProjectTreeViewPart) PlatformUI
                    .getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findViewReference(ProjectTreeViewPart.ID).getView(false);
            projectTree.refresh(currentContributor);
        }
    }
}
