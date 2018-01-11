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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRatingRecyclerAdapter;
import com.example.notepad.bullsandcows.utils.converters.QuerySelectionFormer;

import java.util.HashMap;
import java.util.Map;

import static com.example.notepad.bullsandcows.utils.Constants.ContentProvidersConstant.SORT_ITEM_BY_MOVES_TIME;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.CODED_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.ENY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_RESULT_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_WEEK;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.NOT_UPDATED;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.RESULT_ON_BACKEND;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.UPDATE_TO_SERVER_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.USER_NAME_BUNDLE_KEY;


public class UsersRatingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mCodedDigits;

    private RadioButton mDayRating;

    private CheckBox mOnlineResultCheckBox;

    private RecyclerView mRecycler;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_rating, container, false);
        mRecycler = view.findViewById(R.id.users_rating_recycler_view);
        mOnlineResultCheckBox = view.findViewById(R.id.offline_result_check_box);

        setUpRatingSort(view);

        return view;
    }

    private void setUpRatingSort(final View pView) {

        mDayRating = pView.findViewById(R.id.day_rating_radio_button);
        mOnlineResultCheckBox = pView.findViewById(R.id.offline_result_check_box);
        final RadioButton weekRating = pView.findViewById(R.id.week_rating_radio_button);

        final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton pCompoundButton, final boolean pB) {
                showRating(mCodedDigits);
            }
        };

        mOnlineResultCheckBox.setOnCheckedChangeListener(listener);
        mDayRating.setOnCheckedChangeListener(listener);
        weekRating.setOnCheckedChangeListener(listener);
    }

    public void showRating(final int pCodedDigits) {
        mCodedDigits = pCodedDigits;
        final Bundle args = new Bundle();
        args.putString(USER_NAME_BUNDLE_KEY, ENY);
        args.putString(CODED_BUNDLE_KEY, String.valueOf(pCodedDigits));

        if (mDayRating.isChecked()) {
            args.putString(LAST_RESULT_BUNDLE_KEY, LAST_DAY);
        } else {
            args.putString(LAST_RESULT_BUNDLE_KEY, LAST_WEEK);
        }

        if (mOnlineResultCheckBox.isChecked()) {
            args.putString(UPDATE_TO_SERVER_BUNDLE_KEY, NOT_UPDATED);
        } else {
            args.putString(UPDATE_TO_SERVER_BUNDLE_KEY, RESULT_ON_BACKEND);
        }

        getActivity().getLoaderManager().restartLoader(2, args, this);
    }

    private void initRecycler(final Cursor pCursor) {
        final UserRatingRecyclerAdapter adapter = new UserRatingRecyclerAdapter(pCursor);
        mRecycler.setHasFixedSize(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Map<String, String> selectionArgsMap = new HashMap<>();
        selectionArgsMap.put(UserRecordsDB.NIK_NAME, args.getString(USER_NAME_BUNDLE_KEY));
        selectionArgsMap.put(UserRecordsDB.CODES, args.getString(CODED_BUNDLE_KEY));
        selectionArgsMap.put(UserRecordsDB.ID, args.getString(LAST_RESULT_BUNDLE_KEY));
        selectionArgsMap.put(UserRecordsDB.IS_UPDATE_ONLINE, args.getString(UPDATE_TO_SERVER_BUNDLE_KEY));

        final QuerySelectionArgsModel readySelection = QuerySelectionFormer.convertSelectionArg(selectionArgsMap);
        return new CursorLoader(getActivity().getBaseContext(), RecordsContentProvider.CONTENT_URI,
                null, readySelection.getSelection(), readySelection.getSelectionArgs(), SORT_ITEM_BY_MOVES_TIME);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        if (getActivity() != null) {
            initRecycler(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }
}
