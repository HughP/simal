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
import org.eclipse.jface.viewers.StructuredSelection;
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

public class ProjectTreeViewPart extends ViewPart {

    public static final String ID = "uk.ac.osswatch.simal.webGUI.ProjectTreeViewPart";

    private TreeViewer viewer;

    private TreeViewerContentProvider contentProvider;

    public class TreeViewerContentProvider implements
            IStructuredContentProvider, ITreeContentProvider {

        private Collection<Project> projects;

        Project selectedProject = null;

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent instanceof IViewPart) {
                if (projects == null)
                    initialise();
                return getChildren(projects);
            }
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (Collection.class.isAssignableFrom(parent.getClass())) {
                return projects.toArray();
            } else if (parent instanceof Project) {
                String[] nodes = { "Contributors", "Events"};
                return nodes;
            } else if (parent instanceof String) {
                String node = (String) parent;
                if (node.equals("Contributors")) {
                    return selectedProject.getContributors().toArray();
                } else if (node.equals("Events")) {
                    return selectedProject.getEvents().toArray();
                }
            }
            return null;
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof Collection) {
                return ((Collection) parent).size() > 0;
            } else if (parent instanceof Project) {
                return true;
            } else if (parent instanceof String) {
                String node = (String) parent;
                if (node.equals("Contributors")) {
                    return selectedProject.getContributors().size() > 0;
                } else if (node.equals("Events")) {
                    return selectedProject.getEvents().size() > 0;
                }
            }
            return false;
        }

        private void initialise() {
            try {
                projects = GUIActivator.getProjectService().findAll();
            } catch (BundleException e) {
                throw new RuntimeException(e);
            }
        }

        public void setSelectedProject(Project project) {
            selectedProject = project;
        }

        /**
         * Adds a new project to the tree, but not to the underlying persistence
         * engine. 
         * 
         * @param project
         */
        public void addProject(Project project) {
            projects.add(project);
            viewer.refresh();
            viewer.setSelection(new StructuredSelection(project), true);
        }

        /**
         * Add a contributor to the currently selected project.
         * 
         * @param contributor
         */
        public void addContributor(Contributor contributor) {
            if (selectedProject != null) {
                selectedProject.addContributor(contributor);
                viewer.refresh(selectedProject);
                viewer.setSelection(new StructuredSelection(contributor), true);
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
                        contentProvider
                                .setSelectedProject((Project) firstElement);
                    }
                }
            }
        });
    }

    public void setFocus() {
        viewer.getTree().setFocus();
    }

    public TreeViewerContentProvider getContentProvider() {
        return contentProvider;
    }

    public void refresh(Object obj) {
        viewer.refresh(obj);
    }
}
