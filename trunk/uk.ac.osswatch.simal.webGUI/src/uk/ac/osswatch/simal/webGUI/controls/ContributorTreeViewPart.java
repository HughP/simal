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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
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
