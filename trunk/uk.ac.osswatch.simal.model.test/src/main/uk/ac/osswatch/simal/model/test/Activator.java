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
package uk.ac.osswatch.simal.model.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.osswatch.simal.service.IProjectService;
import uk.ac.osswatch.simal.service.mock.ManagedProjectBean;

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

        System.out.println("Simal Mock Services plugin started");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        System.out.println("Simal Mock Services plugin ended");
    }

}
