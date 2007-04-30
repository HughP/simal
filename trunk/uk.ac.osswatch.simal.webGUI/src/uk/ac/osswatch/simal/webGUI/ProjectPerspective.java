package uk.ac.osswatch.simal.webGUI;

import org.eclipse.rap.ui.IFolderLayout;
import org.eclipse.rap.ui.IPageLayout;
import org.eclipse.rap.ui.IPerspectiveFactory;

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
