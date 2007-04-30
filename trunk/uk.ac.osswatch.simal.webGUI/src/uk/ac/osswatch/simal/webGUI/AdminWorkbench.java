package uk.ac.osswatch.simal.webGUI;

import org.eclipse.rap.rwt.lifecycle.IEntryPoint;
import org.eclipse.rap.rwt.widgets.Display;
import org.eclipse.rap.ui.PlatformUI;


public class AdminWorkbench implements IEntryPoint {

  public Display createUI() {
    final Display result = PlatformUI.createDisplay();
    PlatformUI.createAndRunWorkbench( result, new AdminWorbenchAdvisor() );
    return result;
  }

    public static String getID() {
        return "uk.ac.osswatch.simal.webGUI";
    }
}
