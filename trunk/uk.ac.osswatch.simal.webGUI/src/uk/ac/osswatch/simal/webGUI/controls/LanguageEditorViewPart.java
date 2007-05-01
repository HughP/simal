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

import org.eclipse.rap.jface.viewers.ISelection;
import org.eclipse.rap.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.ISelectionListener;
import org.eclipse.rap.ui.ISelectionService;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchPart;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.part.ViewPart;

import org.eclipse.rap.rwt.layout.GridLayout;
import org.eclipse.rap.rwt.widgets.Text;

import uk.ac.osswatch.simal.model.Language;
import uk.ac.osswatch.simal.service.derby.ManagedLanguageBean;

public class LanguageEditorViewPart extends ViewPart {
    public static final String ID = "uk.ac.osswatch.simal.webGUI.controls.LanguageEditorViewPart";

    private Composite form = null;

    private Text id;

    private Text name;

    private Language currentLanguage;

    public void createPartControl(final Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form = new Composite(parent, RWT.NONE);
        form.setLayout(gridLayout);

        id = ViewPartHelper.addText("ID:", form);
        name = ViewPartHelper.addText("Name:", form);

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
                    save();
                    IStructuredSelection sselection = (IStructuredSelection) selection;
                    Object firstElement = sselection.getFirstElement();
                    if (firstElement instanceof Language) {
                        populate((Language) firstElement);
                    }
                }
            }
        });
    }

    protected void save() {
        if (currentLanguage != null) {
            currentLanguage.setName(name.getText());

            ManagedLanguageBean lb = new ManagedLanguageBean();
            lb.save(currentLanguage);
            
            LanguageTreeViewPart languageTree = (LanguageTreeViewPart) PlatformUI
                    .getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findViewReference(LanguageTreeViewPart.ID).getView(false);
            languageTree.refresh(currentLanguage);
        }
    }

    private void populate(final Language language) {
        currentLanguage = language;

        id.setText(Long.toString(language.getId()));
        if (language.getName() == null) {
            name.setText("");
        } else {
            name.setText(language.getName());
        }
    }
}
