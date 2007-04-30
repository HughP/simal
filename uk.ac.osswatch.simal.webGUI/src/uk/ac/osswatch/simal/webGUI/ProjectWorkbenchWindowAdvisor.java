package uk.ac.osswatch.simal.webGUI;

import org.eclipse.rap.rwt.graphics.Point;
import org.eclipse.rap.ui.IWorkbench;
import org.eclipse.rap.ui.IWorkbenchWindow;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.entrypoint.ActionBarAdvisor;
import org.eclipse.rap.ui.entrypoint.IActionBarConfigurer;
import org.eclipse.rap.ui.entrypoint.IWorkbenchWindowConfigurer;
import org.eclipse.rap.ui.entrypoint.WorkbenchWindowAdvisor;

public class ProjectWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ProjectWorkbenchWindowAdvisor(
            final IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(
            final IActionBarConfigurer configurer) {
        return new ProjectActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);
        configurer.setTitle("Simal Administration Workbench");
    }

    public void postWindowOpen() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        window.getShell().setLocation(70, 25);
    }
}
