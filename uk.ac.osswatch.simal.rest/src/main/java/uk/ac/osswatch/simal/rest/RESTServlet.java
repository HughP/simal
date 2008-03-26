package uk.ac.osswatch.simal.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.SimalAPIException;

/**
 * A servlet class that allows multiple resources to
 * be used for retrieving data. 
 *
 */
public class RESTServlet extends HttpServlet {
  private static final long serialVersionUID = -7003783530005464708L;

  public RESTServlet() {
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter out = res.getWriter();

    RESTCommand cmd = RESTCommand.createCommand(req.getPathInfo());

    String response = "Could not handle request for " + req.getPathInfo();

    try {
      IAPIHandler handler = HandlerFactory.get(cmd);
      response = handler.execute(cmd);      
    } catch (SimalAPIException e) {
      response = errorResponse(e);
    } finally {
      if (cmd.isXML()) {
        res.setContentType("text/xml; charset=UTF-8");
      } else if (cmd.isJSON()) {
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