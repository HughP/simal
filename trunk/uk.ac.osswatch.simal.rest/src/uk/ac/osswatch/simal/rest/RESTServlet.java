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
package uk.ac.osswatch.simal.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class RESTServlet extends HttpServlet {

    private static final String DOAP_COMMAND = "/doap/";

    /**
     * 
     */
    private static final long serialVersionUID = -5149735194044428113L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path.startsWith(DOAP_COMMAND)) {
            writeDOAP(req, resp);
        } else {
            resp.getWriter().write("Unable to handle REST request: " + path);
        }
    }

    /**
     * Write an RDF/XML representation of a project using the DOAP schema.
     * 
     * @param resp
     * @throws IOException
     */
    private void writeDOAP(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String path = req.getPathInfo();
        String id = path.substring(path.indexOf(DOAP_COMMAND)
                + DOAP_COMMAND.length());
        Project p;
        ManagedProjectBean pb = new ManagedProjectBean();
        try {
            p = pb.findProject(new Long(id));
        } catch (NumberFormatException e) {
            p = pb.findProjectByShortName(id);
        }
        p.writeDOAP(resp.getOutputStream());
    }
}
