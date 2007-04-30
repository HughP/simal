package uk.ac.osswatch.simal.webGUI.controls;

import java.util.Collection;
import java.util.Set;

import org.eclipse.rap.jface.viewers.IStructuredContentProvider;
import org.eclipse.rap.jface.viewers.ITreeContentProvider;
import org.eclipse.rap.jface.viewers.TreeViewer;
import org.eclipse.rap.jface.viewers.Viewer;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.IViewPart;
import org.eclipse.rap.ui.part.ViewPart;
import org.osgi.framework.BundleException;

import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.webGUI.AdminWorkbench;
import uk.ac.osswatch.simal.webGUI.GUIActivator;

/**
 * A tree view of events known to the Simal application.
 * 
 */
public class EventTreeViewPart extends ViewPart {

    private TreeViewer viewer;

    class TreeViewerContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {

        private Collection<Event> events;

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent instanceof IViewPart) {
                if (events == null)
                    initialize();
                return getChildren(events);
            }
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (Collection.class.isAssignableFrom(parent.getClass())) {
                return events.toArray();
            }
            return null;
        }

        public boolean hasChildren(Object parent) {
            return false;
        }

        private void initialize() {
            try {
                events = GUIActivator.getEventService().findAll();
            } catch (BundleException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createPartControl(final Composite parent) {
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(new TreeViewerContentProvider());
        viewer.setInput(this);
        getSite().setSelectionProvider(viewer);
    }

    public void setFocus() {
        viewer.getTree().setFocus();
    }
}
