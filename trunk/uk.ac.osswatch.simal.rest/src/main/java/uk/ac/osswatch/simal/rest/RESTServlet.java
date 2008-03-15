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

  private static final String COMMAND_ALL_PROJECTS = "/allProjects.";
  private static final String COMMAND_ALL_COLLEAGUES = "/allColleagues.";

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

    String cmd = req.getPathInfo();

    String response = "Couldn't handle request for " + req.getPathInfo();

    try {
      if (cmd.contains(COMMAND_ALL_PROJECTS)) {
        ProjectAPI projectAPI = new ProjectAPI(repo);
        response = projectAPI.getAllProjects(req, cmd);
      } else if (cmd.contains(COMMAND_ALL_COLLEAGUES)) {
        PersonAPI personAPI = new PersonAPI(repo);
        response = personAPI.getAllColleagues(req, cmd);
      }
    } catch (SimalAPIException e) {
      response = errorResponse(e);
    } finally {
      out.println(response);
      out.close();
    }
  }

  /**
   * Generate an Error response to be returned to the client.
   * 
   * @param e
   * @return
   */
  private String errorResponse(SimalAPIException e) {
    String response;
    response = "ERROR: " + e.getMessage();
    return response;
  }
}