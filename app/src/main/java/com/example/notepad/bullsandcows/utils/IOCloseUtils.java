package com.example.notepad.bullsandcows.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOCloseUtils {

    private final List<Closeable> mCloseableList;

    public IOCloseUtils() {
        mCloseableList = new ArrayList<>();
    }

    public IOCloseUtils addForClose(final Closeable pCloseableObject) {

        if (pCloseableObject != null) {
            mCloseableList.add(pCloseableObject);
        }

        return this;
    }

    public void close() {
        for (int i = 0; i < mCloseableList.size(); ++i) {
            try {
                mCloseableList.get(i).close();
            } catch (final IOException ignored) {

            }
        }
    }
}
