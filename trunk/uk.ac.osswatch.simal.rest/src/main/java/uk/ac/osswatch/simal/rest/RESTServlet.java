package uk.ac.osswatch.simal.rest;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class RESTServlet extends HttpServlet {
    private static final long serialVersionUID = -7003783530005464708L;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        out.println("Hello, world!");
        out.close();
    }
}