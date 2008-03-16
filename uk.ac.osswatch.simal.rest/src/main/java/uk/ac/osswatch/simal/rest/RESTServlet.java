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

  public static final String COMMAND_ALL_PROJECTS = "/allProjects/";
  public static final String COMMAND_ALL_COLLEAGUES = "/allColleagues/";

  public static final String XML_SUFFIX = "/xml";
  public static final String JSON_SUFFIX = "/json";

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
      IAPIHandler handler = HandlerFactory.createHandler(cmd, repo);
      response = handler.execute(cmd);      
    } catch (SimalAPIException e) {
      response = errorResponse(e);
    } finally {
      if (cmd.endsWith(XML_SUFFIX)) {
        res.setContentType("text/xml; charset=UTF-8");
      } else if (cmd.endsWith(JSON_SUFFIX)) {
        res.setContentType("application/json; charset=UTF-8");
      }
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