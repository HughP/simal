package uk.ac.osswatch.simal.service.derby;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.osswatch.simal.service.IProjectService;

public class Activator implements BundleActivator {

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        // Create the service objects
        ManagedProjectBean projectBean = new ManagedProjectBean();

        // Register the service
        context.registerService(IProjectService.class.getName(), projectBean,
                null);

        System.out.println("Simal Derby plugin started");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        System.out.println("Simal Derby plugin ended");
    }

}
