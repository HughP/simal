package uk.ac.osswatch.simal.webGUI.controls;

import java.util.Collection;

import org.eclipse.rap.jface.viewers.ISelection;
import org.eclipse.rap.jface.viewers.IStructuredContentProvider;
import org.eclipse.rap.jface.viewers.IStructuredSelection;
import org.eclipse.rap.jface.viewers.ITreeContentProvider;
import org.eclipse.rap.jface.viewers.TreeViewer;
import org.eclipse.rap.jface.viewers.Viewer;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.ISelectionListener;
import org.eclipse.rap.ui.ISelectionService;
import org.eclipse.rap.ui.IViewPart;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchPart;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.part.ViewPart;
import org.osgi.framework.BundleException;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.webGUI.GUIActivator;

public class ContributorTreeViewPart extends ViewPart {

    private TreeViewer viewer;

    private TreeViewerContentProvider contentProvider;

    class TreeViewerContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {

        private Collection<Contributor> contributors;

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent instanceof IViewPart) {
                if (contributors == null)
                    initialise(null);
                return getChildren(contributors);
            }
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (Collection.class.isAssignableFrom(parent.getClass())) {
                return contributors.toArray();
            }
            return null;
        }

        public boolean hasChildren(Object parent) {
            return false;
        }

        /**
         * Initialise the contents of the tree.
         * 
         * @param project
         *            the project whose contributors should be displayed, set to
         *            null to display all contributors.
         */
        public void initialise(Project project) {
            if (project == null) {
                try {
                    contributors = GUIActivator.getContributorService()
                            .findAll();
                } catch (BundleException e) {
                    throw new RuntimeException(e);
                }
            } else {
                contributors = project.getContributors();
            }
        }
    }

    public void createPartControl(final Composite parent) {
        contentProvider = new TreeViewerContentProvider();
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(contentProvider);
        viewer.setInput(this);
        getSite().setSelectionProvider(viewer);

        createSelectionListener();
    }

    public void setFocus() {
        viewer.getTree().setFocus();
    }

    private void createSelectionListener() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        ISelectionService selectionService = window.getSelectionService();
        selectionService.addSelectionListener(new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part,
                    final ISelection selection) {
                if (selection != null) {
                    IStructuredSelection sselection = (IStructuredSelection) selection;
                    Object firstElement = sselection.getFirstElement();
                    if (firstElement instanceof Project) {
                        contentProvider.initialise((Project) firstElement);
                    }
                }
            }
        });
    }
}
