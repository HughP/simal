package uk.ac.osswatch.simal.webGUI;

import org.eclipse.rap.ui.entrypoint.IWorkbenchWindowConfigurer;
import org.eclipse.rap.ui.entrypoint.WorkbenchAdvisor;
import org.eclipse.rap.ui.entrypoint.WorkbenchWindowAdvisor;


public class AdminWorbenchAdvisor extends WorkbenchAdvisor {

  public String getInitialWindowPerspectiveId() {
    return "uk.ac.osswatch.simal.webGUI.projectPerspective";
  }
  
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer windowConfigurer )
  {
    return new ProjectWorkbenchWindowAdvisor( windowConfigurer );
  }
}
