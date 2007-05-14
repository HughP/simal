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
        form = new Composite(parent, SWT.NONE);
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
