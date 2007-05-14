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

import java.util.Collection;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;
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
