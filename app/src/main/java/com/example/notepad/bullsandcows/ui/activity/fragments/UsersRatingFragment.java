package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRatingRecyclerAdapter;
import com.example.notepad.bullsandcows.utils.converters.QueryConverterUtil;

import java.util.HashMap;

import static com.example.notepad.bullsandcows.ui.activity.activities.RecordsCardActivityFromCursorLoaderActivity.CODED_BUNDL_KEY;
import static com.example.notepad.bullsandcows.ui.activity.activities.RecordsCardActivityFromCursorLoaderActivity.LAST_RESULT_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.ui.activity.activities.RecordsCardActivityFromCursorLoaderActivity.USER_NAME_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.ContentProvidersConstant.SORT_ITEM_BY_MOVES_TIME;

public class UsersRatingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView mRecycler;
    UserRatingRecyclerAdapter mAdapter;

    public UsersRatingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_rating, container, false);
        mRecycler = view.findViewById(R.id.users_rating_recycler_view);
        return view;
    }

    public void showRating(int pCodedDigits) {
        Bundle args = new Bundle();
        args.putString(USER_NAME_BUNDLE_KEY, "Eny");
        args.putString(CODED_BUNDL_KEY, String.valueOf(pCodedDigits));
        args.putString(LAST_RESULT_BUNDLE_KEY, "Last day");

        getActivity().getLoaderManager().restartLoader(2, args, this);
    }

    private void initRecycler(Cursor pCursor) {
        mAdapter = new UserRatingRecyclerAdapter(pCursor);
        mRecycler.setHasFixedSize(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        HashMap<String, String> selectionArgsMap = new HashMap<>();
        selectionArgsMap.put(UserRecordsDB.NIK_NAME, args.getString(USER_NAME_BUNDLE_KEY));
        selectionArgsMap.put(UserRecordsDB.CODES, args.getString(CODED_BUNDL_KEY));
        selectionArgsMap.put(UserRecordsDB.ID, args.getString(LAST_RESULT_BUNDLE_KEY));

        QuerySelectionArgsModel readySelection = QueryConverterUtil.convertSelectionArg(selectionArgsMap);
        //getActivity().getContentResolver().notifyChange(RecordsContentProvider.CONTENT_URI);
        //getActivity().getContentResolver().registerContentObserver(RecordsContentProvider.CONTENT_URI, );
        return new CursorLoader(getActivity().getBaseContext(), RecordsContentProvider.CONTENT_URI,
                null, readySelection.getSelection(), readySelection.getSelectionArgs(), SORT_ITEM_BY_MOVES_TIME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(getActivity() != null) {
            initRecycler(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
