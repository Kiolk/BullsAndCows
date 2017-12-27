package com.example.NotePad.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "userDataBaseApi",
        version = "v1",
        resource = "userDataBase",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.NotePad.example.com",
                ownerName = "backend.myapplication.NotePad.example.com",
                packagePath = ""
        )
)
public class UserDataBaseEndpoint {

    private static final Logger logger = Logger.getLogger(UserDataBaseEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(UserDataBase.class);
    }

    /**
     * Returns the {@link UserDataBase} with the corresponding ID.
     *
     * @param mUserName the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code UserDataBase} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "userDataBase/{mUserName}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public UserDataBase get(@Named("mUserName") String mUserName) throws NotFoundException {
        logger.info("Getting UserDataBase with ID: " + mUserName);
        UserDataBase userDataBase = ofy().load().type(UserDataBase.class).id(mUserName).now();
        if (userDataBase == null) {
            throw new NotFoundException("Could not find UserDataBase with ID: " + mUserName);
        }
        return userDataBase;
    }

    /**
     * Inserts a new {@code UserDataBase}.
     */
    @ApiMethod(
            name = "insert",
            path = "userDataBase",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserDataBase insert(UserDataBase userDataBase) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that userDataBase.mUserName has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(userDataBase).now();
        logger.info("Created UserDataBase.");

        return ofy().load().entity(userDataBase).now();
    }

    /**
     * Updates an existing {@code UserDataBase}.
     *
     * @param mUserName    the ID of the entity to be updated
     * @param userDataBase the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code mUserName} does not correspond to an existing
     *                           {@code UserDataBase}
     */
    @ApiMethod(
            name = "update",
            path = "userDataBase/{mUserName}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public UserDataBase update(@Named("mUserName") String mUserName, UserDataBase userDataBase) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(mUserName);
        ofy().save().entity(userDataBase).now();
        logger.info("Updated UserDataBase: " + userDataBase);
        return ofy().load().entity(userDataBase).now();
    }

    /**
     * Deletes the specified {@code UserDataBase}.
     *
     * @param mUserName the ID of the entity to delete
     * @throws NotFoundException if the {@code mUserName} does not correspond to an existing
     *                           {@code UserDataBase}
     */
    @ApiMethod(
            name = "remove",
            path = "userDataBase/{mUserName}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("mUserName") String mUserName) throws NotFoundException {
        checkExists(mUserName);
        ofy().delete().type(UserDataBase.class).id(mUserName).now();
        logger.info("Deleted UserDataBase with ID: " + mUserName);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "userDataBase",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<UserDataBase> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<UserDataBase> query = ofy().load().type(UserDataBase.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<UserDataBase> queryIterator = query.iterator();
        List<UserDataBase> userDataBaseList = new ArrayList<UserDataBase>(limit);
        while (queryIterator.hasNext()) {
            userDataBaseList.add(queryIterator.next());
        }
        return CollectionResponse.<UserDataBase>builder().setItems(userDataBaseList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String mUserName) throws NotFoundException {
        try {
            ofy().load().type(UserDataBase.class).id(mUserName).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find UserDataBase with ID: " + mUserName);
        }
    }


    /**
     * Returns the {@link UserDataBase} with the corresponding ID.
     *,
     * @param mUserName the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code UserDataBase} with the provided ID.
     */
    @ApiMethod(
            name = "check",
            path = "userDataBase",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public UserDataBase getByToken(@Named("mUserName") String mUserName,  @Nullable @Named("limit") Integer limit) throws NotFoundException {
        logger.info("Getting UserDataBase with ID: " + mUserName);
        UserDataBase userDataBase = ofy().load().type(UserDataBase.class).id(mUserName).now();
        if (userDataBase == null) {
            throw new NotFoundException("Could not find UserDataBase with ID: " + mUserName);
        }
        return userDataBase;
    }
}