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
        if (cmd.endsWith(".json")) {
          String qname = "http://foo.org/~developer/#me";

          Iterator<IPerson> colleagues = getAllColleagues(qname);
          StringBuffer json = new StringBuffer();
          while (colleagues.hasNext()) {
            json.append("{ \"items\": [");
            json.append(colleagues.next().toJSON(true));
            json.append("]}");
          }
          response = json.toString();
        } else if (cmd.endsWith(".xml")) {
          String qname = "http://foo.org/~developer/#me";

          Iterator<IPerson> colleagues = getAllColleagues(qname);
          StringBuffer xml = new StringBuffer();
          xml.append("<container>");
          xml.append("<viewer>");
          xml.append("<person id=\"john.doe\" name=\"FIXME: John Doe\"></person>");
          xml.append("</viewer>");

          xml.append("<viewerFriends>");
          IPerson person;
          while (colleagues.hasNext()) {
            person = colleagues.next();
            xml.append("<person id=\"FIXME:" + person.getFoafGivennames() + "\" name=\"" + person.getFoafGivennames() + "\"></person>");
          }
          xml.append("</viewerFriends>");
          xml.append("</container>");
          response = xml.toString();
        } else {
          response = errorResponse("Unkown format requested - "
              + req.getPathInfo());
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