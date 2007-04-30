package uk.ac.osswatch.simal.webGUI.controls;

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

import uk.ac.osswatch.simal.model.Event;


public class EventEditorViewPart extends ViewPart {
    public static final String ID = "uk.ac.osswatch.simal.webGUI.controls.EventEditorViewPart";

    String[] projectItems = {"Project 1", "project 2", "Project 3"};
    
    private Composite form = null;
    private Text name;
    private Text shortDesc;
    private Text startDate;
    private Text endDate;
    private List projects;
    
    public void createPartControl(final Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form = new Composite(parent, RWT.NONE);
        form.setLayout(gridLayout);
        
        name = ViewPartHelper.addText("Name:", form);
        shortDesc = ViewPartHelper.addText("Short Description:", form);
        startDate = ViewPartHelper.addText("Start Date:", form);
        endDate = ViewPartHelper.addText("End Date:", form);
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
                    IStructuredSelection sselection = (IStructuredSelection) selection;
                    Object firstElement = sselection.getFirstElement();
                    if (firstElement instanceof Event) {
                        populate((Event) firstElement);
                    }
                }
            }
        });
    }

    private void populate(final Event event) {
        name.setText(event.getName());
        shortDesc.setText(event.getShortDesc());
        startDate.setText(ViewPartHelper.getDateAsString(event.getStartDate()));
        endDate.setText(ViewPartHelper.getDateAsString(event.getEndDate()));
    }
}
