package uk.ac.osswatch.simal.webGUI.controls;

import java.util.Collection;

import org.eclipse.rap.jface.viewers.ISelection;
import org.eclipse.rap.jface.viewers.IStructuredContentProvider;
import org.eclipse.rap.jface.viewers.IStructuredSelection;
import org.eclipse.rap.jface.viewers.ITreeContentProvider;
import org.eclipse.rap.jface.viewers.StructuredSelection;
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

import uk.ac.osswatch.simal.model.Language;
import uk.ac.osswatch.simal.webGUI.GUIActivator;

public class LanguageTreeViewPart extends ViewPart {

    public static final String ID = "uk.ac.osswatch.simal.webGUI.LanguageTreeViewPart";

    private TreeViewer viewer;

    private TreeViewerContentProvider contentProvider;

    public class TreeViewerContentProvider implements
            IStructuredContentProvider, ITreeContentProvider {

        private Collection<Language> languages;

        Language selectedLanguage = null;

        private Object[] emptyArray = new Object[] {};

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent instanceof IViewPart) {
                if (languages == null)
                    initialise();
                return getChildren(languages);
            }
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (parent != null && Collection.class.isAssignableFrom(parent.getClass())) {
                return languages.toArray();
            }
            return emptyArray ;
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof Collection) {
                return ((Collection) parent).size() > 0;
            }
            return false;
        }

        private void initialise() {
            try {
                languages = GUIActivator.getLanguageService().findAll();
            } catch (BundleException e) {
                throw new RuntimeException(e);
            }
        }

        public void setSelectedLanguage(Language language) {
            selectedLanguage = language;
        }

        /**
         * Adds a new language to the tree, but not to the underlying persistence
         * engine.
         * 
         * @param project
         */
        public void addLanguage(Language language) {
            languages.add(language);
            viewer.refresh();
            viewer.setSelection(new StructuredSelection(language), true);
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
                    if (firstElement instanceof Language) {
                        contentProvider
                                .setSelectedLanguage((Language) firstElement);
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
