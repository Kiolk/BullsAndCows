/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.notepad.myapplication.backend;

import com.google.gson.Gson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

    private static final String NAME_OF_APP = "Bulls_And_Cows";
    private static final String VERSION_OF_APP = "3";
    private static final String POWERED = "Yauheni Slizh";
    private static final String FEATURES = "Adds backend";
    private static final String URL_NEW_VERSION_OF_APP = "https://github.com/Kiolk/HelloWorld/blob/master/app-Amazon-release.apk?raw=true";
    private static final String[] NEW_APP_FEATURES = {"New backend", "New logic", "Fixed some bugs"};
    private static final String USER_RECORD_BACKEND = "https://myjson-182914.appspot.com/_ah/api/";
    private static final long DATE_OF_RELEASE = 12223232L;
    private static final String REQUEST_PARAM = "name";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String PLEASE_ENTER_A_NAME = "Please enter a name";
    private static final String MESSAGE = "Please use the form to POST to this url";

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.getWriter().println(MESSAGE);
    }

    //TODO move to doGet.
    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {

        final String name = req.getParameter(REQUEST_PARAM);
        resp.setContentType(CONTENT_TYPE);
        if (name == null) {
            resp.getWriter().println(PLEASE_ENTER_A_NAME);
        }

        final VersionOfApp actualVersion = new VersionOfApp();

        actualVersion.setNameOfApp(NAME_OF_APP);
        actualVersion.setVersionOfApp(VERSION_OF_APP);
        actualVersion.setDateOfRelease(DATE_OF_RELEASE);
        actualVersion.setPowered(POWERED);
        actualVersion.setFeatures(FEATURES);
        actualVersion.setUrlNewVersionOfApp(URL_NEW_VERSION_OF_APP);
        actualVersion.setUserRecordBackend(USER_RECORD_BACKEND);
        actualVersion.setNewVersionFeatures(NEW_APP_FEATURES);

        final Gson gson = new Gson();
        final String jsonString = gson.toJson(actualVersion);
        resp.getWriter().println(jsonString);
    }
}
