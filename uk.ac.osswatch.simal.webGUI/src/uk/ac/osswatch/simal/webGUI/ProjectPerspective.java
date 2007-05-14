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
package uk.ac.osswatch.simal.webGUI;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import uk.ac.osswatch.simal.webGUI.controls.ContributorEditorViewPart;
import uk.ac.osswatch.simal.webGUI.controls.EventEditorViewPart;
import uk.ac.osswatch.simal.webGUI.controls.LanguageEditorViewPart;
import uk.ac.osswatch.simal.webGUI.controls.LanguageTreeViewPart;
import uk.ac.osswatch.simal.webGUI.controls.ProjectEditorViewPart;
import uk.ac.osswatch.simal.webGUI.controls.ProjectTreeViewPart;

public class ProjectPerspective implements IPerspectiveFactory {

  public void createInitialLayout( final IPageLayout layout ) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible( false );
    IFolderLayout topLeft = layout.createFolder( "topLeft",
                                                 IPageLayout.LEFT,
                                                 0.25f,
                                                 editorArea );
    topLeft.addView( ProjectTreeViewPart.ID );
    topLeft.addView( LanguageTreeViewPart.ID );
    
    IFolderLayout topRight = layout.createFolder( "topRight",
                                                  IPageLayout.RIGHT,
                                                  0.70f,
                                                  editorArea );
    topRight.addView( ProjectEditorViewPart.ID );
    topRight.addView( ContributorEditorViewPart.ID );
    topRight.addView( EventEditorViewPart.ID );
    topRight.addView( LanguageEditorViewPart.ID );
    topRight.addView( "uk.ac.osswatch.simal.webGUI.BrowserViewPart" );
  }
}
