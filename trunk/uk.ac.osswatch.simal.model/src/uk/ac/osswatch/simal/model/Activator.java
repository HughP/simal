package uk.ac.osswatch.simal.model;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.osswatch.simal.service.IContributorService;
import uk.ac.osswatch.simal.service.ILanguageService;
import uk.ac.osswatch.simal.service.IProjectService;
import uk.ac.osswatch.simal.service.derby.ManagedContributorBean;
import uk.ac.osswatch.simal.service.derby.ManagedLanguageBean;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
        ManagedProjectBean projectBean = new ManagedProjectBean();
        context.registerService(IProjectService.class.getName(), projectBean,
                null);

        ManagedContributorBean contributorBean = new ManagedContributorBean();
        context.registerService(IContributorService.class.getName(), contributorBean,
                null);
        
        ManagedLanguageBean languageBean = new ManagedLanguageBean();
        context.registerService(ILanguageService.class.getName(), languageBean,
                null);
        
		System.out.println("Simal model plugin started");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Simal model plugin ended");
	}

}
