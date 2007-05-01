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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.ServiceTracker;

import uk.ac.osswatch.simal.service.IContributorService;
import uk.ac.osswatch.simal.service.IEventService;
import uk.ac.osswatch.simal.service.ILanguageService;
import uk.ac.osswatch.simal.service.IProjectService;

public class GUIActivator implements BundleActivator {

    private static ServiceTracker projectServiceTracker;

    private static ServiceTracker contributorServiceTracker;

    private static ServiceTracker languageServiceTracker;
    
    private static ServiceTracker eventServiceTracker;

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        GUIActivator.projectServiceTracker = new ServiceTracker(context,
                IProjectService.class.getName(), null);
        GUIActivator.projectServiceTracker.open();

        GUIActivator.contributorServiceTracker = new ServiceTracker(context,
                IContributorService.class.getName(), null);
        GUIActivator.contributorServiceTracker.open();

        GUIActivator.languageServiceTracker = new ServiceTracker(context,
                ILanguageService.class.getName(), null);
        GUIActivator.languageServiceTracker.open();

        GUIActivator.eventServiceTracker = new ServiceTracker(context,
                IEventService.class.getName(), null);
        GUIActivator.eventServiceTracker.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {

    }

    public static IProjectService getProjectService() throws BundleException {
        IProjectService projectService = (IProjectService) projectServiceTracker
                .getService();
        if (projectService == null) {
            throw new BundleException(
                    "Unable to find IProjectService implementation");
        } else {
            return projectService;
        }
    }

    public static IContributorService getContributorService()
            throws BundleException {
        IContributorService contributorService = (IContributorService) contributorServiceTracker
                .getService();
        if (contributorService == null) {
            throw new BundleException(
                    "Unable to find IContributorService implementation");
        } else {
            return contributorService;
        }
    }

    public static IEventService getEventService() throws BundleException {
        IEventService eventService = (IEventService) eventServiceTracker
                .getService();
        if (eventService == null) {
            throw new BundleException(
                    "Unable to find IEventService implementation");
        } else {
            return eventService;
        }
    }

    public static ILanguageService getLanguageService() throws BundleException {
        ILanguageService languageService = (ILanguageService) languageServiceTracker
                .getService();
        if (languageService == null) {
            throw new BundleException(
                    "Unable to find IContributorService implementation");
        } else {
            return languageService;
        }
    }
}
