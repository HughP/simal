package uk.ac.osswatch.simal.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class RESTServlet extends HttpServlet {
    private static final long serialVersionUID = -7003783530005464708L;

    private static final String COMMAND_ALL_PROJECTS = "allProjects";

    private static SimalRepository repo;

    public RESTServlet() {
        try {
            repo = new SimalRepository();
            repo.setIsTest(true);
            repo.initialise();
        } catch (SimalRepositoryException e) {
            log("Unable to create repository object", e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        String[] pathInfo = req.getPathInfo().split("/");
        String cmd = pathInfo[1];

        String response = "Error: Unkown rest request - " + req.getPathInfo();
        ;

        try {
            if (cmd.startsWith(COMMAND_ALL_PROJECTS)) {
                if (cmd.endsWith(".json")) {
                    response = repo.getAllProjectsAsJSON();
                } else {
                    response = "ERROR: Unkown format requested - "
                            + req.getPathInfo();
                }
            }
        } catch (SimalRepositoryException e) {
            // FIXME: Handle errors properly
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
            out.close();
        }

        out.println(response);
        out.close();
    }
}