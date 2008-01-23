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
