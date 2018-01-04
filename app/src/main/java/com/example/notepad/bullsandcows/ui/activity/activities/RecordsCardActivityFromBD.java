package com.example.notepad.bullsandcows.ui.activity.activities;

import android.support.v7.app.AppCompatActivity;

@Deprecated
public class RecordsCardActivityFromBD extends AppCompatActivity  {
//
//    private ArrayList<RecordsToNet> recordModelArrayList;
//    private RecordRecyclerViewAdapter adapter;
//    private RecordsManager mRecordsManager;
//    private String mCursorString;
//    private boolean isLoading;
//    private ProgressBar mRecordsProgressBar;
//    private UserInfoRecordFragment mUserInfoFragment;
//    private FrameLayout mInfoFrameLayout;
//    private FragmentManager mFragmentManager;
//    private FragmentTransaction mFragmentTransaction;
//    private ContentValues[] mArrayContentValues;
//    private CursorListener mCursorListener;
//    private Cursor mCursor;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_records_card);
//        UserLoginHolder.getInstance().setUserOnline();
//        mRecordsProgressBar = findViewById(R.id.records_load_progress_bar);
//        mInfoFrameLayout = findViewById(R.id.user_info_record_frame_layout);
//        mInfoFrameLayout.setOnClickListener(this);
//
//        mCursorString = null;
//        mUserInfoFragment = new UserInfoRecordFragment();
//
//        mCursorListener = new CursorListener() {
//            @Override
//            public Cursor getCursorListener(Cursor pCursor) {
//                mCursor = pCursor;
//                firstTimeShowRecycler(mCursor);
//                return pCursor;
//            }
//        };
//
//        initRecordManager();
//
//        if (CheckConnection.checkConnection(RecordsCardActivityFromBD.this)) {
//            showProgressBar();
//            mRecordsManager.getRecordSBackend(new RequestRecordModel(mCursorString));
//        } else {
//            new DBOperations().query(mCursorListener);
//        }
//    }
//
//    private void initRecordManager() {
//        mRecordsManager = new RecordsManager() {
//
//            @Override
//            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
//                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);
//                Log.d(Constants.TAG, ", cursor :" + response.getmCursor());
//                recordModelArrayList = response.getmRecordsArray();
//
//                mArrayContentValues = new ContentValues[recordModelArrayList.size()];
//                int i = 0;
//                for(RecordsToNet note : recordModelArrayList){
//                    ContentValues cv = new ContentValues();
//
//                    cv.put(UserRecordsDB.ID, note.getDate());
//                    cv.put(UserRecordsDB.NIK_NAME, note.getNikName());
//                    cv.put(UserRecordsDB.MOVES, Integer.parseInt(note.getMoves()));
//                    cv.put(UserRecordsDB.CODES, Integer.parseInt(note.getCodes()));
//                    cv.put(UserRecordsDB.TIME, note.getTime());
//                    cv.put(UserRecordsDB.USER_PHOTO_URL, note.getUserUrlPhoto());
//
//                    mArrayContentValues[i] = cv;
//
//                    ++i;
//                }
//
//                closeProgressBar();
//
//                new DBOperations().query(mCursorListener);
//
//                mCursorString = response.getmCursor();
//
//                return response;
//            }
//        };
//    }
//
//    private void firstTimeShowRecycler(Cursor pCursor) {
//        RecyclerView mRecordRecyclerView = findViewById(R.id.records_recycler_view);
////        Cursor cursor = new DBOperations().query();
//
////        Cursor cursor = new DBOperations().query();
//
//        adapter = new RecordRecyclerViewAdapter(this,  pCursor) {
//
//            @Override
//            public String showInfoFragment(String pUserName) {
//                String userName = super.showInfoFragment(pUserName);
//                closeInfoUserFragment();
//                showInfoUserFragment();
//                getUserInformation(userName);
//                return userName;
//            }
//        };
//
//        if(mArrayContentValues != null){
//            new DBOperations().bulkInsert(UserRecordsDB.TABLE, mArrayContentValues);
//        }
//
//        mRecordRecyclerView.setHasFixedSize(false);
//        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecordRecyclerView.addOnScrollListener(mScrollListener);
//        mRecordRecyclerView.setAdapter(adapter);
//
//        adapter.notifyDataSetChanged();
//    }
//
//    private void getUserInformation(String pUserName) {
//        UserBaseManager userManager = new UserBaseManager() {
//            @Override
//            public UserDataBase getFullUserInfoCallback(UserDataBase pUserData) {
//                UserDataBase user = super.getFullUserInfoCallback(pUserData);
//                mUserInfoFragment.showInfoAboutUser(RecordsCardActivityFromBD.this, user);
//
//                return pUserData;
//            }
//        };
//        userManager.checkInfoAboutUser(pUserName, "1111");
//    }
//
//    private void updateAdapter() {
//        RecordsManager manager = new RecordsManager() {
//
//            @Override
//            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
//                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);
//
//                closeProgressBar();
//
//                if (response.getmRecordsArray() != null) {
//                    mCursorString = response.getmCursor();
//                    isLoading = false;
//
//                    recordModelArrayList.addAll(response.getmRecordsArray());
//                    adapter.notifyDataSetChanged();
//                }
//
//                return response;
//            }
//        };
//
//        if (CheckConnection.checkConnection(this)) {
//            showProgressBar();
//            manager.getRecordSBackend(new RequestRecordModel(mCursorString));
//        }
//    }
//
//    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//            int visibleItems = manager.getChildCount();
//            int totalNumberItems = manager.getItemCount();
//            int firstVisible = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
//
//            if (!isLoading && totalNumberItems <= visibleItems + firstVisible) {
//                isLoading = true;
//
//                Log.d("MyLogs", "Stay near last position " + mCursorString);
//                showProgressBar();
//                updateAdapter();
//            }
//        }
//    };
//
//    private void showProgressBar() {
//        mRecordsProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    private void closeProgressBar() {
//        mRecordsProgressBar.setVisibility(View.GONE);
//    }
//
//    private void showInfoUserFragment() {
//        mInfoFrameLayout.setVisibility(View.VISIBLE);
//        mFragmentTransaction = getFragmentManager().beginTransaction();
//        mFragmentTransaction.add(R.id.user_info_record_frame_layout, mUserInfoFragment);
//        mFragmentTransaction.commit();
//        mFragmentManager = getFragmentManager();
//        mFragmentManager.executePendingTransactions();
//    }
//
//    private void closeInfoUserFragment() {
//        mInfoFrameLayout.setVisibility(View.INVISIBLE);
//        mFragmentTransaction = getFragmentManager().beginTransaction();
//        mFragmentTransaction.remove(mUserInfoFragment);
//        mFragmentTransaction.commit();
//    }
//
//    @Override
//    public void onClick(View v) {
//        closeInfoUserFragment();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        UserLoginHolder.getInstance().keepUserOnline();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mCursor.close();
//        if(mCursor.isClosed()){
//            Log.d("MyLogs", "Cursor closed");
//        }
//        UserLoginHolder.getInstance().setOffline();
//    }
}
