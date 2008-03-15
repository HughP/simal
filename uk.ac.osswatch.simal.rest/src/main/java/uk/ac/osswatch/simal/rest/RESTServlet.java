package uk.ac.osswatch.simal.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IPerson;
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
        if (cmd.endsWith(".json")) {
          response = repo.getAllProjectsAsJSON();
        } else {
          response = errorResponse("Unkown format requested - "
              + req.getPathInfo());
        }
      } else if (cmd.contains(COMMAND_ALL_COLLEAGUES)) {
        String qname = "http://foo.org/~developer/#me";
        StringBuffer result = new StringBuffer();
        if (cmd.endsWith(".json")) {
          Iterator<IPerson> colleagues = getAllColleagues(qname);
          while (colleagues.hasNext()) {
            result.append("{ \"items\": [");
            result.append(colleagues.next().toJSON(true));
            result.append("]}");
          }
        } else if (cmd.endsWith(".xml")) {
          Iterator<IPerson> colleagues = getAllColleagues(qname);
          result.append("<container>");
          result.append("<viewer>");
          result.append("<person id=\"john.doe\" name=\"FIXME: John Doe\"></person>");
          result.append("</viewer>");

          result.append("<viewerFriends>");
          IPerson person;
          while (colleagues.hasNext()) {
            person = colleagues.next();
            result.append("<person id=\"FIXME:" + person.getFoafGivennames() + "\" name=\"" + person.getFoafGivennames() + "\"></person>");
          }
          result.append("</viewerFriends>");
          result.append("</container>");
        } else {
          result.append(errorResponse("Unkown format requested - "
              + req.getPathInfo()));
        }
        response = result.toString();
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

  /**
   * Get all colleagues of a specified person.
   * @param qname
   * @return
   * @throws SimalRepositoryException
   */
  private Iterator<IPerson> getAllColleagues(String qname)
      throws SimalRepositoryException {
    IPerson person = repo.getPerson(new QName(qname));
    if (person == null) {
      errorResponse("No person known with the QName " + qname);
    }
    Iterator<IPerson> colleagues = person.getColleagues().iterator();
    return colleagues;
  }

  private String errorResponse(String msg) {
    String response;
    response = "ERROR: " + msg;
    return response;
  }
}