/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.NotePad.myapplication.backend;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import javax.servlet.http.*;

public class MyServlet extends HttpServlet {

    public static final String NAME_OF_APP = "Bulls_And_Cows";
    public static final String VERSION_OF_APP = "3";
    public static final String POWERED = "Yauheni Slizh";
    public static final String FEATURES = "Adds backend";
    public static final String URL_NEW_VERSION_OF_APP = "https://github.com/Kiolk/HelloWorld/blob/master/app-Amazon-release.apk?raw=true";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        VersionOfApp actualVersion = new VersionOfApp(NAME_OF_APP, VERSION_OF_APP, 12223232L, POWERED, FEATURES, URL_NEW_VERSION_OF_APP);
        Gson gson = new Gson();
        String jsonString = gson.toJson(actualVersion);
        resp.getWriter().println(jsonString);
    }
}
