package uk.ac.osswatch.simal.webGUI.controls;

import java.net.URL;

import org.eclipse.rap.jface.viewers.ISelection;
import org.eclipse.rap.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.browser.Browser;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.ISelectionListener;
import org.eclipse.rap.ui.ISelectionService;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchPart;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.part.ViewPart;

import uk.ac.osswatch.simal.model.Project;

public class BrowserViewPart extends ViewPart {

    Browser browser;

    private static String NO_PROJECT_SELECTED_URL = "http://www.oss-watch.ac.uk";

    public void createPartControl(final Composite parent) {
        browser = new Browser(parent, RWT.NONE);
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        ISelection selection = window.getSelectionService().getSelection();
        setUrlFromSelection(selection);
        createSelectionListener();
    }

    public void setFocus() {
        browser.setFocus();
    }

    private void createSelectionListener() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        ISelectionService selectionService = window.getSelectionService();
        selectionService.addSelectionListener(new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part,
                    final ISelection selection) {
                setUrlFromSelection(selection);
            }
        });
    }

    private void setUrlFromSelection(final ISelection selection) {
        browser.setUrl(NO_PROJECT_SELECTED_URL);
        if (selection != null) {
            IStructuredSelection sselection = (IStructuredSelection) selection;
            Object firstElement = sselection.getFirstElement();
            if (firstElement instanceof Project) {
                URL location = ((Project) firstElement).getURL();
                if (location != null) {
                    browser.setUrl(location.toExternalForm());
                }
            }
        }
    }
}
