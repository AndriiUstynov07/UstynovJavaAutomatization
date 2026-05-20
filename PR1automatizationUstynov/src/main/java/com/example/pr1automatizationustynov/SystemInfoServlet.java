package com.example.pr1automatizationustynov;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/sysinfo")
public class SystemInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Runtime rt = Runtime.getRuntime();
        long maxMb  = rt.maxMemory()   / 1024 / 1024;
        long freeMb = rt.freeMemory()  / 1024 / 1024;
        long totMb  = rt.totalMemory() / 1024 / 1024;
        int  cpus   = rt.availableProcessors();

        out.println("<html><body>");
        out.println("<h2>Системна інформація</h2>");
        out.println("<p><b>CPU (ядра):</b> " + cpus + "</p>");
        out.println("<p><b>RAM max:</b> "   + maxMb  + " MB</p>");
        out.println("<p><b>RAM total:</b> " + totMb  + " MB</p>");
        out.println("<p><b>RAM free:</b> "  + freeMb + " MB</p>");
        out.println("<p><b>OS:</b> "        + System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + "</p>");
        out.println("<p><b>Java:</b> "      + System.getProperty("java.version") + "</p>");
        out.println("</body></html>");
    }
}