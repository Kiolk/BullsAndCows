package com.example.notepad.myapplication.backend;

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
        name = "recordsToNetApi",
        version = "v1",
        resource = "recordsToNet",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.NotePad.example.com",
                ownerName = "backend.myapplication.NotePad.example.com",
                packagePath = ""
        )
)
public class RecordsToNetEndpoint {

    private static final Logger logger = Logger.getLogger(RecordsToNetEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(RecordsToNet.class);
    }

    /**
     * Returns the {@link RecordsToNet} with the corresponding ID.
     *
     * @param mDate the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code RecordsToNet} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "recordsToNet/{mDate}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public RecordsToNet get(@Named("mDate") Long mDate) throws NotFoundException {
        logger.info("Getting RecordsToNet with ID: " + mDate);
        RecordsToNet recordsToNet = ofy().load().type(RecordsToNet.class).id(mDate).now();
        if (recordsToNet == null) {
            throw new NotFoundException("Could not find RecordsToNet with ID: " + mDate);
        }
        return recordsToNet;
    }

    /**
     * Inserts a new {@code RecordsToNet}.
     */
    @ApiMethod(
            name = "insert",
            path = "recordsToNet",
            httpMethod = ApiMethod.HttpMethod.POST)
    public RecordsToNet insert(RecordsToNet recordsToNet) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that recordsToNet.mDate has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(recordsToNet).now();
        logger.info("Created RecordsToNet.");

        return ofy().load().entity(recordsToNet).now();
    }

    /**
     * Updates an existing {@code RecordsToNet}.
     *
     * @param mDate        the ID of the entity to be updated
     * @param recordsToNet the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code mDate} does not correspond to an existing
     *                           {@code RecordsToNet}
     */
    @ApiMethod(
            name = "update",
            path = "recordsToNet/{mDate}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public RecordsToNet update(@Named("mDate") Long mDate, RecordsToNet recordsToNet) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(mDate);
        ofy().save().entity(recordsToNet).now();
        logger.info("Updated RecordsToNet: " + recordsToNet);
        return ofy().load().entity(recordsToNet).now();
    }

    /**
     * Deletes the specified {@code RecordsToNet}.
     *
     * @param mDate the ID of the entity to delete
     * @throws NotFoundException if the {@code mDate} does not correspond to an existing
     *                           {@code RecordsToNet}
     */
    @ApiMethod(
            name = "remove",
            path = "recordsToNet/{mDate}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("mDate") Long mDate) throws NotFoundException {
        checkExists(mDate);
        ofy().delete().type(RecordsToNet.class).id(mDate).now();
        logger.info("Deleted RecordsToNet with ID: " + mDate);
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
            path = "recordsToNet",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<RecordsToNet> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<RecordsToNet> query = ofy().load().type(RecordsToNet.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<RecordsToNet> queryIterator = query.iterator();
        List<RecordsToNet> recordsToNetList = new ArrayList<RecordsToNet>(limit);
        while (queryIterator.hasNext()) {
            recordsToNetList.add(queryIterator.next());
        }
        return CollectionResponse.<RecordsToNet>builder().setItems(recordsToNetList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long mDate) throws NotFoundException {
        try {
            ofy().load().type(RecordsToNet.class).id(mDate).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find RecordsToNet with ID: " + mDate);
        }
    }
}