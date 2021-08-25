package com.alontests;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.PushBuilder;

import java.io.IOException;
import java.io.PrintWriter;

// Tested with:
// Apache Tomcat 10.0.1
// jdk 1.8.0_231

/*
    The initiator (server) is aware of the resources, so why wait for the client to request them?
    Use PushBuilder to push known resources to the client before he requests them and save some time.
    Only supported with HTTP/2. In case it is not supported newPushBuilder() will return null and push
    will not be used.
*/
@WebServlet(name = "Http2",
        urlPatterns = {"/http2"})
public class Http2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PushBuilder pushBuilder = req.newPushBuilder();

        // Will not be null only if http2 isn't available
        if (pushBuilder != null) {
            pushBuilder
                    .path("css/style.css")
                    .push();
            pushBuilder
                    .path("images/rubber-duck.png")
                    .push();
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<link rel=\"stylesheet\" media=\"all\" href=\"css/style.css\">" +
                            "</head>" +
                            "<body>" +
                            "<div>box</div>" +
                            "<img src=\"images/rubber-duck.png\">" +
                            "</body>" +
                            "</html>"
            );
        }
    }
}
