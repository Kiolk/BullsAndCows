package kiolk.com.github.pen.utils;

import android.content.Context;
@Deprecated
public class ContextHolderUtil {

    //TODO How do correct implement ContextHolder?
    private static ContextHolderUtil mContextHolder = null;
    private Context mContext;

    private ContextHolderUtil() {
    }

    public static ContextHolderUtil getInstance() {

        if (mContextHolder == null) {
            mContextHolder = new ContextHolderUtil();
        }
        return mContextHolder;
    }

    public void setContext(Context pContest) {
        mContextHolder.mContext = pContest;
    }

    public Context getContext() {
        return mContext;
    }
}
