package uk.ac.osswatch.simal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.osswatch.simal.IAPIHandler;
import uk.ac.osswatch.simal.SimalAPIException;

/**
 * An abstract servlet class that allows multiple resources to
 * be used for retrieving data. Specific implementation of this
 * class provide a wrapper around a data source.
 *
 */
public abstract class AbstractRESTServlet extends HttpServlet {
  private static final long serialVersionUID = -7003783530005464708L;

  public static final String COMMAND_ALL_PROJECTS = "/allProjects/";
  public static final String COMMAND_ALL_COLLEAGUES = "/allColleagues/";

  public static final String XML_SUFFIX = "/xml";
  public static final String JSON_SUFFIX = "/json";

  public AbstractRESTServlet() {
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter out = res.getWriter();

    String cmd = req.getPathInfo();

    String response = "Couldn't handle request for " + req.getPathInfo();

    try {
      IAPIHandler handler = getHandler(cmd);
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
   * Get a handler for a command.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException 
   */
  protected abstract IAPIHandler getHandler(String cmd) throws SimalAPIException;

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